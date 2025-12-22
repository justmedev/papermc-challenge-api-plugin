package at.iljabusch.challengeAPI.modifiers.stopwatch;

import at.iljabusch.challengeAPI.ChallengeAPI;
import at.iljabusch.challengeAPI.challenges.Challenge;
import at.iljabusch.challengeAPI.challenges.events.ChallengePlayerJoinEvent;
import at.iljabusch.challengeAPI.challenges.events.ChallengePlayerLeaveEvent;
import at.iljabusch.challengeAPI.challenges.events.ChallengeStartedEvent;
import at.iljabusch.challengeAPI.modifiers.Modifier;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.jspecify.annotations.NonNull;

public class StopwatchModifier extends Modifier implements Listener {

  private double secondsPlayed = 0;
  private BukkitTask task;

  public StopwatchModifier(@NonNull Challenge challenge) {
    super(challenge);

    challenge.getEventEmitter().registerEvent(
        ChallengeStartedEvent.class,
        (listener, event) -> startTask(),
        JavaPlugin.getPlugin(ChallengeAPI.class)
    );

    challenge.getEventEmitter().registerEvent(
        ChallengePlayerJoinEvent.class,
        (listener, event) -> {
          if (task.isCancelled()) {
            startTask();
          }
        },
        JavaPlugin.getPlugin(ChallengeAPI.class)
    );

    challenge.getEventEmitter().registerEvent(
        ChallengePlayerLeaveEvent.class,
        (listener, event) -> {
          if (!challenge.getOnlinePlayers().isEmpty()) {
            return;
          }
          task.cancel();
        },
        JavaPlugin.getPlugin(ChallengeAPI.class)
    );

  }

  public void startTask() {
    task = Bukkit.getScheduler().runTaskTimer(
        JavaPlugin.getPlugin(ChallengeAPI.class), () -> {
          secondsPlayed++;

          challenge.getOnlinePlayers().forEach(player -> {
            var min = (int) Math.floor(secondsPlayed / 60);
            var s = Integer.toString((int) Math.floor(secondsPlayed % 60));
            player.sendActionBar(
                MiniMessage.miniMessage().deserialize(
                    "<green><bold><min> <sec>s",
                    Placeholder.unparsed("min", min <= 0 ? "" : min + "min"),
                    Placeholder.unparsed("sec", s)
                ));
          });
        }, 0L, 20L /* 1s */
    );
  }
}
