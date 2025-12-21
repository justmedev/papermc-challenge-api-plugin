package at.iljabusch.challengeAPI.modifiers.sharedhealth;

import static org.bukkit.Bukkit.getServer;
import at.iljabusch.challengeAPI.ChallengeAPI;
import at.iljabusch.challengeAPI.Challenges.Challenge;
import at.iljabusch.challengeAPI.modifiers.Modifier;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SharedHealthModifier extends Modifier {

  private final SharedHealthModifierEventListener eventListener = new SharedHealthModifierEventListener(
      this);

  public SharedHealthModifier(Challenge challenge) {
    super(challenge);
    challenge.registerEvents(eventListener, JavaPlugin.getPlugin(ChallengeAPI.class));

  }

}
