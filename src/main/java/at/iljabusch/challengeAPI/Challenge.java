package at.iljabusch.challengeAPI;

import java.util.List;
import org.bukkit.entity.Player;

public interface Challenge {
  void onCreate(List<Player> players) throws ChallengeCreationException;
  void onJoin(Player player);
  void onLeave(Player player);
}
