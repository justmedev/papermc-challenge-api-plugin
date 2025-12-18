package at.iljabusch.challengeAPI;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.mvplugins.multiverse.core.world.MultiverseWorld;

@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChallengeWorlds {

  MultiverseWorld normal;
  MultiverseWorld nether;
  MultiverseWorld theEnd;
}
