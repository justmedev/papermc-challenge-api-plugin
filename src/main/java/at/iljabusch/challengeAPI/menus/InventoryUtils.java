package at.iljabusch.challengeAPI.menus;

import com.destroystokyo.paper.profile.ProfileProperty;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jspecify.annotations.NonNull;

import java.util.UUID;

public class InventoryUtils {

  public static @NonNull ItemStack getSkull(SkullTextures.@NonNull SkullTexture skullTexture) {
    final ItemStack head = new ItemStack(Material.PLAYER_HEAD);

    head.editMeta(
        SkullMeta.class, skullMeta -> {
          final var playerProfile = Bukkit.createProfile(UUID.randomUUID(), null);
          playerProfile.setProperty(new ProfileProperty("textures", skullTexture.base64()));

          skullMeta.setPlayerProfile(playerProfile);
        }
    );
    return head;
  }
}
