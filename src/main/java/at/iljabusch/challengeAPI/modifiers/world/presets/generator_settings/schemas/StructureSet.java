package at.iljabusch.challengeAPI.modifiers.world.presets.generator_settings.schemas;
import lombok.Data;
import java.util.List;


@Data
public class StructureSet {
  private Placement placement;
  private List<Structure> structures;
}
