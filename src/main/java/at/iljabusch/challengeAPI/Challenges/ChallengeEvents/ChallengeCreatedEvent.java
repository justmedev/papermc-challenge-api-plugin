package at.iljabusch.challengeAPI.Challenges.ChallengeEvents;

import at.iljabusch.challengeAPI.Challenges.Challenge;

public class ChallengeCreatedEvent extends ChallengeEvent {
    public ChallengeCreatedEvent(Challenge challenge) {
        super(challenge);
    }
}
