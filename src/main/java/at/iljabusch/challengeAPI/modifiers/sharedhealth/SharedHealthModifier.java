package at.iljabusch.challengeAPI.modifiers.sharedhealth;

import static org.bukkit.Bukkit.getServer;
import at.iljabusch.challengeAPI.Challenge;
import at.iljabusch.challengeAPI.ChallengeAPI;
import at.iljabusch.challengeAPI.modifiers.Modifier;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SharedHealthModifier extends Modifier {

  private final SharedHealthModifierEventListener eventListener = new SharedHealthModifierEventListener(
      this);

  public SharedHealthModifier(Challenge challenge) {
    super(challenge);
  }

  @Override
  public void onChallengeStarted() {
    getServer().getPluginManager().registerEvents(
        eventListener,
        JavaPlugin.getPlugin(ChallengeAPI.class)
    );
  }

  @Override
  public void onPlayerJoin(Player player) {
    var possibleSource = challenge.getPlayers().getFirst();
    if (possibleSource.equals(player)) {
      return;
    }
    eventListener.syncAll(possibleSource);
  }

  @Override
  public void onDispose() {
    eventListener.dispose();
  }
}
