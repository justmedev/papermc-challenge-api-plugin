package at.iljabusch.challengeAPI;

import at.iljabusch.challengeAPI.commands.ChallengeCmd;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class ChallengeAPI extends JavaPlugin implements Listener {

  /*
  Store uuid of player and nullable player object with challenge state
   */
  public HashMap<UUID, PlayerInChallenge> playersInChallenge = new HashMap<>();


  @Override
  public void onEnable() {
    getLogger().info("ChallengeAPI Plugin is starting ...");
    Bukkit.getPluginManager().registerEvents(this, this);

    getCommand("challenge").setExecutor(new ChallengeCmd());
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    getLogger().info("%s joined!".formatted(event.getPlayer().getUniqueId()));
    // event.getPlayer().sendMessage(Component.text("Hello, " + event.getPlayer().getName() + "!"));
  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic
  }
}
