package at.iljabusch.challengeAPI.v2.modifiers;

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
    modifier.challenge.getPlayers().forEach(p -> {
      if (player.getUniqueId() == p.getUniqueId()) {
        return;
      }
      p.damage(event.getDamage());
    });
  }
}
