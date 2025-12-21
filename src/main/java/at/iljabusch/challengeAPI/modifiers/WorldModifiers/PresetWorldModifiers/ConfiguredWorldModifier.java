package at.iljabusch.challengeAPI.modifiers.WorldModifiers.PresetWorldModifiers;

import at.iljabusch.challengeAPI.ChallengeAPI;
import at.iljabusch.challengeAPI.Challenges.Challenge;
import at.iljabusch.challengeAPI.modifiers.Modifier;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;


public class ConfiguredWorldModifier extends Modifier {
    private final WorldModifierConfig modifierConfig;

    public ConfiguredWorldModifier(Challenge challenge, WorldModifierConfig modifierConfig) {
        super(challenge);
        this.modifierConfig = modifierConfig;

        challenge.registerEvents(new ConfiguredWorldModifierListener(modifierConfig), ChallengeAPI.getPlugin(ChallengeAPI.class));
    }


}
