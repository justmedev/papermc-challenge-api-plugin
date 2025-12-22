package at.iljabusch.challengeAPI.challenges.events;


import at.iljabusch.challengeAPI.challenges.Challenge;

public class ChallengeStartedEvent extends ChallengeEvent {
    public ChallengeStartedEvent(Challenge challenge) {
        super(challenge);
    }
}
