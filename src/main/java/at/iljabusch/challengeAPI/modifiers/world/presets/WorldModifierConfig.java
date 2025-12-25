package at.iljabusch.challengeAPI.modifiers.world.presets;

import at.iljabusch.challengeAPI.ChallengeAPI;
import lombok.Builder;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.BlockType;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;

import static org.apache.logging.log4j.LogManager.getLogger;

@Builder
public class WorldModifierConfig {

  @Builder.Default
  protected @NonNull HashMap<GameRule, Object> gameRules = new HashMap<>();
  @Builder.Default
  protected @Nullable Biome singleBiomeWorldBiome = null;
  @Builder.Default
  protected boolean generateStructures = true;
  @Builder.Default
  protected boolean spawnBonusChest = false;
  @Builder.Default
  protected long seed = 0;
  protected ChunkGenerator chunkGenerator;
  @Builder.Default
  protected GameMode gameMode = GameMode.SURVIVAL;
  @Builder.Default
  protected World.Environment environment = World.Environment.NORMAL;
  protected Difficulty difficulty;
  @Builder.Default
  protected @NonNull Boolean allowMonsterSpawning = true;
  @Builder.Default
  protected @NonNull Boolean allowAnimalSpawning = true;
  @Builder.Default
  protected int clearWeatherDurationTicks = 0;
  protected Vector spawnLocation;
  protected FlatGeneratorSettings generatorSettings;
  @Builder.Default
  WorldType worldType = WorldType.NORMAL;

  /**
   * Returns a preconfigured WorldModfierConfigBuilder Based on the preset for you to configure Use this if you want to change additional Settings otherwise use:
   * RegisteredConfiguredWorldModifier.getPresetConfiguredWorldModifier(WorldModifierPresets preset)
   *
   * @param preset The preset Type
   * @return returns WorldModifierConfig
   */
  public static WorldModifierConfigBuilder getPresetWorldModifierConfig(@NonNull WorldModifierPresets preset) {
    switch (preset) {
      case OVERWORLD, SINGLE_BIOME -> {
        return WorldModifierConfig.builder();
      }
      case NETHER -> {
        return WorldModifierConfig.builder()
                                  .environment(World.Environment.NETHER);
      }
      case END -> {
        return WorldModifierConfig.builder()
                                  .environment(World.Environment.THE_END);
      }
      case SUPERFLAT_DEFAULT, SUPERFLAT_TUNNELERS_DREAM, SUPERFLAT_WATER_WORLD,
           SUPERFLAT_OVERWORLD, SUPERFLAT_SNOWY_KINGDOM, SUPERFLAT_BOTTOMLESS_PIT, SUPERFLAT_DESERT,
           SUPERFLAT_REDSTONE_READY, SUPERFLAT_THE_VOID -> {
        return WorldModifierConfig.builder()
                                  .worldType(WorldType.FLAT)
                                  .generatorSettings(getDefaultGeneratorSettings(preset).build());
      }
      case LARGE_BIOMES -> {
        return WorldModifierConfig.builder()
                                  .worldType(WorldType.LARGE_BIOMES);
      }
      case AMPLIFIED -> {
        return WorldModifierConfig.builder()
                                  .worldType(WorldType.AMPLIFIED);
      }
      case SKY_BLOCK_OVERWORLD -> {
        // TODO: create SkyBlockOverworld ChunkGenerator
        return WorldModifierConfig.builder();
      }
      case SKY_BLOCK_NETHER -> {
        // TODO: create SkyBlockNether ChunkGenerator
        return WorldModifierConfig.builder();
      }
      case SKY_BLOCK_END -> {
        // TODO: create SkyBlockEnd ChunkGenerator
        return WorldModifierConfig.builder();
      }
      case TRUE_VOID -> {
        // TODO: create TrueVoid ChunkGenerator
        return WorldModifierConfig.builder();
      }
      case TRUE_TRUE_VOID -> {
        // TODO: create TrueTrueVoid ChunkGenerator
        return WorldModifierConfig.builder();
      }
      default -> throw new IllegalArgumentException("Unknown Preset " + preset.name());
    }
  }

