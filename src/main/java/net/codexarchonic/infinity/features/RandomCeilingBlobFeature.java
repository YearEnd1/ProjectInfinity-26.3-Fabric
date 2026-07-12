package net.codexarchonic.infinity.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class RandomCeilingBlobFeature extends Feature<RandomCeilingBlobFeature.Config> {
    public RandomCeilingBlobFeature(Codec<Config> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<Config> context) {
        WorldGenLevel structureWorldAccess = context.level();
        BlockPos blockPos = context.origin();
        RandomSource random = context.random();
        if (!structureWorldAccess.isEmptyBlock(blockPos)) {
            return false;
        }
        BlockState blockState = structureWorldAccess.getBlockState(blockPos.above());
        Set<Block> blocks = new HashSet<>();
        Config config = context.config();
        for (BlockState b : config.target_blocks()) blocks.add(b.getBlock());
        if (!blocks.contains(blockState.getBlock())) {
            return false;
        }
        BlockState state = context.config().block().getState(random, blockPos);
        structureWorldAccess.setBlock(blockPos, state, Block.UPDATE_CLIENTS);
        for (int i = 0; i < 1500; ++i) {
            BlockPos blockPos2 = blockPos.offset(random.nextInt(config.size_xz()) - random.nextInt(config.size_xz()),
                    -random.nextInt(config.size_y()), random.nextInt(config.size_xz()) - random.nextInt(config.size_xz()));
            if (!structureWorldAccess.getBlockState(blockPos2).isAir()) continue;
            int j = 0;
            for (Direction direction : Direction.values()) {
                if (structureWorldAccess.getBlockState(blockPos2.relative(direction)).is(state.getBlock())) {
                    ++j;
                }
                if (j > 1) break;
            }
            if (j == 0) continue;
            structureWorldAccess.setBlock(blockPos2, state, Block.UPDATE_CLIENTS);
        }
        return true;
    }

    public record Config(BlockStateProvider block, List<BlockState> target_blocks, int size_xz, int size_y) implements FeatureConfiguration {
        public static final Codec<Config> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                (BlockStateProvider.CODEC.fieldOf("block")).forGetter(a -> a.block),
                (Codec.list(BlockState.CODEC).fieldOf("targets")).forGetter(a -> a.target_blocks),
                (Codec.INT.fieldOf("size_xz")).forGetter(a -> a.size_xz),
                (Codec.INT.fieldOf("size_y")).forGetter(a -> a.size_y)).apply(
                instance, Config::new));
    }
}