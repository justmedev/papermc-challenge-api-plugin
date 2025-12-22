package at.iljabusch.challengeAPI.modifiers;

import at.iljabusch.challengeAPI.challenges.Challenge;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Material;

import java.lang.reflect.InvocationTargetException;

import static org.apache.logging.log4j.LogManager.getLogger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisteredModifier {
  String name;
  String author;
  Material displayItem;
  Class<? extends Modifier> modifier;


  public Modifier createModifierInstance(Challenge challenge) {
    try {
      return modifier.getDeclaredConstructor(Challenge.class).newInstance(challenge);
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      getLogger().warn("Could not instantiate {} starting challenge {} without!", modifier.getName(), challenge.getWorldUUID());
      getLogger().warn(e.getMessage());
      return null;
    }
  }
}