  /**
   * Returns the default generator settings string for the specified superflat preset. These correspond to Minecraft's classic superflat presets.
   *
   * @param preset The superflat preset enum
   * @return JSON string with default generator settings
   */
  //[ChatGPT]\\
  public static FlatGeneratorSettings.FlatGeneratorSettingsBuilder getDefaultGeneratorSettings(@NonNull WorldModifierPresets preset) {
    return switch (preset) {
      case SUPERFLAT_DEFAULT -> FlatGeneratorSettings.builder()
                                                     .biome(Biome.PLAINS)
                                                     .features(false)
                                                     .lakes(false)
                                                     .addBlockLayer(BlockType.BEDROCK, 1)
                                                     .addBlockLayer(BlockType.DIRT, 2)
                                                     .addBlockLayer(BlockType.GRASS_BLOCK, 1)
                                                     .addStructure("minecraft:villages");

      case SUPERFLAT_TUNNELERS_DREAM -> FlatGeneratorSettings.builder()
                                                             .biome(Biome.WINDSWEPT_HILLS)
                                                             .features(true)
                                                             .lakes(false)
                                                             .addBlockLayer(BlockType.BEDROCK, 1)
                                                             .addBlockLayer(BlockType.STONE, 230)
                                                             .addBlockLayer(BlockType.DIRT, 5)
                                                             .addBlockLayer(BlockType.GRASS_BLOCK, 1)
                                                             .addStructure("minecraft:mineshafts")
                                                             .addStructure("minecraft:strongholds");

      case SUPERFLAT_WATER_WORLD -> FlatGeneratorSettings.builder()
                                                         .biome(Biome.DEEP_OCEAN)
                                                         .features(false)
                                                         .lakes(false)
                                                         .addBlockLayer(BlockType.BEDROCK, 1)
                                                         .addBlockLayer(BlockType.DEEPSLATE, 64)
                                                         .addBlockLayer(BlockType.STONE, 5)
                                                         .addBlockLayer(BlockType.DIRT, 5)
                                                         .addBlockLayer(BlockType.GRAVEL, 5)
                                                         .addBlockLayer(BlockType.WATER, 90)
                                                         .addStructure("minecraft:ocean_ruins")
                                                         .addStructure("minecraft:shipwrecks")
                                                         .addStructure("minecraft:ocean_monuments");

      case SUPERFLAT_OVERWORLD -> FlatGeneratorSettings.builder()
                                                       .biome(Biome.PLAINS)
                                                       .features(true)
                                                       .lakes(true)
                                                       .addBlockLayer(BlockType.BEDROCK, 1)
                                                       .addBlockLayer(BlockType.STONE, 59)
                                                       .addBlockLayer(BlockType.DIRT, 3)
                                                       .addBlockLayer(BlockType.GRASS_BLOCK, 1)
                                                       .addStructure("minecraft:villages")
                                                       .addStructure("minecraft:mineshafts")
                                                       .addStructure("minecraft:pillager_outposts")
                                                       .addStructure("minecraft:ruined_portals")
                                                       .addStructure("minecraft:strongholds");

      case SUPERFLAT_SNOWY_KINGDOM -> FlatGeneratorSettings.builder()
                                                           .biome(Biome.SNOWY_PLAINS)
                                                           .features(false)
                                                           .lakes(false)
                                                           .addBlockLayer(BlockType.BEDROCK, 1)
                                                           .addBlockLayer(BlockType.STONE, 59)
                                                           .addBlockLayer(BlockType.DIRT, 3)
                                                           .addBlockLayer(BlockType.GRASS_BLOCK, 1)
                                                           .addBlockLayer(BlockType.SNOW, 1)
                                                           .addStructure("minecraft:villages")
                                                           .addStructure("minecraft:igloos");

      case SUPERFLAT_BOTTOMLESS_PIT -> FlatGeneratorSettings.builder()
                                                            .biome(Biome.PLAINS)
                                                            .features(false)
                                                            .lakes(false)
                                                            .addBlockLayer(BlockType.COBBLESTONE, 2)
                                                            .addBlockLayer(BlockType.DIRT, 3)
                                                            .addBlockLayer(BlockType.GRASS_BLOCK, 1)
                                                            .addStructure("minecraft:villages");

      case SUPERFLAT_DESERT -> FlatGeneratorSettings.builder()
                                                    .biome(Biome.DESERT)
                                                    .features(true)
                                                    .lakes(false)
                                                    .addBlockLayer(BlockType.BEDROCK, 1)
                                                    .addBlockLayer(BlockType.STONE, 3)
                                                    .addBlockLayer(BlockType.SANDSTONE, 52)
                                                    .addBlockLayer(BlockType.SAND, 8)
                                                    .addStructure("minecraft:villages")
                                                    .addStructure("minecraft:desert_pyramids")
                                                    .addStructure("minecraft:mineshafts")
                                                    .addStructure("minecraft:strongholds");

      case SUPERFLAT_REDSTONE_READY -> FlatGeneratorSettings.builder()
                                                            .biome(Biome.DESERT)
                                                            .features(false)
                                                            .lakes(false)
                                                            .addBlockLayer(BlockType.BEDROCK, 1)
                                                            .addBlockLayer(BlockType.STONE, 3)
                                                            .addBlockLayer(BlockType.SANDSTONE, 116);

      case SUPERFLAT_THE_VOID -> FlatGeneratorSettings.builder()
                                                      .biome(Biome.THE_VOID)
                                                      .features(true)
                                                      .lakes(false)
                                                      .addBlockLayer(BlockType.AIR, 1);

      default -> FlatGeneratorSettings.builder();
    };
  }

