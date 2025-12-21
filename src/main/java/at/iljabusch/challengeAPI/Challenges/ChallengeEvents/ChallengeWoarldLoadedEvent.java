package at.iljabusch.challengeAPI.Challenges.ChallengeEvents;

import at.iljabusch.challengeAPI.Challenge;
import org.bukkit.World;

public class ChallengeWoarldLoadedEvent extends ChallengeEvent {
    private final World world;
    public ChallengeWoarldLoadedEvent(Challenge challenge, World world) {
        super(challenge);
        this.world = world;
    }

    public World getWorld() {
       return world;
    }
}
