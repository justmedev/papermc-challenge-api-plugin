package at.iljabusch.challengeAPI.modifiers.portal_link;

import at.iljabusch.challengeAPI.ChallengeAPI;
import at.iljabusch.challengeAPI.challenges.Challenge;
import at.iljabusch.challengeAPI.modifiers.Modifier;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This is a Modifier that gets added to every challenge by default and cannot be registered.
 * Any modifier that disables portals or has some custom logic relating to portals can unregister it in its Constructor.
 */
public class PortalLinkModifier extends Modifier {


  public PortalLinkModifier(Challenge challenge) {
    super(challenge);
    challenge.getEventEmitter().registerEvents(new PortalLinkListener(challenge), JavaPlugin.getProvidingPlugin(ChallengeAPI.class));
  }
}
