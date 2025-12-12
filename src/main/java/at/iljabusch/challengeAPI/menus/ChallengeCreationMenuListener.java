package at.iljabusch.challengeAPI.menus;

import static org.apache.logging.log4j.LogManager.getLogger;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ChallengeCreationMenuListener implements Listener {

  @EventHandler
  public void onInventoryClick(InventoryClickEvent event) {
    var inventory = event.getInventory();
    if (!(inventory.getHolder(false) instanceof ChallengeCreationMenu myInventory)) {
      return;
    }

    event.setCancelled(true);
    getLogger().info("Clicked in slot: {}", event.getSlot());
    if (event.getSlot() == 0) {
      event.getWhoClicked().sendMessage(Component.text("You clicked!"));
    }
  }
}
