package at.iljabusch.challengeAPI.challenges.sharedhealth;

import static org.apache.logging.log4j.LogManager.getLogger;
import at.iljabusch.challengeAPI.Challenge;
import at.iljabusch.challengeAPI.ChallengeCreationException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.World.Environment;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.mvplugins.multiverse.core.MultiverseCoreApi;
import org.mvplugins.multiverse.core.world.MultiverseWorld;
import org.mvplugins.multiverse.core.world.options.CreateWorldOptions;

public class SharedHealthChallenge implements Challenge {

  private ArrayList<Player> players;
  private MultiverseWorld world;

  public void onCreate(List<Player> players) throws ChallengeCreationException {
    if (players.size() < 2) {
      throw new ChallengeCreationException("Challenge requires at least 2 players");
    }
    players.forEach(player -> {
      player.setHealthScaled(false);
      player.setHealth(player.getAttribute(Attribute.MAX_HEALTH).getValue());
    });
    this.players = new ArrayList<>(players);

    // TODO: Delete world once challenge ends
    MultiverseCoreApi.get().getWorldManager()
        .createWorld(
            CreateWorldOptions.worldName("world_" + getClass().getSimpleName() + UUID.randomUUID())
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
    player.setHealth(players.getFirst().getHealth());
    players.add(player);
    player.teleport(world.getSpawnLocation());
  }

  public void onLeave(Player player) {
    players.remove(player);
    if (!players.isEmpty()) return;

    getLogger().info("Challenge closing because all players left!");
  }
}
