package at.iljabusch.challengeAPI.commands;

import at.iljabusch.challengeAPI.menus.ChallengeCreationMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ChallengeMenuCmd implements CommandExecutor {

  private final ChallengeCreationMenu menu = new ChallengeCreationMenu();

  @Override
  public boolean onCommand(@NotNull CommandSender sender,
                           @NotNull Command command,
                           @NotNull String label,
                           @NotNull String @NotNull [] args) {
    if (!(sender instanceof Player player)) {
      sender.sendMessage("&cOnly players can use this command.");
      return true;
    }

    player.openInventory(menu.getInventory());
    return true;
  }
}
