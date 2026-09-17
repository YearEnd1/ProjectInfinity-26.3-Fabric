package net.codexarchonic.infinity.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
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

public class RandomShapeFeature extends Feature<RandomShapeFeature.Config> {
    public RandomShapeFeature(Codec<Config> codec) {
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
        double p = context.config().pow();
        double ia = 0.0;
        double ra = Math.pow(r, p);
        for (int i = 0; ia < ra; i++) {
            double ja = 0.0;
            for (int j = 0; ia + ja < ra; j++) {
                double ka = 0.0;
                for (int k = 0; ia + ja + ka < ra; k++) {
                    for (Vec3i v : signs(i, j, k)) {
                        BlockPos blockPos1 = blockPos.offset(v);
                        if (structureWorldAccess.isEmptyBlock(blockPos1) || context.config().replaceable().contains(structureWorldAccess.getBlockState(blockPos1)))
                            this.setBlock(structureWorldAccess, blockPos1, bl ? blocks.get(v.getX() + v.getY() + v.getZ()) :
                                    context.config().blockProvider().getState(
                                            //? if >26
                                            //structureWorldAccess,
                                            random, blockPos1));
                    }
                    ka = Math.pow(k+1, p);
                }
                ja = Math.pow(j+1, p);
            }
            ia = Math.pow(i+1, p);
        }
        return true;
    }

    static HashSet<Vec3i> signs(int i, int j, int k) {
        HashSet<Vec3i> res = new HashSet<>();
        res.add(new Vec3i(i, j, k));
        res.add(new Vec3i(i, j, -k));
        res.add(new Vec3i(i, -j, k));
        res.add(new Vec3i(i, -j, -k));
        res.add(new Vec3i(-i, j, k));
        res.add(new Vec3i(-i, j, -k));
        res.add(new Vec3i(-i, -j, k));
        res.add(new Vec3i(-i, -j, -k));
        return res;
    }

    public record Config(BlockStateProvider blockProvider, List<BlockState> replaceable, FloatProvider radius, double pow, boolean useBands) implements FeatureConfiguration {
        public static final Codec<Config> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                (BlockStateProvider.CODEC.fieldOf("block_provider")).forGetter(a -> a.blockProvider),
                (Codec.list(BlockState.CODEC).fieldOf("replaceable")).forGetter(a -> a.replaceable),
                //~ if >26 'FloatProvider'->'FloatProviders'
                (FloatProvider.codec(2.0f, 20.0f).fieldOf("radius")).forGetter(a -> a.radius),
                (Codec.DOUBLE.fieldOf("power")).orElse(2.0).forGetter(a -> a.pow),
                (Codec.BOOL.fieldOf("use_bands")).orElse(false).forGetter(a -> a.useBands)).apply(
                instance, Config::new));
    }
}
