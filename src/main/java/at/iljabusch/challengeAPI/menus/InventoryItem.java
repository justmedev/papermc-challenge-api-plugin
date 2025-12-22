package at.iljabusch.challengeAPI.menus;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;

public interface InventoryItem {

  @NonNull ItemStack getItemStack();
}
