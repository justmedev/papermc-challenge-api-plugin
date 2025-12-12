package at.iljabusch.challengeAPI.menus;

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
    var item = myInventory.getMenuItemAtIndex(event.getSlot());
    if (item != null) {
      item.setActive(!item.isActive());
      myInventory.getInventory().setItem(event.getSlot(), item.getGuiItem());
    }
  }
}
