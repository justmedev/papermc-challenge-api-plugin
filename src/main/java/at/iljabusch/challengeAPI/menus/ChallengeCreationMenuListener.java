package at.iljabusch.challengeAPI.menus;

import static org.apache.logging.log4j.LogManager.getLogger;
import at.iljabusch.challengeAPI.Challenge;
import at.iljabusch.challengeAPI.ChallengeManager;
import java.util.HashSet;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;

public class ChallengeCreationMenuListener implements Listener {
  private final Plugin plugin;

    public ChallengeCreationMenuListener(Plugin plugin) {
        this.plugin = plugin;
    }

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

      if(item.getType() == Material.PLAYER_HEAD){
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        if(pdc.has(new NamespacedKey(plugin, "Last_Page"))){
          myInventory.lastPage();
        }

        if(pdc.has(new NamespacedKey(plugin, "Next_Page"))){
          myInventory.nextPage();
        }
      }

      if (item != null && item.getType() == ChallengeCreationMenu.CREATE_CHALLENGE_MATERIAL) {
        // Start challenge with modifiers
        var player = (Player) event.getWhoClicked();
        getLogger().info("Start challenge with selected modifiers!");
        player.closeInventory();
        ChallengeManager.getInstance().registerNewChallenge(
            new Challenge(
                player,
                new HashSet<>(myInventory.getActiveModifiers())
            ),
            player
        );
      }

    } catch (ArrayIndexOutOfBoundsException e) {
      // Is called when player clicked out of inventory or in the bottom inventory.
    }
  }

}
