package at.iljabusch.challengeAPI.commands;

import at.iljabusch.challengeAPI.menus.ChallengeCreationMenu;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;

public class ChallengeCmd {

  public static LiteralCommandNode<CommandSourceStack> challengeCommand() {
    var rootCmd = Commands.literal("challenge");

    var createCmd = Commands.literal("create")
        .executes(ChallengeCmd::runCreateLogic);

    var inviteCmd = Commands.literal("invite")
        .executes(ctx -> {
          ctx.getSource().getSender()
              .sendRichMessage("<red>You have to include the required argument players!</red>");
          return Command.SINGLE_SUCCESS;
        })
        .then(
            Commands.argument("players", ArgumentTypes.players())
                .executes(ChallengeCmd::runInviteLogic)
        );

    rootCmd.then(createCmd);
    rootCmd.then(inviteCmd);
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
      e.sendRichMessage(
          """
              <gold>You've been invited to a challenge by <dark_red><invitee></dark_red>!
              <gold>Use <click:run_command:"/challenge accept %s"><dark_red>/challenge accept <invitee></dark_red></click> to accept the challenge!"""
              .formatted(executor.getName()),
          Placeholder.component("invitee", executor.name())
      );
    });

    return Command.SINGLE_SUCCESS;
  }

  private static int runCreateLogic(CommandContext<CommandSourceStack> ctx) {
    var sender = ctx.getSource().getSender();
    if (!(sender instanceof Player) || !(ctx.getSource()
        .getExecutor() instanceof Player executor)) {
      sender.sendRichMessage("<red>Only players can use this command!");
      return Command.SINGLE_SUCCESS;
    }

    executor.openInventory(new ChallengeCreationMenu().getInventory());
    return Command.SINGLE_SUCCESS;
  }
}