package at.iljabusch.challengeAPI.modifiers.world.presets.generator_settings.schemas;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FlatWorldGeneratorSettingsSchema {
  private String biome;
  private List<LayerInfoSchema> layers = new ArrayList<>();
  @SerializedName("structure_overrides")
  private List<String> structures = new ArrayList<>();
  private boolean features;
  private boolean lakes;
}
