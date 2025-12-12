package at.iljabusch.challengeAPI.challenges.sharedhealth;

import static org.apache.logging.log4j.LogManager.getLogger;
import at.iljabusch.challengeAPI.Challenge;
import at.iljabusch.challengeAPI.ChallengeCreationException;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

public class SharedHealthChallenge implements Challenge {

  private ArrayList<Player> players;

  public void onCreate(List<Player> players) throws ChallengeCreationException {
    if (players.size() < 2) {
      throw new ChallengeCreationException("Challenge requires at least 2 players");
    }
    players.forEach(player -> {
      player.setHealthScaled(false);
      player.setHealth(player.getAttribute(Attribute.MAX_HEALTH).getValue());
    });
    this.players = new ArrayList<>(players);
  }

  public void onJoin(Player player) {
    player.setHealth(players.getFirst().getHealth());
    players.add(player);
  }

  public void onLeave(Player player) {
    players.remove(player);
    if (!players.isEmpty()) return;

    getLogger().info("Challenge closing because all players left!");
  }
}
