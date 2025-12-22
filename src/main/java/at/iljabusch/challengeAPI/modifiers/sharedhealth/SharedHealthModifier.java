package at.iljabusch.challengeAPI.modifiers.sharedhealth;

import at.iljabusch.challengeAPI.ChallengeAPI;
import at.iljabusch.challengeAPI.challenges.Challenge;
import at.iljabusch.challengeAPI.modifiers.Modifier;
import org.bukkit.plugin.java.JavaPlugin;

public class SharedHealthModifier extends Modifier {

  public SharedHealthModifier(Challenge challenge) {
    super(challenge);
    SharedHealthModifierEventListener eventListener = new SharedHealthModifierEventListener(this);
    challenge.registerEvents(eventListener, JavaPlugin.getPlugin(ChallengeAPI.class));
  }

}
