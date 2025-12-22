package at.iljabusch.challengeAPI.modifiers;

import at.iljabusch.challengeAPI.challenges.Challenge;
import lombok.Getter;

public abstract class Modifier {

  @Getter
  protected Challenge challenge;

  protected Modifier(Challenge challenge) {
    this.challenge = challenge;
  }

}
