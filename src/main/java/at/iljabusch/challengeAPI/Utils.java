package at.iljabusch.challengeAPI;

import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Utils {

  static void resetPlayerAdvancements(Player player) {
    Iterator<Advancement> iterator = Bukkit.getServer().advancementIterator();
    while (iterator.hasNext()) {
      AdvancementProgress progress = player.getAdvancementProgress(iterator.next());
      for (String criteria : progress.getAwardedCriteria()) {
        progress.revokeCriteria(criteria);
      }
    }
  }

  static void waitForChunksLoaded(Player player, Location loc, Runnable onChunksLoaded) {
    if (loc.getChunk().isLoaded() && player.isChunkSent(loc.getChunk())) {
      onChunksLoaded.run();
    } else {
      // Re-check in 5 ticks
      Bukkit.getScheduler().runTaskLater(
          JavaPlugin.getPlugin(ChallengeAPI.class),
          () -> waitForChunksLoaded(player, loc, onChunksLoaded),
          5L
      );
    }
  }
}
