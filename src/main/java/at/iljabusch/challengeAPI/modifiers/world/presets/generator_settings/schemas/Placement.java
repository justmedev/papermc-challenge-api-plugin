package at.iljabusch.challengeAPI.modifiers.world.presets.generator_settings.schemas;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public abstract class Placement {
  private int salt;
  @SerializedName("locate_offset")
  private int[] locateOffset;
  private float frequency;
  private int separation;
}
