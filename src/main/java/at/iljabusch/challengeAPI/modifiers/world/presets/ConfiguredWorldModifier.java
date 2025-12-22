package at.iljabusch.challengeAPI.modifiers.world.presets;

import at.iljabusch.challengeAPI.ChallengeAPI;
import at.iljabusch.challengeAPI.challenges.Challenge;
import at.iljabusch.challengeAPI.modifiers.Modifier;
import org.jspecify.annotations.NonNull;


public class ConfiguredWorldModifier extends Modifier {
  private final WorldModifierConfig modifierConfig;

  public ConfiguredWorldModifier(@NonNull Challenge challenge, WorldModifierConfig modifierConfig) {
    super(challenge);
    this.modifierConfig = modifierConfig;

    challenge.getEventEmitter().registerEvents(new ConfiguredWorldModifierListener(modifierConfig), ChallengeAPI.getPlugin(ChallengeAPI.class));
  }
}
