package at.iljabusch.challengeAPI.modifiers.WorldModifiers.PresetWorldModifiers;

import at.iljabusch.challengeAPI.Challenges.Challenge;
import at.iljabusch.challengeAPI.Challenges.ChallengeEvents.ChallengeCreatedEvent;
import at.iljabusch.challengeAPI.Challenges.ChallengeEvents.ChallengeWorldCreatedEvent;
import org.bukkit.WorldCreator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.concurrent.atomic.AtomicReference;

public class ConfiguredWorldModifierListener implements Listener {
    private final WorldModifierConfig config;
    private AtomicReference<WorldCreator> worldCreator;

    public ConfiguredWorldModifierListener(WorldModifierConfig config) {
        this.config = config;
    }

    @EventHandler
    public void onChallengeCreated(ChallengeCreatedEvent event){
       Challenge challenge = event.getChallenge();

       worldCreator = challenge.getOverworldCreator();
       //TODO: create and register WorldCreator
    }

    @EventHandler
    public void onChallengeWorldCreated(ChallengeWorldCreatedEvent event){
       //TODO: apply gamerules and other configs to the World
    }

}
