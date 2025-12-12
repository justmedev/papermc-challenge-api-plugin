package at.iljabusch.challengeAPI.menus;

import at.iljabusch.challengeAPI.modifiers.RegisteredModifier;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

@Getter
@AllArgsConstructor
public class ChallengeMenuItem {

  private RegisteredModifier mod;
  @Setter
  private boolean isActive;

  public ItemStack getGuiItem() {
    return ChallengeCreationMenu.createGuiItem(
        mod.displayItem(),
        mod.name(),
        mod.author(),
        isActive ? "§2[Enabled]" : "§c[Disabled]"
    );
  }
}
