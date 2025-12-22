package at.iljabusch.challengeAPI;

import at.iljabusch.challengeAPI.challenges.ChallengeManager;
import at.iljabusch.challengeAPI.commands.ChallengeCmd;
import at.iljabusch.challengeAPI.menus.createchallenge.ChallengeCreationMenuListener;
import at.iljabusch.challengeAPI.modifiers.RegisteredModifier;
import at.iljabusch.challengeAPI.modifiers.sharedhealth.SharedHealthModifier;
import at.iljabusch.challengeAPI.modifiers.stopwatch.StopwatchModifier;
import at.iljabusch.challengeAPI.modifiers.world.presets.RegisteredConfiguredWorldModifier;
import at.iljabusch.challengeAPI.modifiers.world.presets.WorldModifierPresets;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class ChallengeAPI extends JavaPlugin implements Listener {

  public static final Material DEFAULT_MATERIAL = Material.DIRT;
  private final ChallengeManager state = ChallengeManager.getInstance();

  @Override
  public void onEnable() {
    getLogger().info("ChallengeAPI Plugin is starting ...");
    Bukkit.getPluginManager().registerEvents(this, this);
    Bukkit.getPluginManager().registerEvents(new ChallengeCreationMenuListener(), this);

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

    getLifecycleManager().registerEventHandler(
        LifecycleEvents.COMMANDS, commands -> {
          commands.registrar().register(ChallengeCmd.challengeCommand());
        }
    );
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    getLogger().info("%s joined!".formatted(event.getPlayer().getUniqueId()));
    var player = state.getPlayersInChallenges().get(event.getPlayer().getUniqueId());
    if (player == null) {
      return;
    }

    player.setPlayer(event.getPlayer());
    player.getChallenge().join(player.getPlayer());
  }

  @EventHandler
  public void onPlayerLeave(PlayerQuitEvent event) {
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
