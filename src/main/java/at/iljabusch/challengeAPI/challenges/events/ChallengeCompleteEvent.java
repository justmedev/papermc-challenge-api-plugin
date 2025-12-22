package at.iljabusch.challengeAPI.challenges.events;

import at.iljabusch.challengeAPI.challenges.Challenge;

public class ChallengeCompleteEvent extends ChallengeEvent {
  public ChallengeCompleteEvent(Challenge challenge) {
    super(challenge);
  }
}
