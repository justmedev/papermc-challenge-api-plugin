package at.iljabusch.challengeAPI.menus;

import at.iljabusch.challengeAPI.ChallengeAPI;
import at.iljabusch.challengeAPI.GlobalState;
import java.util.Arrays;
import java.util.HashMap;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class ChallengeCreationMenu implements InventoryHolder {

  private final Inventory inventory;
  private final HashMap<Integer, ChallengeMenuItem> inventoryMap = new HashMap<>();

  public ChallengeCreationMenu() {
    var plugin = JavaPlugin.getPlugin(ChallengeAPI.class);
    this.inventory = plugin.getServer().createInventory(this, 9 * 3);

    for (int i = 0; i < GlobalState.getInstance().getRegisteredModifiers().size(); i++) {
      var registeredMod = GlobalState.getInstance().getRegisteredModifiers().get(i);
      var cmi = new ChallengeMenuItem(registeredMod, false);

      this.inventory.setItem(i, cmi.getGuiItem());
      inventoryMap.put(i, cmi);
    }

    Bukkit.getPluginManager().registerEvents(new ChallengeCreationMenuListener(), plugin);
  }

  public static ItemStack createGuiItem(Material material, String name, final String... lore) {
    final var item = new ItemStack(material, 1);
    final var meta = item.getItemMeta();

    meta.displayName(Component.text(name));
    meta.lore(Arrays.stream(lore).map(Component::text).toList());
    item.setItemMeta(meta);

    return item;
  }

  @Override
  public @NonNull Inventory getInventory() {
    return this.inventory;
  }

  public @Nullable ChallengeMenuItem getMenuItemAtIndex(int index) {
    return this.inventoryMap.get(index);
  }
}
