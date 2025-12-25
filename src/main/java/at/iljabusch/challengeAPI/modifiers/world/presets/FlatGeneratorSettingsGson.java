package at.iljabusch.challengeAPI.modifiers.world.presets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class FlatGeneratorSettingsGson {
  private static final Gson GSON = createGson();

  private static Gson createGson() {
    GsonBuilder builder = new GsonBuilder();

    return builder.create();
  }

  public static Gson get() {
   return GSON;
  }
}
