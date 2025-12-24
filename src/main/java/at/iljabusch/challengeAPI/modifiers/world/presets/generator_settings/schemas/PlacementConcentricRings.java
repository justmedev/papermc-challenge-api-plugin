package at.iljabusch.challengeAPI.modifiers.world.presets.generator_settings.schemas;

import lombok.Data;

@Data
public class PlacementConcentricRings extends Placement{
  private int distance;
  private int spread;
  private int count;
}
