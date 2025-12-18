package at.iljabusch.challengeAPI;

import static org.apache.logging.log4j.LogManager.getLogger;
import at.iljabusch.challengeAPI.modifiers.RegisteredModifier;
import java.util.ArrayList;
import java.util.List;
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
import org.bukkit.GameMode;
import org.bukkit.World.Environment;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.mvplugins.multiverse.core.MultiverseCoreApi;
import org.mvplugins.multiverse.core.utils.result.Attempt.Failure;
import org.mvplugins.multiverse.core.world.LoadedMultiverseWorld;
import org.mvplugins.multiverse.core.world.MultiverseWorld;
import org.mvplugins.multiverse.core.world.options.CreateWorldOptions;
import org.mvplugins.multiverse.core.world.options.DeleteWorldOptions;
import org.mvplugins.multiverse.core.world.reasons.CreateFailureReason;

@Getter

public class Challenge {

  private static final String WORLD_PREFIX = "world_challenge";

  private final ArrayList<Player> players = new ArrayList<>();
  private final UUID creatorUUID;
  private ChallengeState state;
  private final ChallengeWorlds worlds = new ChallengeWorlds();
  private final UUID worldUUID = UUID.randomUUID();
  @Setter
  private Set<RegisteredModifier> modifiers;

  public Challenge(Player creator, Set<RegisteredModifier> modifiers) {
    creator.sendRichMessage("<gold>Creating challenge ...");
    this.modifiers = modifiers;

    this.creatorUUID = creator.getUniqueId();
    this.players.add(creator);

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
    return players.stream().filter(p -> p.getUniqueId() == creatorUUID).findFirst();
  }

  private void handleWorldCreationFailure(
      Failure<LoadedMultiverseWorld, CreateFailureReason> reason) {
    getLogger().error("Failed to create world for challenge: {}", reason);
    getCreator().ifPresent(
        player -> player.sendRichMessage("<red>Failed to create challenge!")
    );
    players.forEach(this::leave);
  }

  public void start() {
    if (this.state != ChallengeState.READY) {
      getCreator().ifPresent(
          player -> player.sendRichMessage("<red>Unable to start the challenge! It is not ready!")
      );
      return;
    }

    this.state = ChallengeState.ONGOING;
    this.modifiers.forEach(registered -> {
      try {
        var mod = registered.modifier().getConstructor(Challenge.class).newInstance(this);
        mod.onChallengeStarted(); // TODO: this should be called after each player is teleported!
      } catch (Exception e) {
        getLogger().error(
            "Unable to instantiate modifier! Did you forget to add a constructor with a challenge arg?");
        getLogger().error(e);
      }
    });

    players.forEach(p -> {
      p.sendRichMessage("<gold>Challenge started! Teleporting ...");
      p.showTitle(
          Title.title(Component.text("Loading ...", NamedTextColor.GOLD), Component.empty()));

      p.teleportAsync(worlds.normal.getSpawnLocation()).thenAccept(success -> {
        if (!success) {
          getLogger().error(
              "teleportAsync failed while trying to teleport players to started challenge!");
          // TODO: HANDLE
          return;
        }

        p.setHealthScaled(false);
        p.setHealth(p.getAttribute(Attribute.MAX_HEALTH).getValue());
        p.setFoodLevel(20); // Fully fed
        p.clearActivePotionEffects();
        p.clearActiveItem();
        p.clearTitle();
        p.getInventory().clear();
      });
    });
  }

  public void join(Player player) {
    players.add(player);
    if (this.state == ChallengeState.READY) {
      player.sendRichMessage("<gold>Challenge joined!");
      return;
    }
    player.sendRichMessage("<gold>Challenge rejoined!");
  }

  public void leave(Player player) {
    if (players.remove(player) && state.hasStartedOrCompleted()) {
      player.teleportAsync(MultiverseCoreApi.get()
                               .getWorldManager()
                               .getDefaultWorld()
                               .getOrNull()
                               .getSpawnLocation());
    }
    if (!players.isEmpty()) {
      return;
    }

    getLogger().info("Challenge closing because all players left!");

    for (MultiverseWorld world : List.of(worlds.normal, worlds.nether, worlds.theEnd)) {
      MultiverseCoreApi.get()
          .getWorldManager()
          .deleteWorld(DeleteWorldOptions.world(world));
    }
  }

  public void complete() {
    complete(false);
  }

  public void complete(boolean completedSuccessfully) {
    state = completedSuccessfully ? ChallengeState.COMPLETED : ChallengeState.FAILED;
    players.forEach(p -> {
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
