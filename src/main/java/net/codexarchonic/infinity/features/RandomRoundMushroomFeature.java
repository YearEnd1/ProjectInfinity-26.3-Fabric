package net.codexarchonic.infinity.features;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

public class RandomRoundMushroomFeature extends RandomMushroomFeature {
    public RandomRoundMushroomFeature(Codec<Config> codec) {
        super(codec);
    }

    @Override
    protected void generateCap(LevelAccessor world, RandomSource random, BlockPos start, int y, BlockPos.MutableBlockPos mutable, Config config) {
        for (int i = y - 3; i <= y; ++i) {
            int j = i < y ? config.foliageRadius() : config.foliageRadius() - 1;
            for (int l = -j; l <= j; ++l) {
                for (int m = -j; m <= j; ++m) {
                    boolean bl = l == -j;
                    boolean bl2 = l == j;
                    boolean bl3 = m == -j;
                    boolean bl4 = m == j;
                    boolean bl5 = bl || bl2;
                    boolean bl6 = bl3 || bl4;
                    if (i < y && bl5 == bl6) continue;
                    mutable.setWithOffset(start, l, i, m);
                    if (world.getBlockState(mutable).isSolidRender(
                            //? if <1.21.2
                            //world, mutable
                    )) continue;
                    BlockState blockState = config.capProvider().getState(random, start);
                    this.setBlock(world, mutable, blockState);
                }
            }
        }
    }

    @Override
    protected int getCapSize(int i, int j, int capSize, int y) {
        int k = 0;
        if (y < j && y >= j - 3) {
            k = capSize;
        } else if (y == j) {
            k = capSize;
        }
        return k;
    }
}
