package at.iljabusch.challengeAPI.challenges.events;

import at.iljabusch.challengeAPI.challenges.Challenge;
import lombok.Getter;
import org.bukkit.World;

@Getter
public class ChallengeWorldLoadedEvent extends ChallengeEvent {
  private final World world;

  public ChallengeWorldLoadedEvent(Challenge challenge, World world) {
    super(challenge);
    this.world = world;
  }

}
