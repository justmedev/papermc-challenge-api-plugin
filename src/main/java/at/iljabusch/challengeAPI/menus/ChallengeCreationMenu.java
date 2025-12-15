package at.iljabusch.challengeAPI.menus;

import static org.apache.logging.log4j.LogManager.getLogger;
import at.iljabusch.challengeAPI.ChallengeAPI;
import at.iljabusch.challengeAPI.ChallengeManager;
import at.iljabusch.challengeAPI.modifiers.RegisteredModifier;
import java.util.Arrays;
import java.util.Collection;
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

  public static Material CREATE_CHALLENGE_MATERIAL = Material.GREEN_BANNER;

  /// ```
  /// Inventory (9x3) M=modifier, S=start challenge, r=reserved, e=must be empty
  ///  0  1  2  3  4  5  6  7  8   <--   INDEX
  /// [M][M][M][M][M][M][M][e][S]
  /// [M][M][M][M][ ][ ][ ][e][r]
  /// [ ][ ][ ][ ][ ][ ][ ][e][r]
  /// ```
  private final Inventory inventory;
  private final HashMap<Integer, ChallengeMenuItem> inventoryMap = new HashMap<>();

  public ChallengeCreationMenu() {
    var plugin = JavaPlugin.getPlugin(ChallengeAPI.class);
    this.inventory = plugin.getServer().createInventory(this, 9 * 3);

    for (int i = 0; i < ChallengeManager.getInstance().getRegisteredModifiers().size(); i++) {
      var registeredMod = ChallengeManager.getInstance().getRegisteredModifiers().get(i);
      var cmi = new ChallengeMenuItem(registeredMod, false);

      // This is done to skip e, S and r fields!
      var index = i;
      if (index > 6) {
        index += 2;
      }
      if (index > 15) {
        index += 2;
      }
      if (index > 7 * 3) {
        getLogger().warn("Too many modifiers! Paging not yet supported");
        return;
      }
      if (registeredMod.displayItem() == CREATE_CHALLENGE_MATERIAL) {
        getLogger().warn("{} is not allowed for modifiers!", CREATE_CHALLENGE_MATERIAL);
        continue;
      }

      this.inventory.setItem(index, cmi.getGuiItem());
      inventoryMap.put(index, cmi);
    }

    this.inventory.setItem(8, createGuiItem(CREATE_CHALLENGE_MATERIAL, "Create challenge"));

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

  public @NonNull Collection<RegisteredModifier> getActiveModifiers() {
    return this.inventoryMap.values().stream().map(ChallengeMenuItem::getMod).toList();
  }
}
