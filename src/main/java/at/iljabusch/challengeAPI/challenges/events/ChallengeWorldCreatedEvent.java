package at.iljabusch.challengeAPI.challenges.events;

import at.iljabusch.challengeAPI.challenges.Challenge;

public class ChallengeWorldCreatedEvent extends ChallengeEvent{
    public ChallengeWorldCreatedEvent(Challenge challenge) {
        super(challenge);
    }
}
