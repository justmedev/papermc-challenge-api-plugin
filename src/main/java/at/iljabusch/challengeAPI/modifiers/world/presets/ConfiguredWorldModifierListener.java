package at.iljabusch.challengeAPI.modifiers.world.presets;

import at.iljabusch.challengeAPI.challenges.events.ChallengeCreatedEvent;
import at.iljabusch.challengeAPI.challenges.events.ChallengeWorldCreatedEvent;
import at.iljabusch.challengeAPI.modifiers.world.presets.chunk_generators.SingleBiomeProvider;
import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.atomic.AtomicReference;

public class ConfiguredWorldModifierListener implements Listener {
  private final WorldModifierConfig config;
  private AtomicReference<WorldCreator> worldCreator;

  public ConfiguredWorldModifierListener(WorldModifierConfig config) {
    this.config = config;
  }

  @EventHandler
  public void onChallengeCreated(@NonNull ChallengeCreatedEvent event) {
    WorldCreator configuredWorldCreator = new WorldCreator("temp");
    configuredWorldCreator.type(config.worldType);
    if (config.singleBiomeWorldBiome != null) {
      configuredWorldCreator.biomeProvider(new SingleBiomeProvider(config.singleBiomeWorldBiome));
    }

    if (config.worldType == WorldType.FLAT || config.chunkGenerator != null) {
      configuredWorldCreator.generatorSettings(config.generatorSettingsString);
    }
    configuredWorldCreator.seed(config.seed);
    configuredWorldCreator.bonusChest(config.spawnBonusChest);

    configuredWorldCreator.environment(config.environment);
    configuredWorldCreator.generateStructures(config.generateStructures);
    worldCreator = event.getChallenge().setWorldCreator(configuredWorldCreator);

  }

  @EventHandler
  public void onChallengeWorldCreated(@NonNull ChallengeWorldCreatedEvent event) {
    if (worldCreator == null) return;
    World world = event.getWorld();

    if (config.spawnLocation != null) {
      world.setSpawnLocation(new Location(
          world,
          config.spawnLocation.getX(),
          config.spawnLocation.getY(),
          config.spawnLocation.getZ()
      ));
    }

    if (config.clearWeatherDurationTicks > 0) {
      world.setClearWeatherDuration(config.clearWeatherDurationTicks);
    }

    world.setSpawnFlags(config.allowMonsterSpawning, config.allowAnimalSpawning);

    if (config.difficulty != null) {
      world.setDifficulty(config.difficulty);
    }

    world.getPlayers().forEach(player -> {
      player.setGameMode(config.gameMode);
    });

    if (config.gameRules.size() > 0) {
      for (GameRule rule : config.gameRules.keySet()) {
        world.setGameRule(rule, config.gameRules.get(rule));
      }

    }

  }

}
