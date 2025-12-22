package at.iljabusch.challengeAPI.challenges.events;

import at.iljabusch.challengeAPI.challenges.Challenge;

public class ChallengeCreatedEvent extends ChallengeEvent {
    public ChallengeCreatedEvent(Challenge challenge) {
        super(challenge);
    }
}
