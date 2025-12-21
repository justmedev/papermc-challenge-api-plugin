package at.iljabusch.challengeAPI.menus;

import at.iljabusch.challengeAPI.modifiers.RegisteredModifier;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.inventory.ItemStack;

@Getter
@AllArgsConstructor
public class ChallengeMenuItem {

  private RegisteredModifier mod;
  @Setter
  private boolean isActive;

  public ItemStack getGuiItem() {
    // TODO: disallow reserved materials
    return ChallengeCreationMenu.createGuiItem(
        mod.getDisplayItem(),
        mod.getName(),
        Component.text(mod.getAuthor()),
        isActive ? Component.text("[Enabled]", NamedTextColor.GREEN)
            : Component.text("[Disabled]", NamedTextColor.RED)
    );
  }
}
