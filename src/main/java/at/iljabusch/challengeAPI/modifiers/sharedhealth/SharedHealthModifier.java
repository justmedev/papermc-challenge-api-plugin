package at.iljabusch.challengeAPI.modifiers.sharedhealth;

import static org.bukkit.Bukkit.getServer;
import at.iljabusch.challengeAPI.Challenge;
import at.iljabusch.challengeAPI.ChallengeAPI;
import at.iljabusch.challengeAPI.modifiers.Modifier;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SharedHealthModifier extends Modifier {

  public SharedHealthModifier(Challenge challenge) {
    super(challenge);
  }

  @Override
  public void onChallengeStarted() {
    getServer().getPluginManager().registerEvents(
        new SharedHealthModifierEventListener(this),
        JavaPlugin.getPlugin(ChallengeAPI.class)
    );
  }

  @Override
  public void onPlayerJoin(Player player) {
    var otherPlayer = challenge.getPlayers().getFirst();
    player.setFoodLevel(otherPlayer.getFoodLevel());
    player.setHealth(otherPlayer.getHealth());
  }
}
