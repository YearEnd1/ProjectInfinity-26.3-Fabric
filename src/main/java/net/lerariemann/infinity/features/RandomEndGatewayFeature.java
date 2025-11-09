package net.lerariemann.infinity.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import java.util.Optional;

public class RandomEndGatewayFeature extends Feature<RandomEndGatewayFeature.Config> {
    public RandomEndGatewayFeature(Codec<Config> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<Config> context) {
        BlockPos origin = context.origin();
        WorldGenLevel structureWorldAccess = context.level();
        Config config = context.config();
        BlockState bedrock = config.block.orElse(Blocks.BEDROCK.defaultBlockState());
        for (BlockPos bp : BlockPos.betweenClosed(origin.offset(-1, -2, -1), origin.offset(1, 2, 1))) {
            boolean bl = bp.getX() == origin.getX();
            boolean bl2 = bp.getY() == origin.getY();
            boolean bl3 = bp.getZ() == origin.getZ();
            boolean bl4 = Math.abs(bp.getY() - origin.getY()) == 2;
            BlockState localFluid = structureWorldAccess.getFluidState(bp).createLegacyBlock().getBlock().defaultBlockState();
            if (bl && bl2 && bl3) {
                BlockPos destination = new BlockPos(bp.getX() + context.random().nextIntBetweenInclusive(-config.spread, config.spread),
                        bp.getY(), bp.getZ() + context.random().nextIntBetweenInclusive(-config.spread, config.spread));
                destination = structureWorldAccess.getWorldBorder().clampToBounds(destination);
                this.setBlock(structureWorldAccess, bp, Blocks.END_GATEWAY.defaultBlockState());
                if (structureWorldAccess.getBlockEntity(bp) instanceof TheEndGatewayBlockEntity egbe) {
                    egbe.setExitPosition(destination, false);
                }
            } else if (bl2) {
                this.setBlock(structureWorldAccess, bp, localFluid);
            } else if (bl4 && bl && bl3) {
                this.setBlock(structureWorldAccess, bp, bedrock);
            } else if ((bl || bl3) && !bl4) {
                this.setBlock(structureWorldAccess, bp, bedrock);
            } else {
                this.setBlock(structureWorldAccess, bp, localFluid);
            }
        }

        return true;
    }

    public record Config(int spread, Optional<BlockState> block) implements FeatureConfiguration {
        public static final Codec<Config> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                (ExtraCodecs.intRange(1, 30000000).fieldOf("spread")).forGetter(a -> a.spread),
                (BlockState.CODEC.optionalFieldOf("block")).forGetter(a -> a.block)).apply(
                instance, Config::new));
    }
}

