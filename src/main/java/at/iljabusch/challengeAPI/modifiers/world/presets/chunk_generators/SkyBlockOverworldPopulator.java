package org.worldGeneratorTest.worldGeneratorTest;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Directional;

import java.util.Random;

public class SkyBlockOverworldPopulator extends BlockPopulator {

    @Override
    public void populate(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, LimitedRegion limitedRegion) {
        if (chunkX == 0 && chunkZ == 0) {
            int x = 5, y = 68, z = 1;

            var data = limitedRegion.getBlockData(x,y,z);
            data.rotate(StructureRotation.COUNTERCLOCKWISE_90);

            var state = limitedRegion.getBlockState(x, y, z);
            if (state instanceof Chest chest) {
                chest.setBlockData(data);
                Inventory inv = chest.getSnapshotInventory();
                inv.addItem(new ItemStack(Material.LAVA_BUCKET));
                inv.addItem(new ItemStack(Material.ICE));
            }
            state.update();




            limitedRegion.generateTree(new Location(null, 0, 68, 5), random, TreeType.TREE);
        }

        if (chunkX == -5 && chunkZ == 0) {
            int x = -79, y = 68, z = 1;

            var data = limitedRegion.getBlockData(x,y,z);
            data.rotate(StructureRotation.CLOCKWISE_90);

            var state = limitedRegion.getBlockState(x, y, z);
            if (state instanceof Chest chest) {
                chest.setBlockData(data);

                Inventory inv = chest.getSnapshotInventory();
                inv.addItem(new ItemStack(Material.OBSIDIAN, 10));
                inv.addItem(new ItemStack(Material.MELON_SLICE));
                inv.addItem(new ItemStack(Material.PUMPKIN_SEEDS));

                chest.update(true);
            }
        }
    }
}
