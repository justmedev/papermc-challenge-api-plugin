package at.iljabusch.challengeAPI;

import static org.apache.logging.log4j.LogManager.getLogger;
import at.iljabusch.challengeAPI.modifiers.Modifier;
import at.iljabusch.challengeAPI.modifiers.RegisteredModifier;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World.Environment;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.mvplugins.multiverse.core.MultiverseCoreApi;
import org.mvplugins.multiverse.core.utils.result.Attempt.Failure;
import org.mvplugins.multiverse.core.world.LoadedMultiverseWorld;
import org.mvplugins.multiverse.core.world.MultiverseWorld;
import org.mvplugins.multiverse.core.world.options.CreateWorldOptions;
import org.mvplugins.multiverse.core.world.options.DeleteWorldOptions;
import org.mvplugins.multiverse.core.world.reasons.CreateFailureReason;

@Getter

public class Challenge {

  private static final ChallengeAPI plugin = JavaPlugin.getPlugin(ChallengeAPI.class);

  private static final String WORLD_PREFIX = "world_challenge";

  private final LocalDateTime startedAt = LocalDateTime.now();
  private final ArrayList<UUID> playerUUIDs = new ArrayList<>();
  private final UUID creatorUUID;
  private final ChallengeWorlds worlds = new ChallengeWorlds();
  private final UUID worldUUID = UUID.randomUUID();
  private ChallengeState state;
  @Setter
  private Set<RegisteredModifier> registeredModifiers;
  private final Set<Modifier> modifiers = new HashSet<>();

  public Challenge(Player creator, Set<RegisteredModifier> modifiers) {
    creator.sendRichMessage("<gold>Creating challenge ...");
    this.registeredModifiers = modifiers;

    this.creatorUUID = creator.getUniqueId();
    this.playerUUIDs.add(this.creatorUUID);

    AtomicInteger successCount = new AtomicInteger();
    for (Environment env : List.of(Environment.NORMAL, Environment.NETHER, Environment.THE_END)) {
      MultiverseCoreApi.get().getWorldManager()
          .createWorld(
              CreateWorldOptions
                  .worldName(WORLD_PREFIX + "_" + worldUUID + "_" + env.name())
                  .environment(env)
          )
          .onSuccess(world -> {
            switch (world.getEnvironment()) {
              case NORMAL -> worlds.setNormal(world);
              case NETHER -> worlds.setNether(world);
              case THE_END -> worlds.setTheEnd(world);
            }

            var successes = successCount.getAndIncrement() + 1;
            if (successes >= 3) {
              this.state = ChallengeState.READY;

              creator.sendRichMessage(
                  """
                      <gold>Your challenge was created successfully!
                      Selected modifiers: <dark_red><modifiers></dark_red>
                      Use <dark_red><click:suggest_command:"/challenge invite ">/challenge invite <players></click></dark_red> to invite others to your challenge!
                      Use <dark_red><click:run_command:"/challenge start">/challenge start</click></dark_red> to start the challenge!""",
                  Placeholder.unparsed(
                      "modifiers",
                      String.join(
                          ", ",
                          modifiers.stream().map(RegisteredModifier::name).toList()
                      )
                  )
              );
            }
          })
          .onFailure(this::handleWorldCreationFailure)
      ;
    }
  }

  private Optional<Player> getCreator() {
    return getOnlinePlayers().stream().filter(p -> p.getUniqueId() == creatorUUID).findFirst();
  }

  public List<Player> getOnlinePlayers() {
    return playerUUIDs.stream()
        .map(p -> Bukkit.getServer().getPlayer(p))
        .filter(Objects::nonNull)
        .filter(OfflinePlayer::isOnline)
        .toList();
  }

  private void handleWorldCreationFailure(
      Failure<LoadedMultiverseWorld, CreateFailureReason> reason) {
    getLogger().error("Failed to create world for challenge: {}", reason);
    getCreator().ifPresent(
        player -> player.sendRichMessage("<red>Failed to create challenge!")
    );
    getOnlinePlayers().forEach(this::leave);
  }

