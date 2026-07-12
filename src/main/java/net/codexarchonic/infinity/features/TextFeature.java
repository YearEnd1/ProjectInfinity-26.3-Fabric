package net.codexarchonic.infinity.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.codexarchonic.infinity.util.var.TextData;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import java.util.List;

public class TextFeature extends Feature<TextFeature.Config> {
    public TextFeature(Codec<Config> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<Config> context) {
        WorldGenLevel structureWorldAccess = context.level();
        RandomSource random = context.random();
        BlockPos blockPos = context.origin();
        blockPos = TextData.mutate(blockPos, context.config().orientation(), 0, -16);
        String text = context.config().text();
        int i2 = 8 + random.nextInt(16);
        if (text.length() > i2) {
            int i1 = random.nextInt(text.length() - i2);
            text = text.substring(i1, i1 + i2);
        }
        int i, j, k;
        int len = 0;
        for (i = 0; i < text.length(); i++) {
            List<Integer> lst = TextData.storage.get(text.charAt(i));
            if (lst != null) {
                if (!structureWorldAccess.hasChunkAt(TextData.mutate(blockPos, context.config().orientation(), 0, len + lst.size() - 1))) break;
                for (j = 0; j < lst.size(); j++) {
                    for (k = 0; k < 8; k++) {
                        if ((lst.get(j) >> k)%2 == 1) {
                            BlockPos blockPos1 = TextData.mutate(blockPos, context.config().orientation(), -k, len + j);
                            if ((structureWorldAccess.isEmptyBlock(blockPos1) || context.config().replaceable().contains(structureWorldAccess.getBlockState(blockPos1))))
                                this.setBlock(structureWorldAccess, blockPos1, context.config().blockProvider().getState(random, blockPos1));
                        }
                    }
                }
                len += lst.size() + context.config().spacing();
            }
        }
        return true;
    }

    public record Config(BlockStateProvider blockProvider, List<BlockState> replaceable, int orientation, int spacing, String text) implements FeatureConfiguration {
        public static final Codec<Config> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                (BlockStateProvider.CODEC.fieldOf("block_provider")).forGetter(a -> a.blockProvider),
                (Codec.list(BlockState.CODEC).fieldOf("replaceable")).forGetter(a -> a.replaceable),
                (Codec.INT.fieldOf("orientation")).orElse(2).forGetter(a -> a.orientation),
                (Codec.INT.fieldOf("spacing")).orElse(1).forGetter(a -> a.spacing),
                (Codec.STRING.fieldOf("text")).forGetter(a -> a.text)).apply(instance, Config::new));
    }
}
