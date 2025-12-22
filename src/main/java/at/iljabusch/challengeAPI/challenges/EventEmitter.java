package at.iljabusch.challengeAPI.challenges;

import at.iljabusch.challengeAPI.challenges.events.ChallengeProxyEventExecutor;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.HashSet;
import java.util.Set;

public class EventEmitter {
  // TODO: let modifiers add a Listener directly to the challenge to check automatically if an event is associated with a challenge or not

  private final @NonNull Challenge challenge;
  private final Set<ChallengeProxyEventExecutor> challengeProxyEventExecutors = new HashSet<>();

  EventEmitter(@NonNull Challenge challenge) {
    this.challenge = challenge;
  }

  /**
   * Registers all the events in the given listener class
   *
   * @param listener Listener to register
   * @param plugin   Plugin to register
   */
  public void registerEvents(@NotNull Listener listener, @NotNull Plugin plugin) {
    challengeProxyEventExecutors.add(new ChallengeProxyEventExecutor(challenge, plugin, listener));
  }

  /**
   * Register an executor with the event type `event`
   *
   * @param event    Event type to register
   * @param executor EventExecutor to register
   * @param plugin   Plugin to register
   */
  public void registerEvent(@NotNull Class<? extends Event> event, @NotNull EventExecutor executor, @NotNull Plugin plugin) {
    challengeProxyEventExecutors.add(new ChallengeProxyEventExecutor(event, challenge, plugin, null, executor));
  }

  /**
   * Registers the specified executor to the given event class
   *
   * @param event    Event type to register
   * @param listener Listener to register
   * @param priority Priority to register this event at
   * @param executor EventExecutor to register
   * @param plugin   Plugin to register
   */
  public void registerEvent(@NotNull Class<? extends Event> event, @NotNull Listener listener, @NotNull EventPriority priority, @NotNull EventExecutor executor, @NotNull Plugin plugin) {
    challengeProxyEventExecutors.add(new ChallengeProxyEventExecutor(event, challenge, plugin, listener, priority, executor));
  }

  /**
   * Registers the specified executor to the given event class
   *
   * @param event           Event type to register
   * @param listener        Listener to register
   * @param priority        Priority to register this event at
   * @param executor        EventExecutor to register
   * @param plugin          Plugin to register
   * @param ignoreCancelled Whether to pass cancelled events or not
   */
  public void registerEvent(
      @NotNull Class<? extends Event> event,
      @NotNull Listener listener,
      @NotNull EventPriority priority,
      @NotNull EventExecutor executor,
      @NotNull Plugin plugin,
      boolean ignoreCancelled
  ) {
    challengeProxyEventExecutors.add(new ChallengeProxyEventExecutor(event, challenge, plugin, listener, priority, executor, ignoreCancelled));
  }

}
