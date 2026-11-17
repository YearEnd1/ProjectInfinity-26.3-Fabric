package net.codexarchonic.infinity.features;

import net.minecraft.world.level.chunk.ChunkGenerator;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.codexarchonic.infinity.util.VersionMethods;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import java.util.Optional;

public class RandomEndGatewayFeature implements Feature {
    public static final MapCodec<RandomEndGatewayFeature> CODEC = Config.CODEC.xmap(RandomEndGatewayFeature::new, feature -> feature.config);

    private final Config config;

    public RandomEndGatewayFeature(Config config) {
        this.config = config;
    }

    @Override
    public MapCodec<RandomEndGatewayFeature> codec() {
        return CODEC;
    }

    @Override
    public boolean place(WorldGenLevel levelArg, ChunkGenerator generator, RandomSource randomArg, BlockPos originArg) {
        BlockPos origin = originArg;
        WorldGenLevel structureWorldAccess = levelArg;
        BlockState bedrock = config.block.orElse(Blocks.BEDROCK.defaultBlockState());
        for (BlockPos bp : BlockPos.betweenClosed(origin.offset(-1, -2, -1), origin.offset(1, 2, 1))) {
            boolean bl = bp.getX() == origin.getX();
            boolean bl2 = bp.getY() == origin.getY();
            boolean bl3 = bp.getZ() == origin.getZ();
            boolean bl4 = Math.abs(bp.getY() - origin.getY()) == 2;
            BlockState localFluid = structureWorldAccess.getFluidState(bp).createLegacyBlock().getBlock().defaultBlockState();
            if (bl && bl2 && bl3) {
                BlockPos destination = new BlockPos(bp.getX() + randomArg.nextIntBetweenInclusive(-config.spread, config.spread),
                        bp.getY(), bp.getZ() + randomArg.nextIntBetweenInclusive(-config.spread, config.spread));
                destination = VersionMethods.clampToBounds(structureWorldAccess, destination);
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

    public record Config(int spread, Optional<BlockState> block) {
        public static final MapCodec<Config> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                (ExtraCodecs.intRange(1, 30000000).fieldOf("spread")).forGetter(a -> a.spread),
                (BlockState.CODEC.optionalFieldOf("block")).forGetter(a -> a.block)).apply(
                instance, Config::new));
    }
}
