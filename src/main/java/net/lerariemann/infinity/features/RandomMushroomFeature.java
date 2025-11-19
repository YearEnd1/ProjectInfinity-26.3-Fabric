package net.lerariemann.infinity.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public abstract class RandomMushroomFeature
        extends Feature<RandomMushroomFeature.Config> {
    public RandomMushroomFeature(Codec<Config> codec) {
        super(codec);
    }

    protected void generateStem(LevelAccessor world, RandomSource random, BlockPos pos, Config config, int height, BlockPos.MutableBlockPos mutablePos) {
        for (int i = 0; i < height; ++i) {
            mutablePos.set(pos).move(Direction.UP, i);
            if (world.getBlockState(mutablePos).isSolidRender(
                    //? if <1.21.2
                    /*world, mutablePos*/
            )) continue;
            this.setBlock(world, mutablePos, config.stemProvider().getState(random, pos));
        }
    }

    protected int getHeight(RandomSource random, int h) {
        int i = random.nextInt(3) + h - 1;
        if (random.nextInt(12) == 0) {
            i *= 2;
        }
        return i;
    }

    protected boolean canGenerate(LevelAccessor world, BlockPos pos, int height, BlockPos.MutableBlockPos mutablePos, Config config) {
        int i = pos.getY();
        //? if <1.21.2 {
        /*var minY = world.getMinBuildHeight();
        var maxY = world.getMaxBuildHeight();
         *///?} else {
        var minY = world.getMinY();
        var maxY = world.getMaxY();
        //?}
        if (i < minY + 1 || i + height + 1 >= maxY) {
            return false;
        }
        BlockState blockState = world.getBlockState(pos.below());
        if (!blockState.getBlock().equals(config.validBaseBlock().getBlock())) {
            return false;
        }
        for (int j = 0; j <= height; ++j) {
            int k = this.getCapSize(-1, -1, config.foliageRadius(), j);
            for (int l = -k; l <= k; ++l) {
                for (int m = -k; m <= k; ++m) {
                    BlockState blockState2 = world.getBlockState(mutablePos.setWithOffset(pos, l, j, m));
                    if (blockState2.isAir() || blockState2.is(BlockTags.LEAVES)) continue;
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean place(FeaturePlaceContext<Config> context) {
        BlockPos.MutableBlockPos mutable;
        WorldGenLevel structureWorldAccess = context.level();
        BlockPos blockPos = context.origin();
        RandomSource random = context.random();
        Config hugeMushroomFeatureConfig = context.config();
        int i = this.getHeight(random, context.config().height());
        if (!this.canGenerate(structureWorldAccess, blockPos, i, mutable = new BlockPos.MutableBlockPos(), hugeMushroomFeatureConfig)) {
            return false;
        }
        this.generateCap(structureWorldAccess, random, blockPos, i, mutable, hugeMushroomFeatureConfig);
        this.generateStem(structureWorldAccess, random, blockPos, hugeMushroomFeatureConfig, i, mutable);
        return true;
    }

    protected abstract int getCapSize(int var1, int var2, int var3, int var4);

    protected abstract void generateCap(LevelAccessor var1, RandomSource var2, BlockPos var3, int var4, BlockPos.MutableBlockPos var5, Config var6);

    public record Config(BlockStateProvider capProvider, BlockStateProvider stemProvider,
                                              BlockState validBaseBlock, int foliageRadius,
                                              int height) implements FeatureConfiguration {
        public static final Codec<Config> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                (BlockStateProvider.CODEC.fieldOf("cap_provider")).forGetter(a -> a.capProvider),
                (BlockStateProvider.CODEC.fieldOf("stem_provider")).forGetter(a -> a.stemProvider),
                (BlockState.CODEC.fieldOf("valid_base_block")).forGetter(a -> a.validBaseBlock),
                (Codec.INT.fieldOf("foliage_radius")).orElse(2).forGetter(a -> a.foliageRadius),
                (Codec.INT.fieldOf("height")).orElse(5).forGetter(a -> a.height)).apply(
                instance, Config::new));
    }
}

