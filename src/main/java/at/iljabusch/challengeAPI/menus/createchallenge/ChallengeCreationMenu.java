package at.iljabusch.challengeAPI.menus.createchallenge;

import at.iljabusch.challengeAPI.ChallengeAPI;
import at.iljabusch.challengeAPI.challenges.ChallengeManager;
import at.iljabusch.challengeAPI.menus.InventoryPager;
import at.iljabusch.challengeAPI.modifiers.RegisteredModifier;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NonNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import static org.apache.logging.log4j.LogManager.getLogger;

public class ChallengeCreationMenu implements InventoryHolder {

  public static final Material CREATE_CHALLENGE_MATERIAL = Material.GREEN_BANNER;
  private static final ChallengeAPI plugin = JavaPlugin.getPlugin(ChallengeAPI.class);
  @Getter
  private final Inventory inventory = plugin.getServer().createInventory(this, 9 * 3);
  @Getter
  private final InventoryPager<ModifierMenuItem> inventoryPager = new InventoryPager<>(this);

  /// ```
  /// Inventory (9x3) M=modifier, S=start challenge, r=reserved, paging: p=previous, n=next
  ///  0  1  2  3  4  5  6  7  8   <--   INDEX
  /// [M][M][M][M][M][M][M][r][S]
  /// [M][M][M][M][ ][ ][ ][r][r]
  /// [ ][ ][ ][ ][ ][ ][ ][p][n]
  /// ```

  public ChallengeCreationMenu() {
    inventoryPager.setPagedSectionDimensions(7, 3);

    var rawMap = new HashMap<Integer, ModifierMenuItem>();
    for (int i = 0; i < ChallengeManager.getInstance().getRegisteredModifiers().size(); i++) {
      var registeredMod = ChallengeManager.getInstance().getRegisteredModifiers().get(i);
      var cmi = new ModifierMenuItem(registeredMod, false);

      if (registeredMod.displayItem() == CREATE_CHALLENGE_MATERIAL) {
        getLogger().warn("{} is not allowed for modifiers!", CREATE_CHALLENGE_MATERIAL);
        continue;
      }

      rawMap.put(i, cmi);
    }

    inventoryPager.autoPage(rawMap);
    inventoryPager.drawPage();
    inventoryPager.drawPageArrows();
    this.inventory.setItem(8, createGuiItem(CREATE_CHALLENGE_MATERIAL, "Create challenge"));
  }

  public static ItemStack createGuiItem(Material material, String name, final Component... lore) {
    final var item = new ItemStack(material, 1);
    final var meta = item.getItemMeta();

    meta.displayName(Component.text(name));
    meta.lore(List.of(lore));
    item.setItemMeta(meta);

    return item;
  }

  public @NonNull Collection<RegisteredModifier> getActiveModifiers() {
    return inventoryPager.getPages().stream()
                         .map(page -> page
                             .values()
                             .stream()
                             .filter(ModifierMenuItem::isActive)
                             .map(ModifierMenuItem::getMod)
                             .toList())
                         .flatMap(List::stream)
                         .toList();
  }
}
