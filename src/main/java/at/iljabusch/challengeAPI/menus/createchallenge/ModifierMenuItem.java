package at.iljabusch.challengeAPI.menus.createchallenge;

import at.iljabusch.challengeAPI.menus.InventoryItem;
import at.iljabusch.challengeAPI.modifiers.RegisteredModifier;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;

@Getter
@AllArgsConstructor
public class ModifierMenuItem implements InventoryItem {

  private RegisteredModifier mod;
  @Setter
  private boolean isActive;

  public @NonNull ItemStack getItemStack() {
    // TODO: disallow reserved materials
    return ChallengeCreationMenu.createGuiItem(
        mod.displayItem(),
        mod.name(),
        Component.text(mod.author()),
        isActive ? Component.text("[Enabled]", NamedTextColor.GREEN)
            : Component.text("[Disabled]", NamedTextColor.RED)
    );
  }
}
