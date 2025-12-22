package at.iljabusch.challengeAPI.challenges.events;

import at.iljabusch.challengeAPI.challenges.Challenge;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class ChallengePlayerJoinEvent extends ChallengePlayerEvent {
  public ChallengePlayerJoinEvent(Challenge challenge, Player player) {
    super(challenge, player);
  }
}
