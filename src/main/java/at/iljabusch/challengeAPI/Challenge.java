package at.iljabusch.challengeAPI;

import static org.apache.logging.log4j.LogManager.getLogger;
import at.iljabusch.challengeAPI.modifiers.RegisteredModifier;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.World.Environment;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.mvplugins.multiverse.core.MultiverseCoreApi;
import org.mvplugins.multiverse.core.world.MultiverseWorld;
import org.mvplugins.multiverse.core.world.options.CreateWorldOptions;

@Getter

public class Challenge {

  private ChallengeState state;

  private MultiverseWorld world;
  private final ArrayList<Player> players = new ArrayList<>();
  @Setter
  private Set<RegisteredModifier> modifiers;

  public Challenge(Player creator, Set<RegisteredModifier> modifiers) {
    creator.sendRichMessage("<gold>Creating challenge ...");
    this.modifiers = modifiers;

    this.players.add(creator);
    this.players.forEach(player -> {
      player.setHealthScaled(false);
      player.setHealth(player.getAttribute(Attribute.MAX_HEALTH).getValue());
      player.setFoodLevel(20); // Fully fed
    });

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

    players.forEach(p -> {
      p.sendRichMessage("<gold>Challenge started!");
      p.teleport(world.getSpawnLocation());
    });
  }

  public void join(Player player) {
    player.sendRichMessage("<gold>Rejoining challenge!");
    players.add(player);
    if (this.state == ChallengeState.ONGOING) {
      player.teleport(world.getSpawnLocation());
    }
  }

  public void leave(Player player) {
    players.remove(player);
    if (!players.isEmpty()) {
      return;
    }

    getLogger().info("Challenge closing because all players left!");
    // TODO: Actually close challenge, delete world, ...
  }
}
