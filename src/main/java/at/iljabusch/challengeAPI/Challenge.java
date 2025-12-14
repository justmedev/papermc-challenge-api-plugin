package at.iljabusch.challengeAPI;

import static org.apache.logging.log4j.LogManager.getLogger;
import at.iljabusch.challengeAPI.modifiers.RegisteredModifier;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.World.Environment;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.mvplugins.multiverse.core.MultiverseCoreApi;
import org.mvplugins.multiverse.core.world.MultiverseWorld;
import org.mvplugins.multiverse.core.world.options.CreateWorldOptions;

@Getter

public class Challenge {

  private ChallengeState state;

  private MultiverseWorld world;
  private final ArrayList<Player> players = new ArrayList<>();
  @Setter
  private Set<RegisteredModifier> modifiers;

  public Challenge(Player creator, Set<RegisteredModifier> modifiers) {
    creator.sendMessage(Component.text("§6Creating challenge ...§r"));
    this.modifiers = modifiers;

    this.players.add(creator);
    this.players.forEach(player -> {
      player.setHealthScaled(false);
      player.setHealth(player.getAttribute(Attribute.MAX_HEALTH).getValue());
      player.setFoodLevel(20); // Fully fed
    });

    // TODO: Delete world once challenge ends
    MultiverseCoreApi.get().getWorldManager()
        .createWorld(
            CreateWorldOptions
                .worldName("world_challenge_" + UUID.randomUUID())
                .environment(Environment.NORMAL)
        )
        .onFailure(reason -> {
          getLogger().error("Failed to create world for challenge: {}", reason);
          // TODO: Handle this
        })
        .onSuccess(world -> {
          creator.sendMessage(Component.text("§6Challenge world created ...§r"));
          this.world = world;
          this.state = ChallengeState.READY;

          var message = Component
              .text("Your challenge was created successfully!", NamedTextColor.GOLD)
              .append(Component.text("\nSelected modifiers: ", NamedTextColor.GOLD))
              .append(Component.text(
                  String.join(", ", modifiers.stream().map(RegisteredModifier::name).toList()),
                  NamedTextColor.DARK_RED
              ))
              .append(Component.text("Use ", NamedTextColor.GOLD))
              .append(Component.text("/challenge-invite", NamedTextColor.DARK_RED)
                          .clickEvent(ClickEvent.runCommand("challenge-invite")))
              .append(Component.text(
                  " to invite other players to your challenge! ",
                  NamedTextColor.GOLD
              ))
              .append(Component.text("When ready start the challenge using ", NamedTextColor.GOLD))
              .append(Component.text("/challenge-accept", NamedTextColor.DARK_RED)
                          .clickEvent(ClickEvent.runCommand("challenge-accept")))
              .append(Component.text("!", NamedTextColor.GOLD));
          creator.sendMessage(message);

          /*creator.sendMessage(Component.text("""
                                                 §eLoading challenge world ...
                                                 §k§f---------------------------------§r§6
                                                 §6Your challenge was created successfully!
                                                 §6Selected modifiers: §4%s§r
                                                 §6You can now use the §e/challenge-invite §6command, to invite players to your challenge.
                                                 §6When ready, you can start the challenge by executing §e/challenge-start§6. Everybody will be teleported to the challenge world and the challenge begins!."""
                                                 .formatted(modifiers.stream()
                                                                .map(RegisteredModifier::name))
          ));*/
        });
  }

  public void start() {
    this.state = ChallengeState.ONGOING;

    players.forEach(p -> {
      p.sendMessage(Component.text("§6Challenge started ...§r"));
      p.teleport(world.getSpawnLocation());
    });
  }

  public void join(Player player) {
    players.add(player);
    if (this.state == ChallengeState.ONGOING) {
      player.sendMessage(Component.text("§6Rejoining challenge ...§r"));
      player.teleport(world.getSpawnLocation());
    }
  }

  public void leave(Player player) {
    players.remove(player);
    if (!players.isEmpty()) {
      return;
    }

    getLogger().info("Challenge closing because all players left!");
    // TODO: Actually close challenge, delete world, ...
  }
}
