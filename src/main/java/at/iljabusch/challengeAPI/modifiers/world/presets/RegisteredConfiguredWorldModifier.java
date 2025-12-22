package at.iljabusch.challengeAPI.modifiers.world.presets;

import at.iljabusch.challengeAPI.ChallengeAPI;
import at.iljabusch.challengeAPI.challenges.Challenge;
import at.iljabusch.challengeAPI.modifiers.Modifier;
import at.iljabusch.challengeAPI.modifiers.RegisteredModifier;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.util.stream.Collectors;

import static org.apache.logging.log4j.LogManager.getLogger;


@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class RegisteredConfiguredWorldModifier extends RegisteredModifier {
  WorldModifierConfig config;

  public RegisteredConfiguredWorldModifier(String name, String author, Material material, WorldModifierConfig config) {
    super(name, author, material, ConfiguredWorldModifier.class);
    this.config = config;
  }


  //TODO: add these to WorldModifierConfig but returning a builder so the use can gor registerModifier(fromPlugin(plugin).envrionment().addGamerule() ...
  public static RegisteredConfiguredWorldModifier fromPlugin(Plugin plugin) {
    return fromPlugin(plugin, ChallengeAPI.DEFAULT_MATERIAL);
  }

  public static RegisteredConfiguredWorldModifier fromPlugin(String name) {
    return fromPlugin(name, ChallengeAPI.DEFAULT_MATERIAL, "world", null);
  }

  public static RegisteredConfiguredWorldModifier fromPlugin(String name, Material material) {
    return fromPlugin(name, material, "world", null);
  }

  public static RegisteredConfiguredWorldModifier fromPlugin(String name, Material material, String worldName, String generatorId) {
    Plugin plugin = Bukkit.getPluginManager().getPlugin(name);
    if (plugin == null) {
      getLogger().warn("Plugin '{}' not found! Could not register!", name);
      return null;
    }
    return fromPlugin(plugin, material, worldName, generatorId);
  }

  public static RegisteredConfiguredWorldModifier fromPlugin(Plugin plugin, Material material) {
    return fromPlugin(plugin, material, "world", null);
  }

  public static RegisteredConfiguredWorldModifier fromPlugin(Plugin plugin, Material material, String worldName, String generatorId) {
    try {
      plugin.getClass().getMethod("getDefaultWorldGenerator", String.class, String.class);
    } catch (NoSuchMethodException e) {
      getLogger().warn("Plugin '{}' does not seem to define a default world generator!", plugin.getName());
      return null;
    }
    RegisteredConfiguredWorldModifier registeredMod = new RegisteredConfiguredWorldModifier();
    registeredMod.setName(plugin.getName());
    registeredMod.setAuthor(plugin.getPluginMeta()
                                  .getAuthors()
                                  .stream()
                                  .limit(plugin
                                             .getPluginMeta()
                                             .getAuthors()
                                             .size() - 1)
                                  .collect(Collectors.joining(", ", "", ", "))
                                + plugin.getPluginMeta()
                                        .getAuthors()
                                        .getLast());
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

  public static RegisteredConfiguredWorldModifier getPresetConfiguredWorldModifier(WorldModifierPresets preset) {
    RegisteredConfiguredWorldModifier registeredMod = new RegisteredConfiguredWorldModifier();
    registeredMod.setName(preset.getDisplayName());
    registeredMod.setAuthor("Challenge API");
    registeredMod.setDisplayItem(preset.getPresetDefaultMaterial());
    registeredMod.setConfig(WorldModifierConfig.getPresetWorldModifierConfig(preset).build());

    return registeredMod;
  }

  @Override
  public Modifier createModifierInstance(Challenge challenge) {
    try {
      if (this.getModifier().isInstance(ConfiguredWorldModifier.class)) {
        return this.getModifier().getDeclaredConstructor(Challenge.class, WorldModifierConfig.class).newInstance(challenge, config);
      } else {
        return this.getModifier().getDeclaredConstructor(Challenge.class).newInstance(challenge);
      }
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      getLogger().error("Could not instantiate {} starting challenge {} without!", this.getModifier().getName(), challenge.getWorldUUID());
      getLogger().error(e.getMessage());
    }
    return null;
  }


}
