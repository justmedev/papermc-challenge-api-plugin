package at.iljabusch.challengeAPI;

import at.iljabusch.challengeAPI.modifiers.Modifier;
import at.iljabusch.challengeAPI.modifiers.RegisteredModifier;
import at.iljabusch.challengeAPI.modifiers.sharedhealth.SharedHealthModifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;

@Getter
public class GlobalState {

  // region Singleton Pattern
  private static GlobalState instance;

  private GlobalState() {
  }

  public static synchronized GlobalState getInstance() {
    if (instance == null) {
      instance = new GlobalState();
    }
    return instance;
  }

  // endregion Singleton Pattern
  private final ArrayList<RegisteredModifier> registeredModifiers = new ArrayList<>();
  private final HashMap<UUID, PlayerInChallenge> playersInChallenges = new HashMap<>();
  private final ArrayList<Challenge> activeChallenges = new ArrayList<>();

  public void registerNewChallenge(Challenge challenge, Collection<Player> players) {
    players.forEach(p -> {
      playersInChallenges.put(p.getUniqueId(), new PlayerInChallenge(challenge, p));
    });
    activeChallenges.add(challenge);
  }

  public void registerModifier(RegisteredModifier registeredModifier) {
    registeredModifiers.add(registeredModifier);
  }
}
