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
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

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


  public static RegisteredConfiguredWorldModifier fromPlugin(@NonNull Plugin plugin) {
    return fromPlugin(plugin, ChallengeAPI.DEFAULT_MATERIAL);
  }

  public static @Nullable RegisteredConfiguredWorldModifier fromPlugin(@NonNull String name) {
    return fromPlugin(name, ChallengeAPI.DEFAULT_MATERIAL, "world", null);
  }

  public static @Nullable RegisteredConfiguredWorldModifier fromPlugin(@NonNull String name, Material material) {
    return fromPlugin(name, material, "world", null);
  }

  public static @Nullable RegisteredConfiguredWorldModifier fromPlugin(@NonNull String name, Material material, @NonNull String worldName, String generatorId) {
    Plugin plugin = Bukkit.getPluginManager().getPlugin(name);
    if (plugin == null) {
      getLogger().warn("Plugin '{}' not found! Could not register!", name);
      return null;
    }
    return fromPlugin(plugin, material, worldName, generatorId);
  }

  public static @Nullable RegisteredConfiguredWorldModifier fromPlugin(@NonNull Plugin plugin, Material material) {
    return fromPlugin(plugin, material, "world", null);
  }

  public static @Nullable RegisteredConfiguredWorldModifier fromPlugin(@NonNull Plugin plugin, Material material, @NonNull String worldName, String generatorId) {
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

  public static <T extends ChunkGenerator> @NonNull RegisteredConfiguredWorldModifier fromGenerator(@NonNull T generator, Material material) {
    RegisteredConfiguredWorldModifier registeredMod = new RegisteredConfiguredWorldModifier();
    registeredMod.setName(generator.getClass().getSimpleName());
    registeredMod.setAuthor("Unknown");
    registeredMod.setDisplayItem(material);
    registeredMod.setConfig(
        WorldModifierConfig.builder().chunkGenerator(generator).build()
    );
    return registeredMod;
  }

  public static @NonNull RegisteredConfiguredWorldModifier getPresetConfiguredWorldModifier(@NonNull WorldModifierPresets preset) {
    RegisteredConfiguredWorldModifier registeredMod = new RegisteredConfiguredWorldModifier();
    registeredMod.setName(preset.getDisplayName());
    registeredMod.setAuthor("Challenge API");
    registeredMod.setDisplayItem(preset.getPresetDefaultMaterial());
    registeredMod.setConfig(WorldModifierConfig.getPresetWorldModifierConfig(preset).build());

    return registeredMod;
  }

  @Override
  public @Nullable Modifier createModifierInstance(@NonNull Challenge challenge) {
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
