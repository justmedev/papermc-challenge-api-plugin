package at.iljabusch.challengeAPI;

import static org.apache.logging.log4j.LogManager.getLogger;
import at.iljabusch.challengeAPI.modifiers.RegisteredModifier;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.title.Title;
import org.bukkit.World.Environment;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.mvplugins.multiverse.core.MultiverseCoreApi;
import org.mvplugins.multiverse.core.world.MultiverseWorld;
import org.mvplugins.multiverse.core.world.options.CreateWorldOptions;

@Getter

public class Challenge {

  private final ArrayList<Player> players = new ArrayList<>();
  private final UUID creatorUUID;
  private ChallengeState state;
  private MultiverseWorld world;
  @Setter
  private Set<RegisteredModifier> modifiers;

  public Challenge(Player creator, Set<RegisteredModifier> modifiers) {
    creator.sendRichMessage("<gold>Creating challenge ...");
    this.modifiers = modifiers;

    this.creatorUUID = creator.getUniqueId();
    this.players.add(creator);

    // TODO: Delete world once challenge ends
    MultiverseCoreApi.get().getWorldManager()
        .createWorld(
            CreateWorldOptions
                .worldName("world_challenge_" + UUID.randomUUID())
                .environment(Environment.NORMAL)
        )
        .onFailure(reason -> {
          getLogger().error("Failed to create world for challenge: {}", reason);
          // TODO: Handle this
        })
        .onSuccess(world -> {
          this.world = world;
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
        });
  }

  public void start() {
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

      p.teleportAsync(world.getSpawnLocation()).thenAccept(success -> {
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
    if (players.remove(player) && state == ChallengeState.ONGOING) {
      player.teleportAsync(MultiverseCoreApi.get().getWorldManager().getDefaultWorld().getOrNull()
                               .getSpawnLocation());
    }
    if (!players.isEmpty()) {
      return;
    }

    getLogger().info("Challenge closing because all players left!");
    // TODO: Actually close challenge, delete world, ...
  }
}
