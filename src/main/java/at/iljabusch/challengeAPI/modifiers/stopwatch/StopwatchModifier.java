package at.iljabusch.challengeAPI.modifiers.stopwatch;

import at.iljabusch.challengeAPI.ChallengeAPI;
import at.iljabusch.challengeAPI.challenges.Challenge;
import at.iljabusch.challengeAPI.challenges.events.ChallengeStartedEvent;
import at.iljabusch.challengeAPI.modifiers.Modifier;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class StopwatchModifier extends Modifier {

  private double secondsPlayed = 0;
  private BukkitTask task;

  public StopwatchModifier(Challenge challenge) {
    super(challenge);
    challenge.registerEvent(
        ChallengeStartedEvent.class,
        null,
        EventPriority.NORMAL,
        (listener, event) -> {
          startTask();
        },
        ChallengeAPI.getPlugin(ChallengeAPI.class)
    );

    challenge.registerEvent(
        ChallengeStartedEvent.class,
        null,
        EventPriority.NORMAL,
        (listener, event) -> {
          if (task.isCancelled()) {
            startTask();
          }
        },
        ChallengeAPI.getPlugin(ChallengeAPI.class)
    );

    challenge.registerEvent(
        ChallengeStartedEvent.class,
        null,
        EventPriority.NORMAL,
        (listener, event) -> {
          if (!challenge.getOnlinePlayers().isEmpty()) {
            return;
          }
          task.cancel();
        },
        ChallengeAPI.getPlugin(ChallengeAPI.class)
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
