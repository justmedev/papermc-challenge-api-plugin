package at.iljabusch.challengeAPI;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class PluginState {

  // region Singleton Pattern
  private static PluginState instance;

  private PluginState() {
  }

  public static synchronized PluginState getInstance() {
    if (instance == null) {
      instance = new PluginState();
    }
    return instance;
  }

  // endregion Singleton Pattern
  private HashMap<UUID, PlayerInChallenge> playersInChallenges = new HashMap<>();
  private ArrayList<Challenge> activeChallenges = new ArrayList<>();

  public void registerNewChallenge(Challenge challenge, Collection<Player> players) {
    players.forEach(p -> {
      playersInChallenges.put(p.getUniqueId(), new PlayerInChallenge(challenge, p));
    });
    activeChallenges.add(challenge);
  }
}
