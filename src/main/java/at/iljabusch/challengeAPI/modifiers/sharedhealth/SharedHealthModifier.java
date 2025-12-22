package at.iljabusch.challengeAPI.modifiers.sharedhealth;

import at.iljabusch.challengeAPI.ChallengeAPI;
import at.iljabusch.challengeAPI.challenges.Challenge;
import at.iljabusch.challengeAPI.modifiers.Modifier;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NonNull;

public class SharedHealthModifier extends Modifier {

  public SharedHealthModifier(@NonNull Challenge challenge) {
    super(challenge);
    challenge.getEventEmitter().registerEvents(new SharedHealthModifierEventListener(this), JavaPlugin.getPlugin(ChallengeAPI.class));
  }
}
