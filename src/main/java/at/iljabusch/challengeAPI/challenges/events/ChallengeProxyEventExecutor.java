package at.iljabusch.challengeAPI.challenges.events;

import at.iljabusch.challengeAPI.challenges.Challenge;
import org.bukkit.Bukkit;
import org.bukkit.event.*;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.world.WorldEvent;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChallengeProxyEventExecutor implements EventExecutor, Listener {
  private final Challenge challenge;
  private final Listener userEventListener;
  private final PluginManager pluginmanager = Bukkit.getPluginManager();
  private final Plugin plugin;
  private final ChallengeEventExecutorProxyInfo executorInfo;
  private final Map<Class<? extends Event>, List<Method>> registeredMethods = new HashMap<>();

  public ChallengeProxyEventExecutor(Challenge challenge, Plugin plugin, Listener userListener) {
    this.challenge = challenge;
    this.plugin = plugin;
    this.userEventListener = userListener;
    this.executorInfo = null;
    registerUserListener();
  }

  public ChallengeProxyEventExecutor(Class<? extends Event> event, Challenge challenge, Plugin plugin, Listener userListener, EventPriority priority, EventExecutor executor) {
    this(event, challenge, plugin, userListener, priority, executor, false);
  }

  public ChallengeProxyEventExecutor(Class<? extends Event> event, Challenge challenge, Plugin plugin, EventPriority priority, EventExecutor executor) {
    this(event, challenge, plugin, null, priority, executor, false);
  }

  public ChallengeProxyEventExecutor(Class<? extends Event> event, Challenge challenge, Plugin plugin, Listener userListener, EventPriority priority, EventExecutor executor, boolean ignoreCancelled) {
    this.challenge = challenge;
    this.plugin = plugin;
    this.userEventListener = userListener;
    this.executorInfo = new ChallengeEventExecutorProxyInfo(
        event,
        executor,
        priority,
        ignoreCancelled
    );
    registerExecutor();
  }
  private void registerExecutor(){
    pluginmanager.registerEvent(
        executorInfo.event(),
        this,
        executorInfo.eventPriority(),
        this,
        plugin,
        executorInfo.ignoreCancelled()
    );
  }
  private void registerUserListener(){
    addUserListenerMethods();

    for (Class<? extends Event> event : registeredMethods.keySet()) {
      for (Method method : registeredMethods.get(event)) {
        EventHandler eh = method.getAnnotation(EventHandler.class);
        pluginmanager.registerEvent(
            event,
            this,
            eh.priority(),
            this,
            plugin,
            eh.ignoreCancelled()
        );
      }
    }
  }

  private void addUserListenerMethods() {
    for (Method method : userEventListener.getClass().getMethods()) {
      EventHandler eh = method.getAnnotation(EventHandler.class);
      if (eh != null && method.getParameterTypes().length == 1) {
        Class<?> paramType = method.getParameterTypes()[0];
        if (Event.class.isAssignableFrom(paramType)) {
          List<Method> methods = registeredMethods.get(paramType.asSubclass(Event.class));
          if (methods == null) {
            registeredMethods.put(paramType.asSubclass(Event.class), List.of(method));
          } else {
            methods.add(method);
          }
        }
      }
    }
  }

  @Override
  public void execute(@NotNull Listener listener, @NotNull Event event) throws EventException {
    if (!isEventAffiliated(event)) return;


    if (executorInfo != null) {
      executorInfo.executor().execute(listener, event);
    }

    if (!registeredMethods.containsKey(event.getClass())) return;

    for (Method method : registeredMethods.get(event.getClass())) {
      method.setAccessible(true);
      try {
        method.invoke(userEventListener, event);
      } catch (IllegalAccessException | InvocationTargetException e) {
        throw new RuntimeException(e);
      }
    }

  }

  private boolean isEventAffiliated(@NotNull Event event) {
    if(event instanceof ChallengeEvent){
      if(((ChallengeEvent) event).getChallenge().equals(challenge))return true;
    }
    if (event instanceof PlayerEvent) {
      if (challenge.getPlayerUUIDs().contains(((PlayerEvent) event).getPlayer().getUniqueId())) return true;
    }
    if (event instanceof EntityEvent) {
      if (((EntityEvent) event).getEntity().getWorld().getName().contains(challenge.getWorldUUID().toString())) return true;
    }
    if (event instanceof BlockEvent) {
      if (((BlockEvent) event).getBlock().getWorld().getName().contains(challenge.getWorldUUID().toString())) return true;
    }
    if (event instanceof WorldEvent) {
      if (((WorldEvent) event).getWorld().getName().contains(challenge.getWorldUUID().toString())) return true;
    }
    if (event instanceof InventoryEvent) {
      return ((InventoryEvent) event).getInventory().getLocation().getWorld().getName().contains(challenge.getWorldUUID().toString());
    }
    return false;
  }
}
