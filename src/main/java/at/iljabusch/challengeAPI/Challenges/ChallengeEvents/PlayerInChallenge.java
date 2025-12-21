package at.iljabusch.challengeAPI.Challenges.ChallengeEvents;

import at.iljabusch.challengeAPI.Challenges.Challenge;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

@AllArgsConstructor
@Getter
@Setter
public class PlayerInChallenge {

  private Challenge challenge;
  private Player player;
}
