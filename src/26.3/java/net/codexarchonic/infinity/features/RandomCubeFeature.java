package net.codexarchonic.infinity.features;

import net.minecraft.world.level.chunk.ChunkGenerator;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.FloatProviders;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class RandomCubeFeature implements Feature {
    public static final MapCodec<RandomCubeFeature> CODEC = Config.CODEC.xmap(RandomCubeFeature::new, feature -> feature.config);

    private final Config config;

    public RandomCubeFeature(Config config) {
        this.config = config;
    }

    @Override
    public MapCodec<RandomCubeFeature> codec() {
        return CODEC;
    }

    @Override
    public boolean place(WorldGenLevel levelArg, ChunkGenerator generator, RandomSource randomArg, BlockPos originArg) {
        WorldGenLevel structureWorldAccess = levelArg;
        RandomSource random = randomArg;
        BlockPos blockPos = originArg;
        Map<Integer, BlockState> blocks = new HashMap<>();
        int r = (int)config.radius().sample(random);
        boolean bl = config.useBands();
        if (bl) for (int i = -3*r; i <= 3*r; i++) blocks.put(i, config.blockProvider().getState(
                structureWorldAccess,
                random, blockPos.east(i)));
        for (int i = -r; i <= r; i++) for (int j = -r; j <= r; j++) for (int k = -r; k <= r; k++) {
            BlockPos blockPos1 = blockPos.offset(i, j, k);
            if (structureWorldAccess.isEmptyBlock(blockPos1) || config.replaceable().contains(structureWorldAccess.getBlockState(blockPos1)))
                this.setBlock(structureWorldAccess, blockPos1, bl ? blocks.get(i+j+k) : config.blockProvider().getState(
                        structureWorldAccess,
                        random, blockPos1));
        }
        return true;
    }

    public record Config(BlockStateProvider blockProvider, List<BlockState> replaceable, FloatProvider radius, boolean useBands) {
        public static final MapCodec<Config> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                (BlockStateProvider.DIRECT_CODEC.fieldOf("block_provider")).forGetter(a -> a.blockProvider),
                (Codec.list(BlockState.CODEC).fieldOf("replaceable")).forGetter(a -> a.replaceable),
                (FloatProviders.codec(2.0f, 20.0f).fieldOf("radius")).forGetter(a -> a.radius),
                (Codec.BOOL.fieldOf("use_bands")).orElse(false).forGetter(a -> a.useBands)).apply(
                instance, Config::new));
    }
}
