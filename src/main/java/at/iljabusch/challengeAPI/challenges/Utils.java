package at.iljabusch.challengeAPI.challenges;

import at.iljabusch.challengeAPI.ChallengeAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Iterator;

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


  static boolean deleteBukkitWorld(World world) {
    //TODO: actully do this skibedy
    Bukkit.unloadWorld(world, false);

    File folder = new File(Bukkit.getWorldContainer(), world.getName());

    if (folder.exists()) {
      File[] files = folder.listFiles();
      if (files != null) {
        for (File file : files) {
          if (file.isDirectory()) {
          } else {
            file.delete();
          }
        }
      }
      return folder.delete();
    }
    return true;
  }
}
