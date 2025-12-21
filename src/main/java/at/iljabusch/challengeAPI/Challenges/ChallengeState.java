package at.iljabusch.challengeAPI.Challenges;

public enum ChallengeState {
  PREPARING,
  READY,
  ONGOING,
  COMPLETED,
  FAILED;

  boolean isOngoingOrCompleted() {
    return this == ONGOING || this == COMPLETED || this == FAILED;
  }
}
