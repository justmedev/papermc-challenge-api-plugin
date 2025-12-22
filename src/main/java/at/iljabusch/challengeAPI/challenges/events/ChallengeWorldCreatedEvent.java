package at.iljabusch.challengeAPI.challenges.events;

import at.iljabusch.challengeAPI.challenges.Challenge;
import org.bukkit.World;

public class ChallengeWorldCreatedEvent extends ChallengeWorldEvent {
  public ChallengeWorldCreatedEvent(Challenge challenge, World world) {
    super(challenge, world);
  }
}
