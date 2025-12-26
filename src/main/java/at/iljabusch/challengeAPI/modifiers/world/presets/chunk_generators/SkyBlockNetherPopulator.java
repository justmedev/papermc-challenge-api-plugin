package org.worldGeneratorTest.worldGeneratorTest;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.block.Chest;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class SkyBlockNetherPopulator extends BlockPopulator {
    @Override
    public void populate(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, LimitedRegion limitedRegion) {
        if (chunkX == 0 && chunkZ == 0) {
            int x = 2, y = 68, z = 2;

            var state = limitedRegion.getBlockState(x, y, z);
            if (state instanceof Chest chest) {
                Inventory inv = chest.getSnapshotInventory();
                inv.addItem(new ItemStack(Material.BIRCH_SAPLING));
                inv.addItem(new ItemStack(Material.SUGAR_CANE));
                inv.addItem(new ItemStack(Material.ICE));
            }
            state.update();
        }

    }
}
