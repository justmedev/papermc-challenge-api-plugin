package at.iljabusch.challengeAPI.modifiers;

import at.iljabusch.challengeAPI.challenges.Challenge;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.lang.reflect.InvocationTargetException;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisteredModifier {
  String name;
  String author;
  Material displayItem;
  Class<? extends Modifier> modifier;


  public <G extends Modifier> G createModifierInstance(Challenge challenge) {
    try {
      Modifier modiierInstance = modifier.getDeclaredConstructor(Challenge.class).newInstance(challenge);
      return (G) modiierInstance;
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      Bukkit.getLogger().warning("Could not instantiate " + modifier.getName() + " starting challenge " + challenge.getWorldUUID() + " without!");
      Bukkit.getLogger().warning(e.getMessage());
      return null;
    }
  }
}
