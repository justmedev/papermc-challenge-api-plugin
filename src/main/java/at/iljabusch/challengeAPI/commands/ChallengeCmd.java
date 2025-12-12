package at.iljabusch.challengeAPI.commands;

import static org.apache.logging.log4j.LogManager.getLogger;
import at.iljabusch.challengeAPI.GlobalState;
import at.iljabusch.challengeAPI.v2.Challenge;
import at.iljabusch.challengeAPI.v2.modifiers.Modifier;
import at.iljabusch.challengeAPI.v2.modifiers.SharedHealthModifier;
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
  private final ArrayList<Challenge> challenges = new ArrayList<>();

  @Override
  public boolean onCommand(@NotNull CommandSender sender,
                           @NotNull Command command,
                           @NotNull String label,
                           @NotNull String @NotNull [] args) {
    if (sender instanceof Player player) {
      if (args.length != 2) {
        return false;
      }

        /*var otherPlayer = sender.getServer().getPlayer(args[1]);
        if (otherPlayer == null) {
          sender.sendMessage("The player is not online!");
          return true;
        }
        if (otherPlayer.getPlayer() == player) {
          sender.sendMessage("The player cannot be yourself!");
          return false;
        }*/
      if (args[0].equalsIgnoreCase("create")) {
        getLogger().info("Creating a new shared health challenge");

        var challenge = new Challenge(new ArrayList<>(List.of(player)));
        challenge.setModifiers(Set.of(new SharedHealthModifier(challenge)));
        GlobalState.getInstance().registerNewChallenge(challenge, challenge.getPlayers());
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
    return switch (args.length) {
      case 1 -> List.of("create", "join");
      case 2 -> sender.getServer().getOnlinePlayers().stream().map(Player::getName).toList();
      default -> List.of();
    };
  }
}
