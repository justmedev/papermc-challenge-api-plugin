package at.iljabusch.challengeAPI.commands;

import static org.apache.logging.log4j.LogManager.getLogger;
import at.iljabusch.challengeAPI.Challenge;
import at.iljabusch.challengeAPI.ChallengeManager;
import at.iljabusch.challengeAPI.modifiers.sharedhealth.SharedHealthModifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ChallengeCmd implements CommandExecutor, TabExecutor {
  @Override
  public boolean onCommand(@NotNull CommandSender sender,
                           @NotNull Command command,
                           @NotNull String label,
                           @NotNull String @NotNull [] args) {
    if (sender instanceof Player player) {
      if (args.length < 1) {
        return false;
      }

      var players = new ArrayList<>(List.of(player));
      for (int i = 0; i < args.length; i++) {
        if (i == 0) {
          continue;
        }

        var otherPlayer = sender.getServer().getPlayer(args[1]);
        if (otherPlayer == null) {
          sender.sendMessage("The player is not online!");
          return true;
        }
        if (otherPlayer.getPlayer() == player) {
          sender.sendMessage("The player cannot be yourself!");
          return false;
        }
        players.add(otherPlayer);
      }

      if (args[0].equalsIgnoreCase("create")) {
        getLogger().info("Creating a new shared health challenge");


        // var challenge = new Challenge(player);
        // challenge.setModifiers(Set.of(new SharedHealthModifier(challenge)));
        // ChallengeManager.getInstance().registerNewChallenge(challenge, challenge.getPlayers());
      }
      return true;
    }

    sender.sendMessage("You must be a player!");
    return false;
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender sender,
                                              @NotNull Command command,
                                              @NotNull String label,
                                              @NotNull String @NotNull [] args) {
    if (args.length == 1) {
      return List.of("create");
    }
    if (args.length > 1) {
      return sender.getServer().getOnlinePlayers().stream().map(Player::getName).toList();
    }
    return List.of();
  }
}
