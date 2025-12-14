package at.iljabusch.challengeAPI;

import at.iljabusch.challengeAPI.modifiers.RegisteredModifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class ChallengeManager {

  // region Singleton Pattern
  private static ChallengeManager instance;

  private ChallengeManager() {
  }

  public static synchronized ChallengeManager getInstance() {
    if (instance == null) {
      instance = new ChallengeManager();
    }
    return instance;
  }

  // endregion Singleton Pattern
  private final ArrayList<RegisteredModifier> registeredModifiers = new ArrayList<>();
  private final HashMap<UUID, PlayerInChallenge> playersInChallenges = new HashMap<>();
  private final ArrayList<Challenge> activeChallenges = new ArrayList<>();

  public void registerNewChallenge(Challenge challenge, Player creator) {
    playersInChallenges.put(creator.getUniqueId(), new PlayerInChallenge(challenge, creator));
    activeChallenges.add(challenge);
  }

  public void registerPlayersInChallenge(Challenge challenge, Collection<Player> players) {
    players.forEach(p -> {
      playersInChallenges.put(p.getUniqueId(), new PlayerInChallenge(challenge, p));
    });
  }

  public void registerModifier(RegisteredModifier registeredModifier) {
    registeredModifiers.add(registeredModifier);
  }
}
