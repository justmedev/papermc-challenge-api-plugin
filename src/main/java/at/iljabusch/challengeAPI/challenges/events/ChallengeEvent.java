package at.iljabusch.challengeAPI.challenges.events;

import at.iljabusch.challengeAPI.challenges.Challenge;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public abstract class ChallengeEvent extends Event implements Cancellable {
  private static final HandlerList handlers = new HandlerList();
  private final Challenge challenge;
  private boolean cancelled;

  public ChallengeEvent(Challenge challenge) {
    this.challenge = challenge;
  }

  public static HandlerList getHandlerList() {
    return handlers;
  }

  public boolean isCancelled() {
    return cancelled;
  }

  public void setCancelled(boolean cancel) {
    cancelled = cancel;
  }

  public Challenge getChallenge() {
    return challenge;
  }

  public HandlerList getHandlers() {
    return handlers;
  }
}
