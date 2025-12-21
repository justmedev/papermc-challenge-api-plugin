package at.iljabusch.challengeAPI.Challenges.ChallengeEvents;

import at.iljabusch.challengeAPI.Challenge;

public class ChallengeCompleteEvent extends ChallengeEvent{
    public ChallengeCompleteEvent(Challenge challenge) {
        super(challenge);
    }
}
