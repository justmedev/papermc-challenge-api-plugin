package org.worldGeneratorTest.worldGeneratorTest;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.mvplugins.multiverse.external.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class SkyBlockNetherGenerator extends ChunkGenerator {

    @Override
    public boolean shouldGenerateNoise() {
        return false;
    }

    @Override
    public boolean shouldGenerateSurface() {
        return false;
    }

    @Override
    public boolean shouldGenerateCaves() {
        return false;
    }

    @Override
    public boolean shouldGenerateDecorations() {
        return false;
    }

    @Override
    public boolean shouldGenerateMobs() {
        return false;
    }

    @Override
    public boolean shouldGenerateStructures() {
        return false;
    }

    @Override
    public void generateCaves(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull ChunkData chunkData) {
        //hardcoded skyblock island
        if(chunkX == 0 && chunkZ == 0) {
            for(int x = 0;x < 3; x++){
                for(int z = 0;z < 3; z++){
                        chunkData.setBlock(x, 67, z, Material.GLOWSTONE);
                        chunkData.setBlock(x, 66, z, Material.GLOWSTONE);
                        chunkData.setBlock(x, 65, z, Material.GLOWSTONE);
                        if(x == 0 && z == 0){
                            chunkData.setBlock(x, 68, z, Material.RED_MUSHROOM);
                        }
                        if (x == 1 && z == 2) {
                            chunkData.setBlock(x, 68, z, Material.BROWN_MUSHROOM);
                        }
                        if (x == 2 && z == 2) {
                            chunkData.setBlock(x, 68, z, Material.CHEST);
                        }
                }
            }
            chunkData.setBlock(1, 67, 3, Material.OBSIDIAN);
            chunkData.setBlock(2, 67, 3, Material.OBSIDIAN);
            for(int y = 0; y < 3;y++){
                chunkData.setBlock(0, 68 + y, 3, Material.OBSIDIAN);
                chunkData.setBlock(1, 68 + y, 3, Material.NETHER_PORTAL);
                chunkData.setBlock(2, 68 + y, 3, Material.NETHER_PORTAL);
                chunkData.setBlock(3, 68 + y, 3, Material.OBSIDIAN);
            }
            chunkData.setBlock(1, 71, 3, Material.OBSIDIAN);
            chunkData.setBlock(2, 71, 3, Material.OBSIDIAN);
        }

    }
    @Override
    public Location getFixedSpawnLocation(World world, Random random) {
        return new Location(world, 0.5, 68, 0.5);
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        return List.of(
                new SkyBlockNetherPopulator()
        );
    }
}
