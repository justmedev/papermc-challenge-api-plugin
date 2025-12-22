package at.iljabusch.challengeAPI.challenges;

import at.iljabusch.challengeAPI.ChallengeAPI;
import at.iljabusch.challengeAPI.challenges.events.ChallengePlayerJoinEvent;
import at.iljabusch.challengeAPI.challenges.events.ChallengePlayerLeaveEvent;
import at.iljabusch.challengeAPI.challenges.events.ChallengeStartedEvent;
import at.iljabusch.challengeAPI.modifiers.Modifier;
import at.iljabusch.challengeAPI.modifiers.RegisteredModifier;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.World.Environment;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.apache.logging.log4j.LogManager.getLogger;

@Getter
public class Challenge {
  private static final ChallengeAPI plugin = JavaPlugin.getPlugin(ChallengeAPI.class);

  private static final String WORLD_PREFIX = "world_challenge";

  private final LocalDateTime startedAt = LocalDateTime.now();
  private final ArrayList<UUID> playerUUIDs = new ArrayList<>();
  private final UUID creatorUUID;
  private final ChallengeWorlds worlds = new ChallengeWorlds();
  private final UUID worldUUID = UUID.randomUUID();
  private final PluginManager pluginManager = plugin.getServer().getPluginManager();
  private final AtomicReference<WorldCreator> overworldCreator = new AtomicReference<>();
  private final AtomicReference<WorldCreator> netherCreator = new AtomicReference<>();
  private final AtomicReference<WorldCreator> endCreator = new AtomicReference<>();
  private final EventEmitter eventEmitter = new EventEmitter(this);
  private ChallengeState state;
  @Setter
  private Set<RegisteredModifier> registeredModifiers;
  @Setter
  private Set<Modifier> modifiers = new HashSet<>();

  public Challenge(Player creator, Set<RegisteredModifier> options) {
    creator.sendRichMessage("<gold>Creating challenge ...");
    this.registeredModifiers = options;

    this.creatorUUID = creator.getUniqueId();
    this.playerUUIDs.add(this.creatorUUID);

    var success = true;
    for (Environment env : List.of(Environment.NORMAL, Environment.NETHER, Environment.THE_END)) {
      try {
        AtomicReference<WorldCreator> ref = switch (env) {
          case NORMAL -> overworldCreator;
          case NETHER -> netherCreator;
          case THE_END -> endCreator;
          //TODO: support custom worlds created by modifiers or something i dont know sigma sigma boy
          case CUSTOM -> null;
        };

        WorldCreator worldCreator = ref.get();
        if (worldCreator == null) {
          worldCreator = new WorldCreator(WORLD_PREFIX + "_" + worldUUID + "_" + env.name())
              .environment(env)
              .type(WorldType.NORMAL)
              .generateStructures(true);
          ref.set(worldCreator);
        } else {
          WorldCreator.name(WORLD_PREFIX + "_" + worldUUID + "_" + env.name());
        }

        World world = worldCreator.createWorld();

        switch (world.getEnvironment()) {
          case NORMAL -> worlds.setNormal(world);
          case NETHER -> worlds.setNether(world);
          case THE_END -> worlds.setTheEnd(world);
        }
      } catch (Exception reason) {
        getLogger().error("Failed to create world for challenge", reason);
        getCreator().ifPresent(
            player -> player.sendRichMessage("<red>Failed to create challenge!")
        );
        getOnlinePlayers().forEach(this::leave);
        success = false;
      }
    }

    if (!success) return;
    this.state = ChallengeState.READY;

    creator.sendRichMessage(
        """
            <gold>Your challenge was created successfully!
            Selected modifiers: <dark_red><modifiers></dark_red>
            Use <dark_red><click:suggest_command:"/challenge invite ">/challenge invite <players></click></dark_red> to invite others to your challenge!
            Use <dark_red><click:run_command:"/challenge start">/challenge start</click></dark_red> to start the challenge!""",
        Placeholder.unparsed(
            "modifiers",
            String.join(", ", registeredModifiers.stream().map(RegisteredModifier::getName).toList())
        )
    );
  }

  public boolean setWorldCreator(WorldCreator creator) {
    switch (creator.environment()) {
      case NETHER:
        return setSingleWorldCreator(overworldCreator, creator);
      case THE_END:
        return setSingleWorldCreator(netherCreator, creator);
      case NORMAL:
        return setSingleWorldCreator(endCreator, creator);

    }
    return false;
  }

  private boolean setSingleWorldCreator(AtomicReference<WorldCreator> ref, WorldCreator creator) {
    boolean success = ref.compareAndSet(null, creator);
    if (!success) {
      getLogger().warn("Duplicate WorldMoifiers!\nCannot assign WorldModifier " + creator.environment().name() + " was already set!");
    }
    return success;
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
        modifiers.add(registered.createModifierInstance(this));
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
                pluginManager.callEvent(new ChallengeStartedEvent(this));
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
      pluginManager.callEvent(new ChallengePlayerJoinEvent(this, player));
    }
    player.sendRichMessage("<gold>Challenge rejoined!");
  }

  public void leave(Player player) {
    if (playerUUIDs.remove(player.getUniqueId()) && state.isOngoingOrCompleted()) {
      player.teleportAsync(Bukkit.getWorld("world").getSpawnLocation()); // TODO: dynamic default world name
    }
    if (!playerUUIDs.isEmpty()) {
      return;
    }

    getLogger().info("Challenge closing because all players left!");
    for (World world : List.of(worlds.normal, worlds.nether, worlds.theEnd)) {
      if (!Utils.deleteBukkitWorld(world)) {
        getLogger().warn("Unable to delete world {}!", world.getName());
      }
    }
  }

  public void leaveServer(Player player) {
    Bukkit.getScheduler().runTask(
        plugin,
        () -> pluginManager.callEvent(new ChallengePlayerLeaveEvent(this, player))
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
