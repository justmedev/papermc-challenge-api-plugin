package at.iljabusch.challengeAPI;

import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;

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
}
