package at.iljabusch.challengeAPI.modifiers.world.presets;

import at.iljabusch.challengeAPI.modifiers.world.presets.generator_settings.schemas.*;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
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
 *      .addStructure("minecraft:villages")
 *      .addStructure("minecraft:strongholds")
 *      .biome(Biome.PLAINS)
 *      .addBlockLayer(BlockType.BEDROCK, 1)
 *      .addBlockLayer(BlockType.DIRT, 1)
 *      .addBlockLayer(BlockType.GRASS_BLOCK, 1)
 *      .build();
 * }</pre>
 */
@Getter
@Value
@Builder(builderClassName = "FlatGeneratorSettingsBuilder")
public class FlatGeneratorSettings {
  @Builder.Default
  boolean lakes = false;
  @Builder.Default
  boolean features = false;
  @Builder.Default
  Biome biome = Biome.PLAINS;
  @Singular("addBlockLayerInfo")
  List<LayerInfo> layers;
  /**
  The Minecraft name of the structure to be generated in your flat world. This is supposed to be a structure set not an individual structure! <br>

  <a href="https://minecraft.wiki/w/Structure_set#Default_structure_sets">A list of all Vanilla StructureSets</a>
   put a "minecraft:" in front of names you find here
   */
  @Singular("addStructure")
  List<String> structuresToGenerate;


  public String getJson(){
    FlatWorldGeneratorSettingsSchema schema = new FlatWorldGeneratorSettingsSchema();

    Registry<Biome> registry = RegistryAccess.registryAccess().getRegistry(RegistryKey.BIOME);
    NamespacedKey key = registry.getKey(biome);
    schema.setBiome(key == null? "minecraft:plains" : key.toString());

    schema.setFeatures(features);
    schema.setLakes(lakes);

    layers.forEach((layer) -> schema.getLayers().add(new LayerInfoSchema(
       layer.block.getKey().toString(),
       layer.layerHeight
    ))
    );

    structuresToGenerate.forEach((structure) -> {
      schema.getStructures().add(structure);
    });

    String json =  FlatGeneratorSettingsGson.get().toJson(schema);
    return json;
  }
  public static class FlatGeneratorSettingsBuilder {
    public FlatGeneratorSettingsBuilder addBlockLayer(BlockType type, int height) {
      return this.addBlockLayerInfo(new LayerInfo(type, height));
    }
  }

  private record LayerInfo(BlockType block, int layerHeight) {}
}
