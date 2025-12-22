package at.iljabusch.challengeAPI;

import at.iljabusch.challengeAPI.modifiers.RegisteredModifier;
import lombok.Getter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

import static org.apache.logging.log4j.LogManager.getLogger;

@Getter
public class ChallengeManager {

  private static ChallengeManager instance;

  private final ArrayList<RegisteredModifier> registeredModifiers = new ArrayList<>();
  private final HashMap<UUID, PlayerInChallenge> playersInChallenges = new HashMap<>();
  private final HashMap<UUID, ChallengeInvite> pendingInvites = new HashMap<>();

  private ChallengeManager() {
  }

  public static synchronized ChallengeManager getInstance() {
    if (instance == null) {
      instance = new ChallengeManager();
    }
    return instance;
  }

  public void registerNewChallenge(Challenge challenge, Player creator) {
    playersInChallenges.put(creator.getUniqueId(), new PlayerInChallenge(challenge, creator));
  }

  public void registerPlayersInChallenge(Challenge challenge, Collection<Player> players) {
    players.forEach(p -> {
      playersInChallenges.put(p.getUniqueId(), new PlayerInChallenge(challenge, p));
      challenge.join(p);
    });
  }

  public void registerModifier(RegisteredModifier registeredModifier) {
    registeredModifiers.add(registeredModifier);
  }

  public boolean canCreateChallenge(Player player) {
    return !playersInChallenges.containsKey(player.getUniqueId());
  }

  public void leaveChallenge(Player player) {
    var playerInChallenge = playersInChallenges.remove(player.getUniqueId());
    if (playerInChallenge == null) {
      player.sendRichMessage("<red>You are not partaking in any challenges!");
      return;
    }

    playerInChallenge.getChallenge().leave(player);
    player.sendRichMessage("<gold>Successfully left the challenge!");
  }

  public void invitePlayerToChallenge(Player invitee, Player invited) {
    var playerInChallenge = playersInChallenges.get(invitee.getUniqueId());
    if (playerInChallenge == null) {
      getLogger().warn("invitePlayerToChallenge called with a player that was not in a challenge!");
      return;
    }
    // TODO: Allow multiple invites per player
    pendingInvites.put(
        invited.getUniqueId(),
        new ChallengeInvite(
            invitee.getUniqueId(), invited.getUniqueId(), playerInChallenge.getChallenge())
    );

    Bukkit.getScheduler().runTaskLater(
        JavaPlugin.getPlugin(ChallengeAPI.class), () -> {
          var pendingInvite = pendingInvites.get(invited.getUniqueId());
          if (pendingInvite != null && pendingInvite.invitee() == invited.getUniqueId()) {
            pendingInvites.remove(invited.getUniqueId());
            invited.sendRichMessage(
                "<gold>The invite from <dark_red><invitee></dark_red> has expired!",
                Placeholder.component("invitee", invitee.name())
            );
          }
        }, 20 * 60L
    );
  }

  public void acceptInviteToChallenge(Player invited) {
    var invite = pendingInvites.remove(invited.getUniqueId());
    if (invite == null) {
      invited.sendRichMessage("<red>No invite found! Maybe it expired?");
      return;
    }
    var invitee = Bukkit.getPlayer(invite.invitee());
    if (invitee == null) {
      invited.sendRichMessage("<red>The player who invited you went offline!");
      return;
    }

    if (playersInChallenges.containsKey(invited.getUniqueId())) {
      invited.sendRichMessage("<red>You are already partaking in another challenge!");
      return;
    }

    registerPlayersInChallenge(invite.challenge(), List.of(invited));

    invited.sendRichMessage(
        "<gold>Accepted the invite from <dark_red><invitee></dark_red>!",
        Placeholder.component("invitee", Bukkit.getPlayer(invite.invitee()).name())
    );

    invitee.sendRichMessage(
        "<dark_red><invited></dark_red><gold> accepted your invite!",
        Placeholder.component("invited", invited.name())
    );
  }

  public void startChallenge(Player creator) {
    var playerInChallenge = playersInChallenges.get(creator.getUniqueId());
    if (playerInChallenge == null) {
      creator.sendRichMessage("<red>You are not partaking in a challenge!");
      return;
    }

    if (playerInChallenge.getChallenge().getCreatorUUID() != creator.getUniqueId()) {
      creator.sendRichMessage("<red>Only a challenges creator can start it!");
      return;
    }

    playerInChallenge.getChallenge().start();
  }
}
