package at.iljabusch.challengeAPI.modifiers.WorldModifiers.PresetWorldModifiers;

import at.iljabusch.challengeAPI.Challenges.Challenge;
import at.iljabusch.challengeAPI.modifiers.RegisteredModifier;
import at.iljabusch.challengeAPI.modifiers.Modifier;
import lombok.Data;
import org.bukkit.Bukkit;

import java.lang.reflect.InvocationTargetException;

@Data
public class RegisteredConfiguredWorldModifier extends RegisteredModifier {
    WorldModifierConfig config;

    @Override
    public <G extends Modifier> G createModifierInstance(Challenge challenge) {
        try {
            if(this.getModifier().isInstance(ConfiguredWorldModifier.class)){
                return (G) this.getModifier().getDeclaredConstructor(Challenge.class, WorldModifierConfig.class).newInstance(challenge, config);
            }
            else{
                return (G) this.getModifier().getDeclaredConstructor(Challenge.class).newInstance(challenge);
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            Bukkit.getLogger().severe("Could not instantiate " + this.getModifier().getName() + " starting challenge " + challenge.getWorldUUID() + " without!");
            Bukkit.getLogger().severe(e.getMessage());
        }
        return null;
    }





    public static RegisteredConfiguredWorldModifier getPresetConfiguredWorldModifier(WorldModifierPresets preset){
        RegisteredConfiguredWorldModifier registeredMod = new RegisteredConfiguredWorldModifier();
        registeredMod.setName(preset.getDisplayName());
        registeredMod.setAuthor("Challenge API");
        registeredMod.setDisplayItem(preset.getPresetDefaultMaterial());
        registeredMod.setConfig(WorldModifierConfig.getPresetWorldModifierConfig(preset));

        return registeredMod;
    }


}
