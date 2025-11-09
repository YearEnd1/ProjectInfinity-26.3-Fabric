package net.lerariemann.infinity.features;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;

public class RandomEndIslandFeature extends Feature<BlockStateConfiguration> {
    public RandomEndIslandFeature(Codec<BlockStateConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<BlockStateConfiguration> context) {
        WorldGenLevel structureWorldAccess = context.level();
        RandomSource random = context.random();
        BlockPos blockPos = context.origin();
        float f = (float)random.nextInt(3) + 4.0f;
        int i = 0;
        while (f > 0.5f) {
            for (int j = Mth.floor(-f); j <= Mth.ceil(f); ++j) {
                for (int k = Mth.floor(-f); k <= Mth.ceil(f); ++k) {
                    if (!((float)(j * j + k * k) <= (f + 1.0f) * (f + 1.0f))) continue;
                    this.setBlock(structureWorldAccess, blockPos.offset(j, i, k), context.config().state);
                }
            }
            f -= (float)random.nextInt(2) + 0.5f;
            --i;
        }
        return true;
    }
}

