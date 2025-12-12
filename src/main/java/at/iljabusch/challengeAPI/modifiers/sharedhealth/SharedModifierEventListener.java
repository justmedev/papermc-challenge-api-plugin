package at.iljabusch.challengeAPI.modifiers.sharedhealth;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class SharedModifierEventListener implements Listener {

  private final SharedHealthModifier modifier;

  public SharedModifierEventListener(SharedHealthModifier modifier) {
    this.modifier = modifier;
  }

  @EventHandler
  public void onPlayerDamage(EntityDamageEvent event) {
    if (!(event.getEntity() instanceof Player player)) {
      return;
    }
    if (!modifier.getChallenge().getPlayers().contains(player)) {
      return;
    }

    modifier.getChallenge().getPlayers().forEach(p -> {
      if (player.getUniqueId() == p.getUniqueId()) {
        return;
      }
      p.sendMessage(Component.text("&e%s&c has been damaged!".formatted(player.getName())));
      p.setHealth(player.getHealth() - event.getFinalDamage());
    });
  }
}
