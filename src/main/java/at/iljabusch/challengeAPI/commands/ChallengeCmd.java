package at.iljabusch.challengeAPI.commands;

import at.iljabusch.challengeAPI.ChallengeManager;
import at.iljabusch.challengeAPI.Permissions;
import at.iljabusch.challengeAPI.menus.ChallengeCreationMenu;
import at.iljabusch.challengeAPI.modifiers.RegisteredModifier;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;

public class ChallengeCmd {

  public static LiteralCommandNode<CommandSourceStack> challengeCommand() {
    var rootCmd = Commands.literal("challenge");

    var createCmd = Commands.literal("create")
        .executes(ChallengeCmd::runCreateLogic);

    var createInviteCmd = Commands.literal("invite")
        .executes(ctx -> {
          ctx.getSource().getSender()
              .sendRichMessage("<red>You have to include the required argument players!</red>");
          return Command.SINGLE_SUCCESS;
        })
        .then(
            Commands.argument("players", ArgumentTypes.players())
                .executes(ChallengeCmd::runInviteLogic)
        );

    var acceptInviteCmd = Commands.literal("accept")
        .executes(ctx -> {
          ctx.getSource().getSender()
              .sendRichMessage("<red>You have to include the required argument player!</red>");
          return Command.SINGLE_SUCCESS;
        })
        .then(
            Commands.argument("player", ArgumentTypes.player())
                .executes(ChallengeCmd::runAcceptInviteLogic)
        );

    var leaveCmd = Commands.literal("leave")
        .executes(ChallengeCmd::runLeaveChallengeLogic);

    var startCmd = Commands.literal("start")
        .executes(ChallengeCmd::runStartLogic);

    var infoCmd = Commands.literal("info")
        .executes(ChallengeCmd::runInfoLogic);

    rootCmd.then(createCmd);
    rootCmd.then(createInviteCmd);
    rootCmd.then(acceptInviteCmd);
    rootCmd.then(leaveCmd);
    rootCmd.then(startCmd);
    rootCmd.then(infoCmd);
    return rootCmd.build();
  }

  private static int runInviteLogic(CommandContext<CommandSourceStack> ctx)
      throws CommandSyntaxException {
    if (!(ctx.getSource().getExecutor() instanceof Player executor)) {
      ctx.getSource().getSender().sendRichMessage(
          "<red>Only players are allowed to invite other players to challenges!</red>");
      return Command.SINGLE_SUCCESS;
    }

    final var playerSelectorArgResolver = ctx.getArgument(
        "players",
        PlayerSelectorArgumentResolver.class
    );
    final var players = playerSelectorArgResolver.resolve(ctx.getSource());

    players.forEach(e -> {
      if (e == executor) {
        return;
      }
      ChallengeManager.getInstance().invitePlayerToChallenge(executor, e);
      e.sendRichMessage(
          """
              <gold>You've been invited to a challenge by <dark_red><invitee></dark_red>!
              <gold>Use <click:run_command:"/challenge accept %s"><dark_red>/challenge accept <invitee></dark_red></click> to accept the challenge!"""
              .formatted(executor.getName()),
          Placeholder.component("invitee", executor.name())
      );
    });

    ctx.getSource().getSender().sendRichMessage("<gold>Invite sent!");
    return Command.SINGLE_SUCCESS;
  }

  // TODO: Multiple invites per person
  private static int runAcceptInviteLogic(CommandContext<CommandSourceStack> ctx) {
    var sender = ctx.getSource().getSender();
    if (!(sender instanceof Player) || !(ctx.getSource()
        .getExecutor() instanceof Player executor)) {
      sender.sendRichMessage("<red>Only players can use this command!");
      return Command.SINGLE_SUCCESS;
    }

    ChallengeManager.getInstance().acceptInviteToChallenge(executor);
    return Command.SINGLE_SUCCESS;
  }

  private static int runCreateLogic(CommandContext<CommandSourceStack> ctx) {
    var sender = ctx.getSource().getSender();
    if (!(sender instanceof Player) || !(ctx.getSource()
        .getExecutor() instanceof Player executor)) {
      sender.sendRichMessage("<red>Only players can use this command!");
      return Command.SINGLE_SUCCESS;
    }
    if (!sender.hasPermission(Permissions.CHALLENGE_CREATE)) {
      sender.sendRichMessage("<red>You do not have permission to use this command!");
      return Command.SINGLE_SUCCESS;
    }

    if (!ChallengeManager.getInstance().canCreateChallenge(executor)) {
      sender.sendRichMessage("<red>You cannot create a challenge at this moment!");
      return Command.SINGLE_SUCCESS;
    }
    executor.openInventory(new ChallengeCreationMenu().getInventory());
    return Command.SINGLE_SUCCESS;
  }

  private static int runLeaveChallengeLogic(CommandContext<CommandSourceStack> ctx) {
    var sender = ctx.getSource().getSender();
    if (!(sender instanceof Player) || !(ctx.getSource()
        .getExecutor() instanceof Player executor)) {
      sender.sendRichMessage("<red>Only players can use this command!");
      return Command.SINGLE_SUCCESS;
    }

    ChallengeManager.getInstance().leaveChallenge(executor);
    return Command.SINGLE_SUCCESS;
  }

  private static int runStartLogic(CommandContext<CommandSourceStack> ctx) {
    var sender = ctx.getSource().getSender();
    if (!(sender instanceof Player) || !(ctx.getSource()
        .getExecutor() instanceof Player executor)) {
      sender.sendRichMessage("<red>Only players can use this command!");
      return Command.SINGLE_SUCCESS;
    }

    ChallengeManager.getInstance().startChallenge(executor);
    return Command.SINGLE_SUCCESS;
  }

  private static int runInfoLogic(CommandContext<CommandSourceStack> ctx) {
    var sender = ctx.getSource().getSender();
    if (!(sender instanceof Player) || !(ctx.getSource()
        .getExecutor() instanceof Player executor)) {
      sender.sendRichMessage("<red>Only players can use this command!");
      return Command.SINGLE_SUCCESS;
    }

    var playerInChallenge = ChallengeManager.getInstance().getPlayersInChallenges()
        .get(executor.getUniqueId());
    if (playerInChallenge == null) {
      sender.sendRichMessage("<red>You are not partaking in any challenges!");
      return Command.SINGLE_SUCCESS;
    }

    sender.sendRichMessage(
        """
            <gold>--- [ Challenge Info ] ---
            <gold>Active modifiers:
            <dark_red><modifiers></dark_red>
            <gold>Partaking players:
            <dark_red><players></dark_red>""",
        Placeholder.unparsed(
            "modifiers",
            String.join(
                ", ",
                playerInChallenge
                    .getChallenge()
                    .getRegisteredModifiers()
                    .stream()
                    .map(RegisteredModifier::name)
                    .toList()
            )
        ),
        Placeholder.unparsed(
            "players",
            String.join(
                ", ",
                playerInChallenge
                    .getChallenge()
                    .getOnlinePlayers()
                    .stream()
                    .map(Player::getName)
                    .toList()
            )
        )
    );

    return Command.SINGLE_SUCCESS;
  }
}