package com.mushroom.midnight.common.biome;

import com.mushroom.midnight.common.registry.ModBlocks;
import com.mushroom.midnight.common.world.generator.WorldGenDoubleMidnightPlant;
import com.mushroom.midnight.common.world.generator.WorldGenMidnightFungi;
import com.mushroom.midnight.common.world.generator.WorldGenMidnightPlant;
import net.minecraft.block.BlockBush;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.TerrainGen;

import java.util.Random;

public class BiomeFungiForest extends BiomeBase {
    protected static final WorldGenMidnightPlant DEWSHROOM_GENERATOR = new WorldGenMidnightPlant(
            ModBlocks.DEWSHROOM.getDefaultState(),
            ((BlockBush) ModBlocks.DEWSHROOM)::canBlockStay,
            32
    );

    protected static final WorldGenDoubleMidnightPlant DOUBLE_DEWSHROOM_GENERATOR = new WorldGenDoubleMidnightPlant(
            ModBlocks.DOUBLE_DEWSHROOM.getDefaultState(),
            (world, pos, state) -> ModBlocks.DOUBLE_DEWSHROOM.canPlaceBlockAt(world, pos),
            16
    );

    protected static final WorldGenMidnightPlant NIGHTSHROOM_GENERATOR = new WorldGenMidnightPlant(
            ModBlocks.NIGHTSHROOM.getDefaultState(),
            ((BlockBush) ModBlocks.NIGHTSHROOM)::canBlockStay,
            32
    );

    protected static final WorldGenDoubleMidnightPlant DOUBLE_NIGHTSHROOM_GENERATOR = new WorldGenDoubleMidnightPlant(
            ModBlocks.DOUBLE_NIGHTSHROOM.getDefaultState(),
            (world, pos, state) -> ModBlocks.DOUBLE_NIGHTSHROOM.canPlaceBlockAt(world, pos),
            16
    );

    protected static final WorldGenMidnightFungi LARGE_DEWSHROOM_GENERATOR = new WorldGenMidnightFungi(ModBlocks.DEWSHROOM_STEM.getDefaultState(), ModBlocks.DEWSHROOM_HAT.getDefaultState());
    protected static final WorldGenMidnightFungi LARGE_NIGHTSHROOM_GENERATOR = new WorldGenMidnightFungi(ModBlocks.NIGHTSHROOM_STEM.getDefaultState(), ModBlocks.NIGHTSHROOM_HAT.getDefaultState());

    public BiomeFungiForest(BiomeProperties properties) {
        super(properties);
        this.decorator.grassPerChunk = 4;
    }

    @Override
    public void decorate(World world, Random rand, BlockPos pos) {
        ChunkPos chunkPos = new ChunkPos(pos);

        if (TerrainGen.decorate(world, rand, chunkPos, DecorateBiomeEvent.Decorate.EventType.BIG_SHROOM)) {
            for (int i = 0; i < 4; i++) {
                int offsetX = rand.nextInt(16) + 8;
                int offsetZ = rand.nextInt(16) + 8;

                BlockPos surface = world.getTopSolidOrLiquidBlock(pos.add(offsetX, 0, offsetZ));
                WorldGenMidnightFungi generator = rand.nextBoolean() ? LARGE_DEWSHROOM_GENERATOR : LARGE_NIGHTSHROOM_GENERATOR;
                generator.generate(world, rand, surface);
            }
        }

        if (TerrainGen.decorate(world, rand, chunkPos, DecorateBiomeEvent.Decorate.EventType.SHROOM)) {
            this.generateCoverPlant(world, rand, pos, 2, DEWSHROOM_GENERATOR);
            this.generateCoverPlant(world, rand, pos, 1, DOUBLE_DEWSHROOM_GENERATOR);
            this.generateCoverPlant(world, rand, pos, 2, NIGHTSHROOM_GENERATOR);
            this.generateCoverPlant(world, rand, pos, 1, DOUBLE_NIGHTSHROOM_GENERATOR);
        }

        super.decorate(world, rand, pos);
    }
}
