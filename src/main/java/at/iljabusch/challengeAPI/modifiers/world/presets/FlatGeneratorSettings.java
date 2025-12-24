package at.iljabusch.challengeAPI.modifiers.world.presets;

import at.iljabusch.challengeAPI.modifiers.world.presets.generator_settings.schemas.FlatWorldGeneratorSettingsSchema;
import at.iljabusch.challengeAPI.modifiers.world.presets.generator_settings.schemas.LayerInfoSchema;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import lombok.Builder;
import lombok.Value;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.block.Biome;
import org.bukkit.block.BlockType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to generate the rather obscure json of the .generatorSettings option for FlatWorlds <br>
 *
 * <p><b>Usage:</b></p>
 * <pre>{@code
 * // creates the default flat world
 * FlatGeneratorSettings.builder()
 *      .lakes(true)
 *      .decoration(false)
 *      .addStructure("minecraft:village")
 *      .addStructure("minecraft:stronghold")
 *      .biome(Biome.PLAINS)
 *      .addBlockLayer(BlockType.BEDROCK, 1)
 *      .addBlockLayer(BlockType.DIRT, 1)
 *      .addBlockLayer(BlockType.GRASS_BLOCK, 1)
 *      .build();
 * }</pre>
 */
@Value
@Builder(builderClassName = "FlatGeneratorSettingsBuilder")
public class FlatGeneratorSettings {
  boolean lakes;
  boolean features;
  Biome biome;
  @Builder.Default
  List<LayerInfo> layers = new ArrayList<>();
  /**
  The Minecraft name of the structure to be generated in your flat world
   */
  @Builder.Default
  List<String> structuresToGenerate = new ArrayList<>();

  public String getJson(){
    FlatWorldGeneratorSettingsSchema schema = new FlatWorldGeneratorSettingsSchema();

    Registry<Biome> registry = RegistryAccess.registryAccess().getRegistry(RegistryKey.BIOME);
    NamespacedKey key = registry.getKey(biome);
    schema.setBiome(key == null? "minecraft:plains" : key.toString());

    schema.setFeatures(features);
    schema.setLakes(lakes);

    layers.forEach((layer) -> {
                     schema.getLayers().add(new LayerInfoSchema(
                        layer.block.getKey().toString(),
                        layer.layerHeight
                     ));
                   }
    );

    //TODO: maybe support setting costum values for the placement field by the user but its so obscure and goofy they might just want to make a custom ChunkGenerator setting the defaults for now
    //TODO: system to get default placements
   if(structuresToGenerate.isEmpty()){}

    return "";
  }

  public static class FlatGeneratorSettingsBuilder  {
    /**
     * Unfortunately Bukkit doesn't have an Enum or Type that lists all Structures and recommends them to you anymore since they are taken from the Registry now.<br>
     *
     * <p>To find the Id of you Structure look at the recommended names when running /locate structure #minecraft:... </p>
     *
     * @param structureId
     * @return
     */
    public @NotNull FlatGeneratorSettingsBuilder addStructure(String structureId) {
      this.structuresToGenerate$value.add(structureId);
      return this;
    }

    public @NotNull FlatGeneratorSettingsBuilder addBlockLayer(BlockType blockType, int layerHeight) {
      this.layers$value.add(new LayerInfo(blockType, layerHeight));
      return this;
    }

  }

  private record LayerInfo(BlockType block, int layerHeight) {}
}
