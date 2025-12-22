package at.iljabusch.challengeAPI.modifiers.world.presets;

import org.bukkit.Material;
import org.jspecify.annotations.NonNull;

public enum WorldModifierPresets {
  OVERWORLD,
  NETHER,
  END,
  SUPERFLAT_DEFAULT,
  SUPERFLAT_TUNNELERS_DREAM,
  SUPERFLAT_WATER_WORLD,
  SUPERFLAT_OVERWORLD,
  SUPERFLAT_SNOWY_KINGDOM,
  SUPERFLAT_BOTTOMLESS_PIT,
  SUPERFLAT_DESERT,
  SUPERFLAT_REDSTONE_READY,
  /**
   * Superflat Void peset
   */
  SUPERFLAT_THE_VOID,
  /**
   * World with actually Nothing. Keeps Biomes. Modifiers will need to make something to actually stand on f.E. OneBlock.
   */
  TRUE_VOID,
  /**
   * World with actually Nothing. No Biomes.  Modifiers will need to make something to actually stand on f.E. OneBlock.
   */
  TRUE_TRUE_VOID,
  LARGE_BIOMES,
  AMPLIFIED,
  SINGLE_BIOME,
  SKY_BLOCK_OVERWORLD,
  SKY_BLOCK_NETHER,
  SKY_BLOCK_END;

  public @NonNull String getDisplayName() {
    return switch (this) {
      case OVERWORLD -> "Overworld";
      case NETHER -> "Nether";
      case END -> "The End";
      case SUPERFLAT_DEFAULT -> "Superflat (Default)";
      case SUPERFLAT_TUNNELERS_DREAM -> "Superflat (Tunneler's Dream)";
      case SUPERFLAT_WATER_WORLD -> "Superflat (Water World)";
      case SUPERFLAT_OVERWORLD -> "Superflat (Overworld)";
      case SUPERFLAT_SNOWY_KINGDOM -> "Superflat (Snowy Kingdom)";
      case SUPERFLAT_BOTTOMLESS_PIT -> "Superflat (Bottomless Pit)";
      case SUPERFLAT_DESERT -> "Superflat (Desert)";
      case SUPERFLAT_REDSTONE_READY -> "Superflat (Redstone Ready)";
      case SUPERFLAT_THE_VOID -> "Superflat (The Void)";
      case TRUE_VOID -> "True Void (With Biomes)";
      case TRUE_TRUE_VOID -> "True True Void (No Biomes)";
      case LARGE_BIOMES -> "Large Biomes";
      case AMPLIFIED -> "Amplified";
      case SINGLE_BIOME -> "Single Biome";
      case SKY_BLOCK_OVERWORLD -> "SkyBlock (Overworld)";
      case SKY_BLOCK_NETHER -> "SkyBlock (Nether)";
      case SKY_BLOCK_END -> "SkyBlock (End)";
    };
  }

  public @NonNull Material getPresetDefaultMaterial() {
    return switch (this) {
      case OVERWORLD -> Material.GRASS_BLOCK;
      case NETHER -> Material.NETHERRACK;
      case END -> Material.END_STONE;
      case SUPERFLAT_DEFAULT -> Material.GRASS_BLOCK;
      case SUPERFLAT_TUNNELERS_DREAM -> Material.STONE;
      case SUPERFLAT_WATER_WORLD -> Material.WATER_BUCKET;
      case SUPERFLAT_OVERWORLD -> Material.DIRT;
      case SUPERFLAT_SNOWY_KINGDOM -> Material.SNOW_BLOCK;
      case SUPERFLAT_BOTTOMLESS_PIT -> Material.BEDROCK;
      case SUPERFLAT_DESERT -> Material.SAND;
      case SUPERFLAT_REDSTONE_READY -> Material.REDSTONE_BLOCK;
      case SUPERFLAT_THE_VOID -> Material.BARRIER;
      case TRUE_VOID -> Material.STRUCTURE_VOID;
      case TRUE_TRUE_VOID -> Material.AIR;
      case LARGE_BIOMES -> Material.MAP;
      case AMPLIFIED -> Material.POINTED_DRIPSTONE;
      case SINGLE_BIOME -> Material.DIRT_PATH;
      case SKY_BLOCK_OVERWORLD -> Material.OAK_SAPLING;
      case SKY_BLOCK_NETHER -> Material.CRIMSON_FUNGUS;
      case SKY_BLOCK_END -> Material.CHORUS_FLOWER;
    };
  }
}
