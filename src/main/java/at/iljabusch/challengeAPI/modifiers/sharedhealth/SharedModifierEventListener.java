package at.iljabusch.challengeAPI.modifiers.sharedhealth;

import at.iljabusch.challengeAPI.modifiers.ModifierListener;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class SharedModifierEventListener implements ModifierListener {

  private final SharedHealthModifier modifier;

  public SharedModifierEventListener(SharedHealthModifier modifier) {
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

    modifier.getChallenge().getPlayers().forEach(p -> {
      if (player.getUniqueId() == p.getUniqueId()) {
        return;
      }
      p.sendRichMessage(
          "<yellow>Player <dark_red><player></dark_red> has been damaged!",
          Placeholder.component("player", player.name())
      );
      p.setHealth(player.getHealth() - event.getFinalDamage());
    });
  }

  @EventHandler(ignoreCancelled = true)
  public void onPlayerDeath(EntityDeathEvent event) {
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
      p.sendRichMessage(
          "<yellow>Player <dark_red><player></dark_red> has died!",
          Placeholder.component("player", player.name())
      );
      p.setKiller(player);
      p.setHealth(0);
    });
  }

  @Override
  public void dispose() {
    EntityDamageEvent.getHandlerList().unregister(this);
  }
}
