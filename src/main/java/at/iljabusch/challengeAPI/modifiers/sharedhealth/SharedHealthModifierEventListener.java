package at.iljabusch.challengeAPI.modifiers.sharedhealth;

import at.iljabusch.challengeAPI.modifiers.ModifierListener;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

public class SharedHealthModifierEventListener implements ModifierListener {

  private final SharedHealthModifier modifier;

  public SharedHealthModifierEventListener(SharedHealthModifier modifier) {
    this.modifier = modifier;
  }

  @EventHandler(ignoreCancelled = true)
  public void onPlayerDamage(EntityDamageEvent event) {
    if (!(event.getEntity() instanceof Player player)) {
      return;
    }
    if (!modifier.getChallenge().getPlayers().contains(player)) {
      return;
    }

    var newHealth = player.getHealth() - event.getFinalDamage();

    if (newHealth <= 0) {
      player.sendRichMessage("<dark_red>You have died");
      modifier.getChallenge().complete(false);
      event.setCancelled(true);
    }

    modifier.getChallenge().getPlayers().forEach(p -> {
      if (player.getUniqueId() == p.getUniqueId()) {
        return;
      }
      if (newHealth <= 0) {
        p.sendRichMessage(
            "<yellow>Player <dark_red><player></dark_red> has died!",
            Placeholder.component("player", player.name())
        );
        modifier.getChallenge().complete(false);
        event.setCancelled(true);
        return;
      }

      p.sendRichMessage(
          "<yellow>Player <dark_red><player></dark_red> has been damaged!",
          Placeholder.component("player", player.name())
      );
      p.setHealth(newHealth);
    });
  }

  @Override
  public void dispose() {
    EntityDamageEvent.getHandlerList().unregister(this);
  }
}
