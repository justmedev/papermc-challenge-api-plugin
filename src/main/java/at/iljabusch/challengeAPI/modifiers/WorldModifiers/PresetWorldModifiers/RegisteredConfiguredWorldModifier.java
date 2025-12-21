package at.iljabusch.challengeAPI.modifiers.WorldModifiers.PresetWorldModifiers;

import at.iljabusch.challengeAPI.ChallengeAPI;
import at.iljabusch.challengeAPI.Challenges.Challenge;
import at.iljabusch.challengeAPI.modifiers.RegisteredModifier;
import at.iljabusch.challengeAPI.modifiers.Modifier;
import lombok.Builder;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.stream.Collectors;

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


    //TODO: add these to WorldModifierConfig but returning a builder so the use can gor registerModifier(fromPlugin(plugin).envrionment().addGamerule() ...
    public static RegisteredConfiguredWorldModifier fromPlugin(Plugin plugin) {return fromPlugin(plugin, ChallengeAPI.DEFAULT_MATERIAL);}
    public static RegisteredConfiguredWorldModifier fromPlugin(String name) {return fromPlugin(name, ChallengeAPI.DEFAULT_MATERIAL, "world", null);}
    public static RegisteredConfiguredWorldModifier fromPlugin(String name, Material material) {return fromPlugin(name, material, "world", null);}
    public static RegisteredConfiguredWorldModifier fromPlugin(String name, Material material, String worldName, String generatorId) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin(name);
        if (plugin == null) {
            Bukkit.getLogger().warning("Plugin '" + name + "' not found! Could not register!");
            return null;
        }
        return fromPlugin(plugin, material, worldName, generatorId);
    }
    public static RegisteredConfiguredWorldModifier fromPlugin(Plugin plugin, Material material) {return fromPlugin(plugin, material, "world", null);}
    public static RegisteredConfiguredWorldModifier fromPlugin(Plugin plugin, Material material, String worldName, String generatorId) {
        try {
            plugin.getClass().getMethod("getDefaultWorldGenerator", String.class, String.class);
        } catch (NoSuchMethodException e) {
            Bukkit.getLogger().warning("Plugin '" + plugin.getName() + "' does not seem to define a default world generator!");
            return null;
        }
       RegisteredConfiguredWorldModifier registeredMod = new RegisteredConfiguredWorldModifier();
        registeredMod.setName(plugin.getName());
        registeredMod.setAuthor(plugin.getDescription().getAuthors().stream()
                .limit(plugin.getDescription().getAuthors().size() - 1)
                .collect(Collectors.joining(", ", "", ", "))
                + plugin.getDescription().getAuthors().getLast());
        registeredMod.setDisplayItem(material);
        registeredMod.setConfig(WorldModifierConfig.builder().chunkGenerator(plugin.getDefaultWorldGenerator(worldName, generatorId)).build());
        return registeredMod;
    }
    public static <T extends ChunkGenerator> RegisteredConfiguredWorldModifier fromGenerator(T generator, Material material) {
        RegisteredConfiguredWorldModifier registeredMod = new RegisteredConfiguredWorldModifier();
        registeredMod.setName(generator.getClass().getSimpleName());
        registeredMod.setAuthor("Unknown");
        registeredMod.setDisplayItem(material);
        registeredMod.setConfig(
                WorldModifierConfig.builder().chunkGenerator(generator).build()
        );
        return registeredMod;
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
