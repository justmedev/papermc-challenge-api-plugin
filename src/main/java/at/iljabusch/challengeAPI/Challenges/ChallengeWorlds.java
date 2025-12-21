package at.iljabusch.challengeAPI.Challenges;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.World;
import org.mvplugins.multiverse.core.world.MultiverseWorld;

@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChallengeWorlds {
  World normal;
  World nether;
  World theEnd;
}
