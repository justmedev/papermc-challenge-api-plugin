package at.iljabusch.challengeAPI;

import java.util.UUID;

public record ChallengeInvite(
    UUID invitee,
    UUID invited,
    Challenge challenge
) {

}
