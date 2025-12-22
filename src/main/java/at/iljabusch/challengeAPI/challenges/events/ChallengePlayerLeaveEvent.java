package at.iljabusch.challengeAPI.challenges.events;

import at.iljabusch.challengeAPI.challenges.Challenge;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class ChallengePlayerLeaveEvent extends ChallengePlayerEvent {
  public ChallengePlayerLeaveEvent(Challenge challenge, Player player) {
    super(challenge, player);
  }
}
