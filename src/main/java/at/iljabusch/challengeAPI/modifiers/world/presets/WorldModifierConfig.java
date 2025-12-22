package at.iljabusch.challengeAPI.modifiers.world.presets;

import lombok.Builder;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.Vector;

import java.util.HashMap;

@Builder
public class WorldModifierConfig {

  @Builder.Default
  protected HashMap<GameRule, Object> gameRules = new HashMap<>();
  @Builder.Default
  protected Biome singleBiomeWorldBiome = null;
  @Builder.Default
  protected boolean generateStructures = true;
  @Builder.Default
  protected boolean starterChest = false;
  @Builder.Default
  protected long seed = 0;
  protected ChunkGenerator chunkGenerator;
  @Builder.Default
  protected GameMode gameMode = GameMode.SURVIVAL;
  @Builder.Default
  protected World.Environment environment = World.Environment.NORMAL;
  protected Difficulty difficulty;
  @Builder.Default
  protected Boolean allowMonsterSpawning = true;
  @Builder.Default
  protected Boolean allowAnimalSpawning = true;
  @Builder.Default
  protected int clearWeatherDurationTicks = 0;
  protected Vector spawnLocation;
  protected String generatorSettingsString;
  @Builder.Default
  WorldType worldType = WorldType.NORMAL;

  /**
   * Returns a preconfigured WorldModfierConfigBuilder Based on the preset for you to configure Use this if you want to change additional Settings otherwise use:
   * RegisteredConfiguredWorldModifier.getPresetConfiguredWorldModifier(WorldModifierPresets preset)
   *
   * @param preset The preset Type
   * @return returns WorldModifierConfig
   */
  public static WorldModifierConfigBuilder getPresetWorldModifierConfig(WorldModifierPresets preset) {
    switch (preset) {
      case OVERWORLD -> {
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
                                  .generatorSettingsString(getDefaultGeneratorSettings(preset));
      }
      case LARGE_BIOMES -> {
        return WorldModifierConfig.builder()
                                  .worldType(WorldType.LARGE_BIOMES);
      }
      case AMPLIFIED -> {
        return WorldModifierConfig.builder()
                                  .worldType(WorldType.AMPLIFIED);
      }
      case SINGLE_BIOME -> {
        // TODO: create SingleBiome ChunkGenerator
        return WorldModifierConfig.builder();
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
  public static String getDefaultGeneratorSettings(WorldModifierPresets preset) {
    return switch (preset) {
      case SUPERFLAT_DEFAULT -> """
          {
            "biome": "minecraft:plains",
            "layers": [
              {
                "block": "minecraft:bedrock",
                "height": 1
              },
              {
                "block": "minecraft:dirt",
                "height": 3
              },
              {
                "block": "minecraft:grass_block",
                "height": 1
              }
            ],
            "structures": {
              "village": true
            },
            "features": true
          }
          """;

      case SUPERFLAT_TUNNELERS_DREAM -> """
          {
            "biome": "minecraft:mountain_edge",
            "layers": [
              {
                "block": "minecraft:bedrock",
                "height": 1
              },
              {
                "block": "minecraft:stone",
                "height": 5
              },
              {
                "block": "minecraft:dirt",
                "height": 3
              },
              {
                "block": "minecraft:grass_block",
                "height": 1
              }
            ],
            "structures": {
              "village": true,
              "mineshaft": {
                "chance": 1.0
              }
            },
            "features": true
          }
          """;

      case SUPERFLAT_WATER_WORLD -> """
          {
            "biome": "minecraft:plains",
            "layers": [
              {
                "block": "minecraft:bedrock",
                "height": 1
              },
              {
                "block": "minecraft:water",
                "height": 90
              }
            ],
            "structures": {},
            "features": false
          }
          """;

      case SUPERFLAT_OVERWORLD -> """
          {
            "biome": "minecraft:plains",
            "layers": [
              {
                "block": "minecraft:air",
                "height": 1
              },
              {
                "block": "minecraft:bedrock",
                "height": 1
              }
            ],
            "structures": {
              "village": true,
              "stronghold": true,
              "mineshaft": true,
              "dungeon": true
            },
            "features": true
          }
          """;

      case SUPERFLAT_SNOWY_KINGDOM -> """
          {
            "biome": "minecraft:snowy_plains",
            "layers": [
              {
                "block": "minecraft:bedrock",
                "height": 1
              },
              {
                "block": "minecraft:snow_block",
                "height": 3
              },
              {
                "block": "minecraft:snow",
                "height": 1
              }
            ],
            "structures": {
              "village": true
            },
            "features": true
          }
          """;

      case SUPERFLAT_BOTTOMLESS_PIT -> """
          {
            "biome": "minecraft:plains",
            "layers": [
              {
                "block": "minecraft:bedrock",
                "height": 1
              },
              {
                "block": "minecraft:air",
                "height": 64
              }
            ],
            "structures": {},
            "features": false
          }
          """;

      case SUPERFLAT_DESERT -> """
          {
            "biome": "minecraft:desert",
            "layers": [
              {
                "block": "minecraft:bedrock",
                "height": 1
              },
              {
                "block": "minecraft:sand",
                "height": 3
              },
              {
                "block": "minecraft:sandstone",
                "height": 1
              }
            ],
            "structures": {
              "village": true,
              "desert_pyramid": true
            },
            "features": true
          }
          """;

      case SUPERFLAT_REDSTONE_READY -> """
          {
            "biome": "minecraft:bedrock",
            "layers": [
              {
                "block": "minecraft:bedrock",
                "height": 1
              },
              {
                "block": "minecraft:bedrock",
                "height": 1
              }
            ],
            "structures": {},
            "features": false
          }
          """;

      case SUPERFLAT_THE_VOID -> """
          {
            "biome": "minecraft:the_void",
            "layers": [],
            "structures": {},
            "features": false
          }
          """;

      default -> "{}";
    };
  }

  public <T> WorldModifierConfig addGamerule(GameRule<T> rule, T value) {
    gameRules.put(rule, value);
    return this;
  }
}
