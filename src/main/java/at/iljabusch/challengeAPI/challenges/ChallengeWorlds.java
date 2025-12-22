package at.iljabusch.challengeAPI.challenges;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.World;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChallengeWorlds {
  World normal;
  World nether;
  World theEnd;
}
