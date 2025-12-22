package at.iljabusch.challengeAPI.menus.createchallenge;

import at.iljabusch.challengeAPI.Challenge;
import at.iljabusch.challengeAPI.ChallengeManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.HashSet;

import static org.apache.logging.log4j.LogManager.getLogger;

public class ChallengeCreationMenuListener implements Listener {

  @EventHandler
  public void onInventoryClick(InventoryClickEvent event) {

    var inventory = event.getInventory();
    if (!(inventory.getHolder(false) instanceof ChallengeCreationMenu ccm)) {
      return;
    }

    try {
      event.setCancelled(true);
      var challengeMenuItem = ccm.getInventoryPager().getCurrentPage().get(
          event.getSlot()
      );
      if (challengeMenuItem != null) {
        challengeMenuItem.setActive(!challengeMenuItem.isActive());
        ccm.getInventory().setItem(
            event.getSlot(),
            challengeMenuItem.getItemStack()
        );
      }

      var item = ccm.getInventory().getItem(event.getSlot());
      if (item != null) {
        if (item.getType() == ChallengeCreationMenu.CREATE_CHALLENGE_MATERIAL) {
          // Start challenge with modifiers
          var player = (Player) event.getWhoClicked();
          getLogger().info("Start challenge with selected modifiers!");
          player.closeInventory();
          ChallengeManager.getInstance().registerNewChallenge(
              new Challenge(
                  player,
                  new HashSet<>(ccm.getActiveModifiers())
              ),
              player
          );
        } else if (item.getType() == Material.PLAYER_HEAD) {
          if (event.getSlot() == inventory.getSize() - 2) {
            // arrow previous
            ccm.getInventoryPager().previousPage();
          } else if (event.getSlot() == inventory.getSize() - 1) {
            // arrow next
            ccm.getInventoryPager().nextPage();
          }
        }
      }
    } catch (ArrayIndexOutOfBoundsException e) {
      // Is called when player clicked out of inventory or in the bottom inventory.
    }
  }
}
