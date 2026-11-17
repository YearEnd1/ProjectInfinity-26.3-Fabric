package net.codexarchonic.infinity.features;

import net.minecraft.world.level.chunk.ChunkGenerator;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.IntProviders;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import org.jspecify.annotations.Nullable;

public class RandomColumnsFeature implements Feature {
    private static final ImmutableList<Block> CANNOT_REPLACE_BLOCKS = ImmutableList.of(Blocks.LAVA, Blocks.BEDROCK, Blocks.WATER, Blocks.END_GATEWAY, Blocks.CHEST, Blocks.SPAWNER);

    public static final MapCodec<RandomColumnsFeature> CODEC = Config.CODEC.xmap(RandomColumnsFeature::new, feature -> feature.config);

    private final Config config;

    public RandomColumnsFeature(Config config) {
        this.config = config;
    }

    @Override
    public MapCodec<RandomColumnsFeature> codec() {
        return CODEC;
    }

    @Override
    public boolean place(WorldGenLevel levelArg, ChunkGenerator generator, RandomSource randomArg, BlockPos originArg) {
        int i = generator.getSeaLevel();
        BlockPos blockPos = originArg;
        WorldGenLevel structureWorldAccess = levelArg;
        RandomSource random = randomArg;
        Config randomColumnsFeatureConfig = config;
        if (!RandomColumnsFeature.canPlaceAt(structureWorldAccess, i, blockPos.mutable())) {
            return false;
        }
        int j = randomColumnsFeatureConfig.height().sample(random);
        boolean bl = random.nextFloat() < 0.9f;
        int k = Math.min(j, bl ? 5 : 8);
        int l = bl ? 50 : 15;
        boolean bl2 = false;
        for (BlockPos blockPos2 : BlockPos.randomBetweenClosed(random, l, blockPos.getX() - k, blockPos.getY(), blockPos.getZ() - k, blockPos.getX() + k, blockPos.getY(), blockPos.getZ() + k)) {
            int m = j - blockPos2.distManhattan(blockPos);
            if (m < 0) continue;
            BlockState state = config.block().getState(
                    structureWorldAccess,
                    random, blockPos2);
            bl2 |= this.placeBasaltColumn(structureWorldAccess, i, blockPos2, m, randomColumnsFeatureConfig.reach().sample(random), state);
        }
        return bl2;
    }

    private boolean placeBasaltColumn(LevelAccessor world, int seaLevel, BlockPos pos, int height, int reach, BlockState state) {
        boolean bl = false;
        block0: for (BlockPos blockPos : BlockPos.betweenClosed(pos.getX() - reach, pos.getY(), pos.getZ() - reach,
                pos.getX() + reach, pos.getY(), pos.getZ() + reach)) {
            int i = blockPos.distManhattan(pos);
            BlockPos blockPos2 = RandomColumnsFeature.isAirOrOcean(world, seaLevel, blockPos) ?
                    RandomColumnsFeature.moveDownToGround(world, seaLevel, blockPos.mutable(), i) :
                    RandomColumnsFeature.moveUpToAir(world, blockPos.mutable(), i);
            if (blockPos2 == null) continue;
            BlockPos.MutableBlockPos mutable = blockPos2.mutable();
            for (int j = height - i / 2; j >= 0; --j) {
                if (RandomColumnsFeature.isAirOrOcean(world, seaLevel, mutable)) {
                    this.setBlock(world, mutable, state);
                    mutable.move(Direction.UP);
                    bl = true;
                    continue;
                }
                if (!world.getBlockState(mutable).is(state.getBlock())) continue block0;
                mutable.move(Direction.UP);
            }
        }
        return bl;
    }

    @Nullable
    private static BlockPos moveDownToGround(LevelAccessor world, int seaLevel, BlockPos.MutableBlockPos mutablePos, int distance) {
        var minY = world.getMinY();
        while (mutablePos.getY() > minY + 1 && distance > 0) {
            --distance;
            if (RandomColumnsFeature.canPlaceAt(world, seaLevel, mutablePos)) {
                return mutablePos;
            }
            mutablePos.move(Direction.DOWN);
        }
        return null;
    }

    private static boolean canPlaceAt(LevelAccessor world, int seaLevel, BlockPos.MutableBlockPos mutablePos) {
        if (RandomColumnsFeature.isAirOrOcean(world, seaLevel, mutablePos)) {
            BlockState blockState = world.getBlockState(mutablePos.move(Direction.DOWN));
            mutablePos.move(Direction.UP);
            return !blockState.isAir() && !CANNOT_REPLACE_BLOCKS.contains(blockState.getBlock());
        }
        return false;
    }

    @Nullable
    private static BlockPos moveUpToAir(LevelAccessor world, BlockPos.MutableBlockPos mutablePos, int distance) {
        var maxY = world.getMaxY();
        while (mutablePos.getY() < maxY && distance > 0) {
            --distance;
            BlockState blockState = world.getBlockState(mutablePos);
            if (CANNOT_REPLACE_BLOCKS.contains(blockState.getBlock())) {
                return null;
            }
            if (blockState.isAir()) {
                return mutablePos;
            }
            mutablePos.move(Direction.UP);
        }
        return null;
    }

    private static boolean isAirOrOcean(LevelAccessor world, int seaLevel, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);
        return blockState.isAir() || (blockState.getBlock() instanceof LiquidBlock) && pos.getY() <= seaLevel;
    }

    public record Config(IntProvider reach, IntProvider height,
                         BlockStateProvider block) {
        public static final MapCodec<Config> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                (IntProviders.codec(0, 3).fieldOf("reach")).forGetter(a -> a.reach),
                (IntProviders.codec(1, 15).fieldOf("height")).forGetter(a -> a.height),
                (BlockStateProvider.DIRECT_CODEC.fieldOf("block_provider")).forGetter(a -> a.block)).apply(
                instance, Config::new));
    }
}
