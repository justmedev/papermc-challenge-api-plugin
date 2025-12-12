package at.iljabusch.challengeAPI.v2.modifiers;

import static org.bukkit.Bukkit.getServer;
import at.iljabusch.challengeAPI.ChallengeAPI;
import at.iljabusch.challengeAPI.v2.Challenge;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class SharedHealthModifier extends Modifier implements Listener {

  public SharedHealthModifier(Challenge challenge) {
    super(challenge);
    getServer().getPluginManager().registerEvents(new SharedModifierEventListener(this), JavaPlugin.getPlugin(ChallengeAPI.class));
  }

  @Override
  public String getName() {
    return "Shared Health";
  }

  @Override
  public String getAuthor() {
    return "Ilja Busch";
  }

  @Override
  public void onPlayerJoin(Player player) {
    var otherPlayer = challenge.getPlayers().getFirst();
    player.setFoodLevel(otherPlayer.getFoodLevel());
    player.setHealth(otherPlayer.getHealth());
  }
}
