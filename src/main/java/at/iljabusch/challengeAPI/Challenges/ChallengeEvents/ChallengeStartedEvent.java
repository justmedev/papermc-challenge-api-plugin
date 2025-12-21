package at.iljabusch.challengeAPI.Challenges.ChallengeEvents;


import at.iljabusch.challengeAPI.Challenges.Challenge;

public class ChallengeStartedEvent extends ChallengeEvent {
    public ChallengeStartedEvent(Challenge challenge) {
        super(challenge);
    }
}
