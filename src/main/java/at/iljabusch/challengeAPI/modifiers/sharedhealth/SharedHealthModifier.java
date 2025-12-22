package at.iljabusch.challengeAPI.modifiers.sharedhealth;

import at.iljabusch.challengeAPI.ChallengeAPI;
import at.iljabusch.challengeAPI.challenges.Challenge;
import at.iljabusch.challengeAPI.modifiers.Modifier;
import org.bukkit.plugin.java.JavaPlugin;

public class SharedHealthModifier extends Modifier {

  public SharedHealthModifier(Challenge challenge) {
    super(challenge);
    challenge.getEventEmitter().registerEvents(new SharedHealthModifierEventListener(this), JavaPlugin.getPlugin(ChallengeAPI.class));
  }
}
