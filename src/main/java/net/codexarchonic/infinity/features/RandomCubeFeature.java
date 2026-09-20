package net.codexarchonic.infinity.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.FloatProvider;
//? if >26
//import net.minecraft.util.valueproviders.FloatProviders;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class RandomCubeFeature extends Feature<RandomCubeFeature.Config> {
    public RandomCubeFeature(Codec<Config> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<Config> context) {
        WorldGenLevel structureWorldAccess = context.level();
        RandomSource random = context.random();
        BlockPos blockPos = context.origin();
        Map<Integer, BlockState> blocks = new HashMap<>();
        int r = (int)context.config().radius().sample(random);
        boolean bl = context.config().useBands();
        if (bl) for (int i = -3*r; i <= 3*r; i++) blocks.put(i, context.config().blockProvider().getState(
                //? if >26
                //structureWorldAccess,
                random, blockPos.east(i)));
        for (int i = -r; i <= r; i++) for (int j = -r; j <= r; j++) for (int k = -r; k <= r; k++) {
            BlockPos blockPos1 = blockPos.offset(i, j, k);
            if (structureWorldAccess.isEmptyBlock(blockPos1) || context.config().replaceable().contains(structureWorldAccess.getBlockState(blockPos1)))
                this.setBlock(structureWorldAccess, blockPos1, bl ? blocks.get(i+j+k) : context.config().blockProvider().getState(
                        //? if >26
                        //structureWorldAccess,
                        random, blockPos1));
        }
        return true;
    }

    public record Config(BlockStateProvider blockProvider, List<BlockState> replaceable, FloatProvider radius, boolean useBands) implements FeatureConfiguration {
        public static final Codec<Config> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                (BlockStateProvider.CODEC.fieldOf("block_provider")).forGetter(a -> a.blockProvider),
                (Codec.list(BlockState.CODEC).fieldOf("replaceable")).forGetter(a -> a.replaceable),
                //~ if >26 'FloatProvider'->'FloatProviders'
                (FloatProvider.codec(2.0f, 20.0f).fieldOf("radius")).forGetter(a -> a.radius),
                (Codec.BOOL.fieldOf("use_bands")).orElse(false).forGetter(a -> a.useBands)).apply(
                instance, Config::new));
    }
}
