package org.worldGeneratorTest.worldGeneratorTest;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.mvplugins.multiverse.external.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class SkyBlockOverworldGenerator extends ChunkGenerator {


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
            for(int x = 0;x < 6; x++){
                for(int z = 0;z < 6; z++){
                    if(x < 3 || z < 3){
                        chunkData.setBlock(x, 67, z, Material.GRASS_BLOCK);
                        chunkData.setBlock(x, 66, z, Material.DIRT);
                        chunkData.setBlock(x, 65, z, Material.DIRT);
                    }
                    if(x == 5 && z == 1){
                        chunkData.setBlock(x, 68, z, Material.CHEST);
                    }
                }
            }
        }
        if(chunkX == -5 && chunkZ == 0){
            for(int x = 0;x < 3; x++){
                for(int z = 0;z < 3; z++) {
                    chunkData.setBlock(x, 67, z, Material.SAND);
                    chunkData.setBlock(x, 66, z, Material.SAND);
                    chunkData.setBlock(x, 65, z, Material.SAND);
                    if(x == 1 && z == 1){
                        chunkData.setBlock(x, 68, z, Material.CHEST);
                    }
                    if(x == 0 && z == 2){
                        chunkData.setBlock(x, 68, z, Material.CACTUS);
                        chunkData.setBlock(x, 69, z, Material.CACTUS_FLOWER);
                    }
                }
            }
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
