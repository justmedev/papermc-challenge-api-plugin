package at.iljabusch.challengeAPI.modifiers.world.presets.generator_settings.schemas;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LayerInfoSchema {
  public String block;
  public int height;
}
