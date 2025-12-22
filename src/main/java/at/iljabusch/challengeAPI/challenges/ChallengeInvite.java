package at.iljabusch.challengeAPI.challenges;

import java.util.UUID;

public record ChallengeInvite(
    UUID invitee,
    UUID invited,
    Challenge challenge
) {

}
