package at.iljabusch.challengeAPI.modifiers.portal_link;

import at.iljabusch.challengeAPI.challenges.Challenge;
import org.bukkit.Location;
import org.bukkit.PortalType;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PortalLinkListener implements Listener {
  private final Challenge challenge;

  public PortalLinkListener(Challenge challenge) {
    this.challenge = challenge;
  }

  @EventHandler
  public void onPlayerPortal(PlayerPortalEvent event) {
    event.setTo(getLocation(event.getTo()));
  }

 @EventHandler
 public void onEntityPortal(EntityPortalEvent event) {
    if(event.getTo() == null) return;

    event.setTo(getLocation(event.getTo()));
 }

 private Location getLocation(Location location) {
    switch(location.getWorld().getEnvironment()) {
      case THE_END -> location.setWorld(challenge.getWorlds().getTheEnd());
      case NETHER -> location.setWorld(challenge.getWorlds().getNether());
      case NORMAL -> location.setWorld(challenge.getWorlds().getNormal());
    }
    return location;
 }
}

