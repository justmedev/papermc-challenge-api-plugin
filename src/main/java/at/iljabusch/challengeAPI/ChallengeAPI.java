package at.iljabusch.challengeAPI;

import at.iljabusch.challengeAPI.commands.ChallengeCmd;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class ChallengeAPI extends JavaPlugin implements Listener {
  private GlobalState state = GlobalState.getInstance();

  @Override
  public void onEnable() {
    getLogger().info("ChallengeAPI Plugin is starting ...");
    Bukkit.getPluginManager().registerEvents(this, this);

    getCommand("challenge").setExecutor(new ChallengeCmd());
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    getLogger().info("%s joined!".formatted(event.getPlayer().getUniqueId()));
    var player = state.getPlayersInChallenges().get(event.getPlayer().getUniqueId());
    if (player == null) {
      return;
    }

    player.setPlayer(event.getPlayer());
    player.getChallenge().onJoin(player.getPlayer());
    player.getPlayer().sendMessage("Rejoining challenge!");
  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic
  }
}
