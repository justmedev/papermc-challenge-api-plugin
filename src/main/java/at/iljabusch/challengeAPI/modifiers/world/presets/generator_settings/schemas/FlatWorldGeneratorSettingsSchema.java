package at.iljabusch.challengeAPI.modifiers.world.presets.generator_settings.schemas;

import lombok.Data;

import java.util.List;

@Data
public class FlatWorldGeneratorSettingsSchema {
  private String biome;
  private List<LayerInfoSchema> layers;
  private boolean features;
  private boolean lakes;
}
