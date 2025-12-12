package at.iljabusch.challengeAPI.menus;

import at.iljabusch.challengeAPI.ChallengeAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NonNull;

public class ChallengeCreationMenu implements InventoryHolder {

  private final Inventory inventory;

  public ChallengeCreationMenu() {
    var plugin = JavaPlugin.getPlugin(ChallengeAPI.class);
    this.inventory = plugin.getServer().createInventory(this, 9);
    this.inventory.setItem(0, ItemStack.of(Material.STONE));

    Bukkit.getPluginManager().registerEvents(new ChallengeCreationMenuListener(), plugin);
  }

  @Override
  public @NonNull Inventory getInventory() {
    return this.inventory;
  }
}
