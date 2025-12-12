package at.iljabusch.challengeAPI;

import static org.apache.logging.log4j.LogManager.getLogger;
import at.iljabusch.challengeAPI.modifiers.Modifier;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.World.Environment;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.mvplugins.multiverse.core.MultiverseCoreApi;
import org.mvplugins.multiverse.core.world.MultiverseWorld;
import org.mvplugins.multiverse.core.world.options.CreateWorldOptions;

@Getter

public class Challenge {

  private MultiverseWorld world;
  private ArrayList<Player> players;
  @Setter
  private Set<Modifier> modifiers;

  public Challenge(ArrayList<Player> players) {
    getLogger().info("Creating a new challenge!");
    this.players = players;
    this.players.forEach(player -> {
      player.setHealthScaled(false);
      player.setHealth(player.getAttribute(Attribute.MAX_HEALTH).getValue());
      player.setFoodLevel(20); // Fully fed
    });

    // TODO: Delete world once challenge ends
    MultiverseCoreApi.get().getWorldManager()
        .createWorld(
            CreateWorldOptions.worldName("world_challenge_" + UUID.randomUUID())
                .environment(Environment.NORMAL))
        .onFailure(reason -> {
          getLogger().error("Failed to create world for challenge: {}", reason);
        })
        .onSuccess(world -> {
          this.world = world;
          players.forEach(p -> p.teleport(world.getSpawnLocation()));
        });
  }

  public void onJoin(Player player) {
    players.add(player);
    player.teleport(world.getSpawnLocation());
  }

  public void onLeave(Player player) {
    players.remove(player);
    if (!players.isEmpty()) {
      return;
    }

    getLogger().info("Challenge closing because all players left!");
    // TODO: Actually close challenge, delete world, ...
  }
}
