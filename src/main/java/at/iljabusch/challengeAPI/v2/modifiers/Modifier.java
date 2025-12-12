package at.iljabusch.challengeAPI.v2.modifiers;

import at.iljabusch.challengeAPI.v2.Challenge;
import lombok.Getter;
import org.bukkit.entity.Player;

public abstract class Modifier {
  @Getter
  protected Challenge challenge;

  Modifier(Challenge challenge) {
    this.challenge = challenge;
  }

  abstract String getName();

  abstract String getAuthor();

  abstract void onPlayerJoin(Player player);
}
