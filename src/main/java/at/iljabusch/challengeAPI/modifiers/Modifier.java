package at.iljabusch.challengeAPI.modifiers;

import at.iljabusch.challengeAPI.Challenges.Challenge;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;

public abstract class Modifier {

  @Getter
  protected Challenge challenge;

  protected Modifier(Challenge challenge) {
    this.challenge = challenge;
  }

}
