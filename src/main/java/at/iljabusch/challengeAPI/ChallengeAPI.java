package at.iljabusch.challengeAPI;

import at.iljabusch.challengeAPI.commands.ChallengeCmd;
import at.iljabusch.challengeAPI.commands.ChallengeMenuCmd;
import at.iljabusch.challengeAPI.modifiers.RegisteredModifier;
import at.iljabusch.challengeAPI.modifiers.sharedhealth.SharedHealthModifier;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class ChallengeAPI extends JavaPlugin implements Listener {
  private ChallengeManager state = ChallengeManager.getInstance();

  @Override
  public void onEnable() {
    getLogger().info("ChallengeAPI Plugin is starting ...");
    Bukkit.getPluginManager().registerEvents(this, this);

    ChallengeManager.getInstance().registerModifier(
        new RegisteredModifier(
            "Shared-Health",
            "Ilja Busch",
            Material.RED_TULIP,
            SharedHealthModifier.class
        )
    );

    getCommand("challenge").setExecutor(new ChallengeCmd());
    getCommand("challenge-menu").setExecutor(new ChallengeMenuCmd());
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
    player.getPlayer().sendMessage("Rejoining challenge!");
  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic
  }
}
