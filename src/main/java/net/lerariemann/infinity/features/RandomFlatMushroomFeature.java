package net.lerariemann.infinity.features;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

public class RandomFlatMushroomFeature extends RandomMushroomFeature {
    public RandomFlatMushroomFeature(Codec<Config> codec) {
        super(codec);
    }

    @Override
    protected void generateCap(LevelAccessor world, RandomSource random, BlockPos start, int y, BlockPos.MutableBlockPos mutable, Config config) {
        int i = config.foliageRadius();
        for (int j = -i; j <= i; ++j) {
            for (int k = -i; k <= i; ++k) {
                boolean bl = j == -i;
                boolean bl2 = j == i;
                boolean bl3 = k == -i;
                boolean bl4 = k == i;
                boolean bl5 = bl || bl2;
                boolean bl6 = bl3 || bl4;
                if (bl5 && bl6) continue;
                mutable.setWithOffset(start, j, y, k);
                if (world.getBlockState(mutable).isSolidRender(
                        //? if <1.21.2
                        /*world, mutable*/
                )) continue;
                BlockState blockState = config.capProvider().getState(random, start);
                this.setBlock(world, mutable, blockState);
            }
        }
    }

    @Override
    protected int getCapSize(int i, int j, int capSize, int y) {
        return y <= 3 ? 0 : capSize;
    }
}
