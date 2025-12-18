package at.iljabusch.challengeAPI;

public enum ChallengeState {
  PREPARING,
  READY,
  ONGOING,
  COMPLETED,
  FAILED;

  boolean hasStartedOrCompleted() {
    return this == ONGOING || this == COMPLETED || this == FAILED;
  }
}
