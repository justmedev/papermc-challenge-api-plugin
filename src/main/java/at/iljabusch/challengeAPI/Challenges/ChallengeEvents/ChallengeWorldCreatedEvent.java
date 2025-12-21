package at.iljabusch.challengeAPI.Challenges.ChallengeEvents;

import at.iljabusch.challengeAPI.Challenges.Challenge;

public class ChallengeWorldCreatedEvent extends ChallengeEvent{
    public ChallengeWorldCreatedEvent(Challenge challenge) {
        super(challenge);
    }
}
