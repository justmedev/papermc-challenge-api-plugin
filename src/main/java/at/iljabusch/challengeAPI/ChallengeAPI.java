package at.iljabusch.challengeAPI;

import at.iljabusch.challengeAPI.challenges.ChallengeManager;
import at.iljabusch.challengeAPI.commands.ChallengeCmd;
import at.iljabusch.challengeAPI.menus.createchallenge.ChallengeCreationMenuListener;
import at.iljabusch.challengeAPI.modifiers.RegisteredModifier;
import at.iljabusch.challengeAPI.modifiers.sharedhealth.SharedHealthModifier;
import at.iljabusch.challengeAPI.modifiers.stopwatch.StopwatchModifier;
import at.iljabusch.challengeAPI.modifiers.world.presets.*;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.block.Biome;
import org.bukkit.block.BlockType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NonNull;

public final class ChallengeAPI extends JavaPlugin implements Listener {

  public static final Material DEFAULT_MATERIAL = Material.DIRT;
  private final ChallengeManager state = ChallengeManager.getInstance();

  @Override
  public void onEnable() {
    getLogger().info("ChallengeAPI Plugin is starting ...");
    Bukkit.getPluginManager().registerEvents(this, this);
    Bukkit.getPluginManager().registerEvents(new ChallengeCreationMenuListener(), this);


    FlatGeneratorSettings settings =  FlatGeneratorSettings.builder()
                                                           .features(true)
                                                           .lakes(true)
                                                           .biome(Biome.DARK_FOREST)
                                                           .addStructure("minecraft:woodland_mansions")
                                                           .addStructure("minecraft:strongholds")
                                                           .addBlockLayer(BlockType.BEDROCK, 1)
                                                           .addBlockLayer(BlockType.GREEN_TERRACOTTA, 200)
                                                           .build();

    String json = settings.getJson();
    WorldCreator creator = new WorldCreator("test");
    creator.type(WorldType.FLAT);
    creator.generatorSettings(json);
    creator.createWorld();
    //for (WorldModifierPresets preset : WorldModifierPresets.values()) {
    //  ChallengeManager.getInstance().registerModifier(
    //      RegisteredConfiguredWorldModifier.getPresetConfiguredWorldModifier(preset)
    //  );
    //}

    ChallengeManager.getInstance().registerModifier(
        new RegisteredModifier(
            "Shared-Health",
            "Ilja Busch",
            Material.RED_TULIP,
            SharedHealthModifier.class
        )
    );



    ChallengeManager.getInstance().registerModifier(
        new RegisteredModifier(
            "Stopwatch",
            "Ilja Busch",
            Material.CLOCK,
            StopwatchModifier.class
        )
    );

    ChallengeManager.getInstance().registerModifier(
        RegisteredConfiguredWorldModifier.getPresetConfiguredWorldModifier(WorldModifierPresets.SUPERFLAT_DESERT)
    );

    ChallengeManager.getInstance().registerModifier(
        new RegisteredConfiguredWorldModifier(
            "skibedi" ,
            "dop",
            Material.AZALEA,
            WorldModifierConfig.builder()
                               .worldType(WorldType.FLAT)
                               .generatorSettings(
                                   WorldModifierConfig.getDefaultGeneratorSettings(WorldModifierPresets.SUPERFLAT_WATER_WORLD)
                                       .addStructure("minecraft:strongholds")
                                       .build()
                               ).build()
        ));

    ChallengeManager.getInstance().registerModifier(
        new RegisteredConfiguredWorldModifier(
            "skibedi" ,
            "dop",
            Material.SLIME_BLOCK,
            WorldModifierConfig.builder()
                .worldType(WorldType.FLAT)
                .generatorSettings(
                    FlatGeneratorSettings.builder()
                        .features(true)
                        .lakes(true)
                        .biome(Biome.BADLANDS)
                        .addStructure("woodland_mansions")
                        .addStructure("minecraft:strongholds")
                        .addBlockLayer(BlockType.BEDROCK, 200)
                        .addBlockLayer(BlockType.GREEN_TERRACOTTA, 200)
                               .build()
                ).build()

        )
    );

    getLifecycleManager().registerEventHandler(
        LifecycleEvents.COMMANDS, commands -> {
          commands.registrar().register(ChallengeCmd.challengeCommand());
        }
    );
  }

  @EventHandler
  public void onPlayerJoin(@NonNull PlayerJoinEvent event) {
    getLogger().info("%s joined!".formatted(event.getPlayer().getUniqueId()));
    var player = state.getPlayersInChallenges().get(event.getPlayer().getUniqueId());
    if (player == null) {
      return;
    }

    player.setPlayer(event.getPlayer());
    player.getChallenge().join(player.getPlayer());
  }

  @EventHandler
  public void onPlayerLeave(@NonNull PlayerQuitEvent event) {
    var player = state.getPlayersInChallenges().get(event.getPlayer().getUniqueId());
    if (player == null) {
      return;
    }

    player.getChallenge().leaveServer(player.getPlayer());
  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic
  }
}
