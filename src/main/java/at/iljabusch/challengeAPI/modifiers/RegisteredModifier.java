package at.iljabusch.challengeAPI.modifiers;

import org.bukkit.Material;

public record RegisteredModifier(String name, String author, Material displayItem,
                                 Class<? extends Modifier> modifier) {

}
