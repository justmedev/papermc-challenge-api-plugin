package at.iljabusch.challengeAPI.modifiers.world.presets.generator_settings.schemas;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class PlacementRandomSpread extends Placement {
  private int spacing;
  private int separation;
  @SerializedName("spread_type")
  private String spreadType;
}