  public void start() {
    if (this.state != ChallengeState.READY) {
      getCreator().ifPresent(
          player -> player.sendRichMessage("<red>Unable to start the challenge! It is not ready!")
      );
      return;
    }

    this.state = ChallengeState.ONGOING;
    this.registeredModifiers.forEach(registered -> {
      try {
        this.modifiers.add(registered.modifier().getConstructor(Challenge.class).newInstance(this));
      } catch (Exception e) {
        getLogger().error(
            "Unable to instantiate modifier! Did you forget to add a constructor with a challenge arg?");
        getLogger().error(e);
      }
    });

    AtomicInteger successCount = new AtomicInteger();
    getOnlinePlayers().forEach(p -> {
      p.sendRichMessage("<gold>Challenge started! Teleporting ...");
      p.showTitle(
          Title.title(Component.text("Loading ...", NamedTextColor.GOLD), Component.empty()));
      p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 200, 1, false, false));
      p.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 200, 255, false, false));
      p.sendBlockChange(
          worlds.normal.getSpawnLocation().clone().add(0, -1, 0), Material.GLASS.createBlockData());

      Utils.resetPlayerAdvancements(p);
      p.teleportAsync(worlds.normal.getSpawnLocation()).thenAccept(success -> {
        if (!success) {
          getLogger().error(
              "teleportAsync failed while trying to teleport players to started challenge!");
          // TODO: HANDLE
          return;
        }

        Utils.waitForChunksLoaded(
            p, p.getLocation(), () -> {
              successCount.getAndIncrement();
              p.setHealthScaled(false);
              p.setHealth(p.getAttribute(Attribute.MAX_HEALTH).getValue());
              p.setFoodLevel(20); // Fully fed
              p.clearActivePotionEffects();
              p.clearActiveItem();
              p.clearTitle();
              p.getInventory().clear();

              if (successCount.get() >= playerUUIDs.size()) {
                this.modifiers.forEach(Modifier::onChallengeStarted);
              }
            }
        );
      });
    });
  }

  public void join(Player player) {
    if (!this.playerUUIDs.contains(player.getUniqueId())) {
      this.playerUUIDs.add(player.getUniqueId());
    }
    if (this.state == ChallengeState.READY) {
      player.sendRichMessage("<gold>Challenge joined!");
      return;
    }

    if (this.state == ChallengeState.ONGOING) {
      modifiers.forEach(mod -> mod.onPlayerJoin(player));
    }
    player.sendRichMessage("<gold>Challenge rejoined!");
  }

  public void leave(Player player) {
    if (playerUUIDs.remove(player.getUniqueId()) && state.isOngoingOrCompleted()) {
      player.teleportAsync(MultiverseCoreApi.get()
                               .getWorldManager()
                               .getDefaultWorld()
                               .getOrNull()
                               .getSpawnLocation());
    }
    if (!playerUUIDs.isEmpty()) {
      return;
    }

    getLogger().info("Challenge closing because all players left!");
    modifiers.forEach(Modifier::onDispose);
    for (MultiverseWorld world : List.of(worlds.normal, worlds.nether, worlds.theEnd)) {
      MultiverseCoreApi.get()
          .getWorldManager()
          .deleteWorld(DeleteWorldOptions.world(world));
    }
  }

  public void leaveServer(Player player) {
    Bukkit.getScheduler().runTask(
        plugin,
        () -> this.modifiers.forEach(mod -> mod.onPlayerLeave(player))
    );
  }

  public void complete(boolean completedSuccessfully) {
    state = completedSuccessfully ? ChallengeState.COMPLETED : ChallengeState.FAILED;
    getOnlinePlayers().forEach(p -> {
      p.clearActivePotionEffects();
      p.getInventory().clear();
      p.setHealth(p.getAttribute(Attribute.MAX_HEALTH).getValue());
      p.setGameMode(GameMode.SPECTATOR);

      p.sendRichMessage(
          "<gold>You are no longer part of this challenge! use <click:run_command:\"/challenge leave\"><dark_red>/challenge leave</dark_red></click> to go back to the lobby!");
      if (completedSuccessfully) {
        p.showTitle(
            Title.title(
                Component.text("Completed!", NamedTextColor.GREEN, TextDecoration.BOLD),
                Component.text("You are now spectating the challenge")
            )
        );
        return;
      }

      p.showTitle(
          Title.title(
              Component.text("Failed!", NamedTextColor.RED, TextDecoration.BOLD),
              Component.text("You are now spectating the challenge")
          )
      );
    });
  }
}
