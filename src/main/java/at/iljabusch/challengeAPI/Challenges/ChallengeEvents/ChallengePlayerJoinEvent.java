package at.iljabusch.challengeAPI.Challenges.ChallengeEvents;

import at.iljabusch.challengeAPI.Challenges.Challenge;
import org.bukkit.entity.Player;

public class ChallengePlayerJoinEvent extends ChallengeEvent {
    private final Player player;
    public ChallengePlayerJoinEvent(Challenge challenge, Player player) {
        super(challenge);
        this.player = player;
    }

    public Player getPlayer() {
       return  player;
    }


}
