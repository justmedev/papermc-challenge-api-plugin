package at.iljabusch.challengeAPI;

import at.iljabusch.challengeAPI.v2.Challenge;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

@AllArgsConstructor
@Getter
@Setter
public class PlayerInChallenge {

  private Challenge challenge;
  private Player player;
}