  public static WorldModifierConfigBuilder fromPlugin(@NonNull Plugin plugin) {
    return fromPlugin(plugin, ChallengeAPI.DEFAULT_MATERIAL);
  }

  public static @Nullable WorldModifierConfigBuilder fromPlugin(@NonNull String name) {
    return fromPlugin(name, ChallengeAPI.DEFAULT_MATERIAL, "world", null);
  }

  public static @Nullable WorldModifierConfigBuilder fromPlugin(@NonNull String name, Material material) {
    return fromPlugin(name, material, "world", null);
  }

  public static @Nullable WorldModifierConfigBuilder fromPlugin(@NonNull String name, Material material, @NonNull String worldName, String generatorId) {
    Plugin plugin = Bukkit.getPluginManager().getPlugin(name);
    if (plugin == null) {
      getLogger().warn("Plugin '{}' not found! Could not register!", name);
      return null;
    }
    return fromPlugin(plugin, material, worldName, generatorId);
  }

  public static @Nullable WorldModifierConfigBuilder fromPlugin(@NonNull Plugin plugin, Material material) {
    return fromPlugin(plugin, material, "world", null);
  }

  public static @Nullable WorldModifierConfigBuilder fromPlugin(@NonNull Plugin plugin, Material material, @NonNull String worldName, String generatorId) {
    try {
      plugin.getClass().getMethod("getDefaultWorldGenerator", String.class, String.class);
    } catch (NoSuchMethodException e) {
      getLogger().warn("Plugin '{}' does not seem to define a default world generator!", plugin.getName());
      return null;
    }
    return WorldModifierConfig.builder().chunkGenerator(plugin.getDefaultWorldGenerator(worldName, generatorId));
  }

  public static class WorldModifierConfigBuilder {
    public @NonNull WorldModifierConfigBuilder addGamerule(GameRule<?> rule, Object value) {
      this.gameRules$value.put(rule, value);
      return this;
    }
  }
}
