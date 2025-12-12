package at.iljabusch.challengeAPI.modifiers;

import at.iljabusch.challengeAPI.Challenge;
import lombok.Getter;
import org.bukkit.entity.Player;

public abstract class Modifier {
  @Getter
  protected Challenge challenge;

  protected Modifier(Challenge challenge) {
    this.challenge = challenge;
  }

  public abstract void onPlayerJoin(Player player);
}
