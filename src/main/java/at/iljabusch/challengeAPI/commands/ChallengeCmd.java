package at.iljabusch.challengeAPI.commands;

import static org.apache.logging.log4j.LogManager.getLogger;
import at.iljabusch.challengeAPI.Challenge;
import at.iljabusch.challengeAPI.PluginState;
import at.iljabusch.challengeAPI.challenges.sharedhealth.SharedHealthChallenge;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ChallengeCmd implements CommandExecutor {
  private final ArrayList<Challenge> challenges = new ArrayList<>();

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
      @NotNull String label, @NotNull String @NotNull [] args) {
    if (sender instanceof Player player) {
      if (args.length != 2) {
        return false;
      }

      if (args[0].equalsIgnoreCase("create")) {
        var otherPlayer = sender.getServer().getPlayer(args[1]);
        if (otherPlayer == null) {
          sender.sendMessage("The player is not online!");
          return true;
        }

        getLogger().info("Creating a new shared health challenge");
        var challenge = new SharedHealthChallenge();
        challenge.onCreate(List.of(player, otherPlayer));
        PluginState.getInstance().registerNewChallenge(challenge, List.of(player, otherPlayer));
      }
      return true;
    }

    sender.sendMessage("You must be a player!");
    return false;
  }
}
