package at.iljabusch.challengeAPI.menus;

import static org.apache.logging.log4j.LogManager.getLogger;
import at.iljabusch.challengeAPI.ChallengeAPI;
import at.iljabusch.challengeAPI.ChallengeManager;
import at.iljabusch.challengeAPI.modifiers.RegisteredModifier;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

import com.mojang.authlib.properties.PropertyMap;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

public class ChallengeCreationMenu implements InventoryHolder {

  public static Material CREATE_CHALLENGE_MATERIAL = Material.GREEN_BANNER;
  public static final int MIN_INV_ROWS = 3;
  public static final int MAX_INV_ROWS = 6;
  /// ```
  /// Inventory (9x3) M=modifier, S=start challenge, r=reserved, e=must be empty
  ///  0  1  2  3  4  5  6  7  8   <--   INDEX
  /// [M][M][M][M][M][M][M][e][S]
  /// [M][M][M][M][ ][ ][ ][e][r]
  /// [ ][ ][ ][ ][ ][ ][ ][e][r]
  /// ```

  private Inventory inventory;
  private final List<ItemStack[]> pages = new ArrayList<>();
  private final List<HashMap<Integer, ChallengeMenuItem>> pagesInventoryMap = new ArrayList<>();
  private int currentPageIndex = 0;

  public ChallengeCreationMenu() {
    var plugin = JavaPlugin.getPlugin(ChallengeAPI.class);

    int numModifiers = ChallengeManager.getInstance().getRegisteredModifiers().size();
    int numRows = (int) Math.ceil(numModifiers / 7.0);
    int numPages = (int)Math.ceil(Math.ceil(numModifiers / 7.0) / (double)MAX_INV_ROWS);
    int totalSlots = numPages * 9 * MAX_INV_ROWS;
    int rowsPerPage = Math.max((numPages > 1) ? MAX_INV_ROWS :  numRows, MIN_INV_ROWS);

    for(int j = 0; j < numPages; j++) {
      int numSlotsCurrentPage =  (j == numPages - 1) ? (totalSlots % (rowsPerPage * 9 ) == 0? (rowsPerPage * 9) : totalSlots % (rowsPerPage * 9)) : rowsPerPage * 9;
      int numModifierCurrentPage = (j == numPages - 1) ?(totalSlots % (rowsPerPage * 7) == 0? (rowsPerPage * 7) : numModifiers % (rowsPerPage * 7)) : rowsPerPage * 7;
      int lastModifierIndex = 9 * (numModifierCurrentPage / 7) + numModifierCurrentPage % 7;

      pages.add(new ItemStack[numSlotsCurrentPage]);
      pagesInventoryMap.add(currentPageIndex, new HashMap<>());
      for(int i = 0; i < lastModifierIndex; i++) {
        if(i % 9 >  6){
          i+=2;
          continue;
        }

        int modifierIndex = rowsPerPage* 7 * j + i;
        var registeredMod = ChallengeManager.getInstance().getRegisteredModifiers().get(modifierIndex);

        pagesInventoryMap.get(j).put(i, new ChallengeMenuItem(registeredMod, false));
        pages.get(j)[i] = pagesInventoryMap.get(j).get(i).getGuiItem();
      }

      pages.get(j)[8] =  createGuiItem(CREATE_CHALLENGE_MATERIAL, "Create challenge");

      pages.get(j)[numSlotsCurrentPage - 10] =  getLastPageItem(plugin);
      pages.get(j)[numSlotsCurrentPage - 1] =  getNextPageItem(plugin);
    }

    inventory = Bukkit.createInventory(this, rowsPerPage * 9, Component.text("Select Modifiers"));

    this.inventory.setContents(pages.get(currentPageIndex));
    
  }

  public static ItemStack getHeadWithTexture(String url)  {
    UUID uuid = UUID.nameUUIDFromBytes("CustomHead".getBytes());
    PlayerProfile profile = Bukkit.createPlayerProfile(uuid, "Player");

    PlayerTextures textures = profile.getTextures();

    URL skinurl = null;
    try {
      skinurl = new URL(url);
    }
    catch (MalformedURLException e) {}
    textures.setSkin(skinurl, PlayerTextures.SkinModel.CLASSIC);
    profile.setTextures(textures);
    ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
    SkullMeta meta = (SkullMeta) head.getItemMeta();
    meta.setOwnerProfile(profile);


    head.setItemMeta(meta);
    return head;
  }

  private static ItemStack getLastPageItem(Plugin plugin) {
    ItemStack lastPage = getHeadWithTexture("http://textures.minecraft.net/texture/4ce36fcb1e5f6b36517fbbeb9cbf4b0c05c30d8bdb5154824e60e6d550f528e9");
    SkullMeta meta = (SkullMeta) lastPage.getItemMeta();
    meta.displayName(Component.text("Last Page"));

    NamespacedKey key = new NamespacedKey(plugin, "Last_Page");
    meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "true");

    lastPage.setItemMeta(meta);
    return lastPage;
  }
  private static ItemStack getNextPageItem(Plugin plugin) {
    ItemStack nextPage = getHeadWithTexture("http://textures.minecraft.net/texture/335c1ef127f5c52cb389a8ac6df3f6fc66cd37f26094b3e1e76d0177115bb08f");
    SkullMeta meta = (SkullMeta)nextPage.getItemMeta();
    meta.displayName(Component.text("Next Page"));

    NamespacedKey key = new NamespacedKey(plugin, "Next_Page");
    meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "true");

    nextPage.setItemMeta(meta);
    return nextPage;
  }

  public void nextPage(){
    if(currentPageIndex < pages.size() - 1){
      currentPageIndex++;
      inventory.setContents(pages.get(currentPageIndex));
    }
  }
  public void lastPage(){
    if(currentPageIndex >0){
      currentPageIndex--;
      inventory.setContents(pages.get(currentPageIndex));
    }
  }

  public static ItemStack createGuiItem(Material material, String name, final Component... lore) {
    final var item = new ItemStack(material, 1);
    final var meta = item.getItemMeta();

    meta.displayName(Component.text(name));
    meta.lore(List.of(lore));
    item.setItemMeta(meta);

    return item;
  }

  @Override
  public @NonNull Inventory getInventory() {
    return this.inventory;
  }

  public @Nullable ChallengeMenuItem getMenuItemAtIndex(int index) {
    return this.pagesInventoryMap.get(currentPageIndex).get(index);
  }

  public @NonNull Collection<RegisteredModifier> getActiveModifiers() {
    return this.pagesInventoryMap
        .stream()
        .flatMap(map -> map.values().stream())
        .filter(ChallengeMenuItem::isActive)
        .map(ChallengeMenuItem::getMod)
        .toList();
  }
}
