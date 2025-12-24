package at.iljabusch.challengeAPI.modifiers.world.presets.generator_settings.schemas;

import com.google.gson.*;

import javax.crypto.spec.PSource;
import java.lang.reflect.Type;

public class PlacementAdapter implements JsonSerializer<Placement> {

  @Override
  public JsonElement serialize(Placement placement, Type type, JsonSerializationContext jsonSerializationContext) {
    JsonElement jsonElement = null;
    if(placement instanceof PlacementConcentricRings)
    {
      jsonElement = jsonSerializationContext.serialize(placement, PlacementConcentricRings.class);
      jsonElement.getAsJsonObject().addProperty("type", "minecraft:concentric_rings");
    }else if(placement instanceof  PlacementRandomSpread){
      jsonElement = jsonSerializationContext.serialize(placement, PlacementRandomSpread.class);
      jsonElement.getAsJsonObject().addProperty("type", "minecraft:random_spread");
    }

    return jsonElement;
  }
}
