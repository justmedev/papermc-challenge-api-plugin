package at.iljabusch.challengeAPI.modifiers.world.presets;

import at.iljabusch.challengeAPI.modifiers.world.presets.generator_settings.schemas.Placement;
import at.iljabusch.challengeAPI.modifiers.world.presets.generator_settings.schemas.PlacementAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class FlatGeneratorSettingsGson {
  private static final Gson GSON = createGson();

  private static Gson createGson() {
    GsonBuilder builder = new GsonBuilder()
        .registerTypeAdapter(Placement.class, new PlacementAdapter());

    return builder.create();
  }

  public static Gson get() {
   return GSON;
  }
}
