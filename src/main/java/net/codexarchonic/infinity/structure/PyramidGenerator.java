package net.codexarchonic.infinity.structure;

import net.codexarchonic.infinity.registry.core.ModStructureTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.ScatteredFeaturePiece;
import net.minecraft.world.level.levelgen.structure.Structure;

public class PyramidGenerator extends ScatteredFeaturePiece {
    int y, y0;
    BlockStateProvider b;

    public PyramidGenerator(int x, int y, int z, int width, int height, int depth, BlockStateProvider provider) {
        super(ModStructureTypes.PYRAMID_PIECE.get(), x, y, z, width, height, depth, Direction.EAST);
        b = provider;
    }

    public static PyramidGenerator of(Structure.GenerationContext context, int top_y, int bottom_y, BlockStateProvider provider) {
        int deltaY = top_y-bottom_y;
        int x = context.chunkPos().getMinBlockX();
        int z = context.chunkPos().getMinBlockZ();
        return new PyramidGenerator(x - deltaY, bottom_y, z - deltaY, 2*deltaY + 1, deltaY, 2*deltaY + 1, provider);
    }

    public PyramidGenerator(CompoundTag nbt) {
        super(ModStructureTypes.PYRAMID_PIECE.get(), nbt);
    }

    @Override
    public void postProcess(WorldGenLevel world, StructureManager structureAccessor, ChunkGenerator chunkGenerator, RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos pivot) {
        int size = y-y0;
        for(int j = 0; j <= size; j++) {
            for (int i = j; i <= 2*size - j; ++i) {
                for (int k = j; k <= 2*size - j; ++k) {
                    if (this.getBlock(world, i, j, k, box).isAir() || this.getBlock(world, i, j, k, box).is(Blocks.WATER)) {
                        BlockState state;
                        if (b == null) state = Blocks.BRICKS.defaultBlockState();
                        else state = b.getState(
                                //? if >26
                                //world,
                                random, new BlockPos(i, j, k));
                        this.placeBlock(world, state, i, j, k, box);
                    }
                }
            }
        }
    }
}
