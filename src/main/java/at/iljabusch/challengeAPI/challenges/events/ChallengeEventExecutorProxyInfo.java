package at.iljabusch.challengeAPI.challenges.events;

import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.EventExecutor;

public record ChallengeEventExecutorProxyInfo(Class<? extends Event> event, EventExecutor executor, EventPriority eventPriority, boolean ignoreCancelled) {
}
