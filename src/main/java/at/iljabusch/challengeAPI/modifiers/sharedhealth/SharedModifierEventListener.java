package at.iljabusch.challengeAPI.modifiers.sharedhealth;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
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
      p.sendRichMessage(
          "<yellow>Player <dark_red><player></dark_red> has been damaged!",
          Placeholder.component("player", player.name())
      );
      p.setHealth(player.getHealth() - event.getFinalDamage());
    });
  }
}
