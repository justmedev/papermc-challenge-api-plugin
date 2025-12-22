package at.iljabusch.challengeAPI.challenges.events;

import at.iljabusch.challengeAPI.Challenge;

public class ChallengeCompleteEvent extends ChallengeEvent{
    public ChallengeCompleteEvent(Challenge challenge) {
        super(challenge);
    }
}
