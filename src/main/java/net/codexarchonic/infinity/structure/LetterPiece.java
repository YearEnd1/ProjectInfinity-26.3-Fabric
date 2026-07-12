package net.codexarchonic.infinity.structure;

import net.codexarchonic.infinity.util.var.TextData;
import net.codexarchonic.infinity.registry.core.ModStructureTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.ScatteredFeaturePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class LetterPiece extends ScatteredFeaturePiece {
    List<Integer> letter;
    BlockStateProvider block;
    int ori;

    protected LetterPiece(StructurePieceType type, int x, int y, int z, int width, int height, int depth,
                          int ori, List<Integer> letter, BlockStateProvider block) {
        super(type, x, y, z, width, height, depth, Direction.SOUTH);
        this.letter = letter;
        this.block = block;
        this.ori = ori;
    }

    public LetterPiece(CompoundTag nbt) {
        super(ModStructureTypes.LETTER.get(), nbt);
    }

    static LetterPiece of(BlockPos startcorner, int ori, @NonNull List<Integer> letter, BlockStateProvider block) {
        BlockPos dims = getDimensions(ori, letter.size());
        BlockPos othercoord = startcorner.offset(dims);
        return new LetterPiece(ModStructureTypes.LETTER.get(),
                Math.min(startcorner.getX(), othercoord.getX()),
                Math.min(startcorner.getY(), othercoord.getY()),
                Math.min(startcorner.getZ(), othercoord.getZ()),
                Math.abs(dims.getX()), Math.abs(dims.getY()), Math.abs(dims.getZ()), ori, letter, block);
    }

    static BlockPos getDimensions(int ori, int letterwidth) {
        return TextData.mutate(BlockPos.ZERO, ori, 8, letterwidth+1, 8, 0, 0, 0);
    }

    @Override
    public void postProcess(WorldGenLevel world, StructureManager structureAccessor, ChunkGenerator chunkGenerator, RandomSource random, BoundingBox chunkBox, ChunkPos chunkPos, BlockPos pivot) {
        int j, k;
        if (letter == null) return;

        for (j = 0; j < letter.size(); j++) {
            for (k = 0; k < 8; k++) {
                BlockPos bp = TextData.mutate(BlockPos.ZERO, ori, k, j, 0, 8, letter.size(), 8);
                this.placeBlock(world,
                        ((letter.get(j) >> k)%2 == 1) ? block.getState(random, bp) : Blocks.AIR.defaultBlockState(),
                        bp.getX(), bp.getY(), bp.getZ(), chunkBox);
            }
        }
    }
}