package at.iljabusch.challengeAPI.challenges.events;

import at.iljabusch.challengeAPI.challenges.Challenge;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

@Getter
public abstract class ChallengeEvent extends Event implements Cancellable {
  private static final HandlerList handlers = new HandlerList();
  private final Challenge challenge;
  @Setter
  private boolean cancelled;

  public ChallengeEvent(Challenge challenge) {
    this.challenge = challenge;
  }

  public static @NonNull HandlerList getHandlerList() {
    return handlers;
  }

  @Override
  public @NotNull HandlerList getHandlers() {
    return handlers;
  }
}
