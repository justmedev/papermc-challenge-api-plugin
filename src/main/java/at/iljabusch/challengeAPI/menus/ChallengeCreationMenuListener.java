package at.iljabusch.challengeAPI.menus;

import static org.apache.logging.log4j.LogManager.getLogger;
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

    try {
      event.setCancelled(true);
      var challengeItem = myInventory.getMenuItemAtIndex(event.getSlot());
      if (challengeItem != null) {
        challengeItem.setActive(!challengeItem.isActive());
        myInventory.getInventory().setItem(event.getSlot(), challengeItem.getGuiItem());
      }

      var item = myInventory.getInventory().getItem(event.getSlot());
      if (item != null && item.getType() == ChallengeCreationMenu.CREATE_CHALLENGE_MATERIAL) {
        // Start challenge with modifiers
        getLogger().info("Start challenge with selected modifiers!");
      }
    } catch (ArrayIndexOutOfBoundsException e) {
      // Is called when player clicked out of inventory or in the bottom inventory.
    }
  }
}
