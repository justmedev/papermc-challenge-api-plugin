package at.iljabusch.challengeAPI.challenges.events;

import at.iljabusch.challengeAPI.challenges.Challenge;
import org.bukkit.World;

public class ChallengeWorldEvent extends ChallengeEvent {

  private final World world;

  public ChallengeWorldEvent(Challenge challenge, World world) {
    super(challenge);
    this.world = world;
  }

  public World getWorld() {
    return world;
  }
}
