package at.iljabusch.challengeAPI.Challenges;

import at.iljabusch.challengeAPI.Challenge;

import java.util.UUID;

public record ChallengeInvite(
    UUID invitee,
    UUID invited,
    Challenge challenge
) {

}
