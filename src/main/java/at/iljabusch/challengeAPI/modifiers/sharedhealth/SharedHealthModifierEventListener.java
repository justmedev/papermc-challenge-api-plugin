package at.iljabusch.challengeAPI.modifiers.sharedhealth;

import at.iljabusch.challengeAPI.ChallengeAPI;
import at.iljabusch.challengeAPI.ChallengeState;
import at.iljabusch.challengeAPI.challenges.events.ChallengePlayerJoinEvent;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.jspecify.annotations.NonNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class SharedHealthModifierEventListener implements Listener {

  private final SharedHealthModifier modifier;
  private final ChallengeAPI plugin = JavaPlugin.getPlugin(ChallengeAPI.class);

  // Guard set to prevent infinite event loops
  private final Set<UUID> isSyncing = new HashSet<>();

  public SharedHealthModifierEventListener(SharedHealthModifier modifier) {
    this.modifier = modifier;
  }

  public boolean isPlayerNotPartOfActiveChallenge(Player player) {
    return !modifier.getChallenge().getPlayerUUIDs().contains(player.getUniqueId())
        || modifier.getChallenge().getState() != ChallengeState.ONGOING;
  }


  void syncAll(@NonNull Player source) {
    double health = source.getHealth();
    double absorption = source.getAbsorptionAmount();
    int food = source.getFoodLevel();
    float saturation = source.getSaturation();

    for (Player p : modifier.getChallenge().getOnlinePlayers()) {
      if (p.getUniqueId().equals(source.getUniqueId())) {
        continue;
      }

      // Add to guard set before making changes; This prevents recursive calling
      isSyncing.add(p.getUniqueId());

      try {
        p.setHealth(Math.min(health, p.getAttribute(Attribute.MAX_HEALTH).getValue()));
        p.setAbsorptionAmount(absorption);
        p.setFoodLevel(food);
        p.setSaturation(saturation);

        for (PotionEffect effect : source.getActivePotionEffects()) {
          p.addPotionEffect(effect);
        }
      } finally {
        isSyncing.remove(p.getUniqueId());
      }
    }
  }

  @EventHandler(ignoreCancelled = true)
  public void onDamage(EntityDamageEvent event) {
    if (!(event.getEntity() instanceof Player p) || isSyncing.contains(p.getUniqueId())) {
      return;
    }
    if (isPlayerNotPartOfActiveChallenge(p)) {
      return;
    }

    if (p.getHealth() - event.getFinalDamage() <= 0) {
      event.setCancelled(true);
      modifier.getChallenge().complete(false);
      return;
    }

    plugin.getServer().getScheduler().runTask(plugin, () -> syncAll(p));
  }

  @EventHandler(ignoreCancelled = true)
  public void onRegen(EntityRegainHealthEvent event) {
    if (!(event.getEntity() instanceof Player p) || isSyncing.contains(p.getUniqueId())) {
      return;
    }
    if (isPlayerNotPartOfActiveChallenge(p)) {
      return;
    }

    plugin.getServer().getScheduler().runTask(plugin, () -> syncAll(p));
  }

  @EventHandler(ignoreCancelled = true)
  public void onFoodChange(Listener listener, FoodLevelChangeEvent event) {
    if (!(event.getEntity() instanceof Player p) || isSyncing.contains(p.getUniqueId())) {
      return;
    }
    if (isPlayerNotPartOfActiveChallenge(p)) {
      return;
    }

    plugin.getServer().getScheduler().runTask(plugin, () -> syncAll(p));
  }

  @EventHandler(ignoreCancelled = true)
  public void onPotion(EntityPotionEffectEvent event) {
    if (!(event.getEntity() instanceof Player p) || isSyncing.contains(p.getUniqueId())) {
      return;
    }
    if (isPlayerNotPartOfActiveChallenge(p)) {
      return;
    }

    plugin.getServer().getScheduler().runTask(plugin, () -> syncAll(p));
  }

  @EventHandler(ignoreCancelled = true)
  public void onDragonDeath(EntityDeathEvent event) {
    if (!(event.getEntity() instanceof EnderDragon dragon)) {
      return;
    }
    if (!event.getEntity().getWorld().getName()
              .equals(modifier.getChallenge().getWorlds().getTheEnd().getName())) {
      return;
    }

    modifier.getChallenge().complete(true);
  }

  @EventHandler
  public void onPlayerJoin(ChallengePlayerJoinEvent event) {
    var possibleSource = event.getChallenge().getOnlinePlayers().getFirst();
    if (possibleSource.equals(event.getPlayer())) {
      return;
    }
    this.syncAll(possibleSource);
  }
}
