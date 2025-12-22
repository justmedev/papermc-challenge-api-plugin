package at.iljabusch.challengeAPI.challenges;

import at.iljabusch.challengeAPI.ChallengeAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NonNull;

import java.io.File;
import java.util.Iterator;

public class Utils {

  static void resetPlayerAdvancements(@NonNull Player player) {
    Iterator<Advancement> iterator = Bukkit.getServer().advancementIterator();
    while (iterator.hasNext()) {
      AdvancementProgress progress = player.getAdvancementProgress(iterator.next());
      for (String criteria : progress.getAwardedCriteria()) {
        progress.revokeCriteria(criteria);
      }
    }
  }

  static void waitForChunksLoaded(@NonNull Player player, @NonNull Location loc, @NonNull Runnable onChunksLoaded) {
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

  static boolean deleteBukkitWorld(@NonNull World world) {
    Bukkit.unloadWorld(world, false);

    var folder = new File(Bukkit.getWorldContainer(), world.getName());
    if (folder.exists()) return folder.delete();
    return true;
  }
}
