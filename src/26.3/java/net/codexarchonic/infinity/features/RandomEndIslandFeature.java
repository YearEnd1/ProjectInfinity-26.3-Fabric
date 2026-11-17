package net.codexarchonic.infinity.features;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;

public class RandomEndIslandFeature implements Feature {
    public static final MapCodec<RandomEndIslandFeature> CODEC = BlockState.CODEC.fieldOf("state").xmap(RandomEndIslandFeature::new, feature -> feature.config);

    private final BlockState config;

    public RandomEndIslandFeature(BlockState config) {
        this.config = config;
    }

    @Override
    public MapCodec<RandomEndIslandFeature> codec() {
        return CODEC;
    }

    @Override
    public boolean place(WorldGenLevel levelArg, ChunkGenerator generator, RandomSource randomArg, BlockPos originArg) {
        WorldGenLevel structureWorldAccess = levelArg;
        RandomSource random = randomArg;
        BlockPos blockPos = originArg;
        float f = (float)random.nextInt(3) + 4.0f;
        int i = 0;
        while (f > 0.5f) {
            for (int j = Mth.floor(-f); j <= Mth.ceil(f); ++j) {
                for (int k = Mth.floor(-f); k <= Mth.ceil(f); ++k) {
                    if (!((float)(j * j + k * k) <= (f + 1.0f) * (f + 1.0f))) continue;
                    this.setBlock(structureWorldAccess, blockPos.offset(j, i, k), config);
                }
            }
            f -= (float)random.nextInt(2) + 0.5f;
            --i;
        }
        return true;
    }
}
