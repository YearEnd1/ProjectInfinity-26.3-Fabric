package net.codexarchonic.infinity.features;

import net.minecraft.world.level.chunk.ChunkGenerator;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.codexarchonic.infinity.InfinityMod;
import net.codexarchonic.infinity.util.VersionMethods;
import net.codexarchonic.infinity.util.core.ConfigType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.RandomizableContainer;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import java.util.Optional;

public class RandomBonusChestFeature implements Feature {
    public static final MapCodec<RandomBonusChestFeature> CODEC = Config.CODEC.xmap(RandomBonusChestFeature::new, feature -> feature.config);

    private final Config config;

    public RandomBonusChestFeature(Config config) {
        this.config = config;
    }

    @Override
    public MapCodec<RandomBonusChestFeature> codec() {
        return CODEC;
    }

    @Override
    public boolean place(WorldGenLevel levelArg, ChunkGenerator generator, RandomSource randomArg, BlockPos originArg) {
        RandomSource random = randomArg;
        WorldGenLevel structureWorldAccess = levelArg;

        BlockPos blockPos = originArg;
        if (structureWorldAccess.isEmptyBlock(blockPos) || structureWorldAccess.getBlockState(blockPos).getCollisionShape(structureWorldAccess, blockPos).isEmpty()) {
            BlockState state = config.block.orElseGet(Blocks.CHEST::defaultBlockState);
            structureWorldAccess.setBlock(blockPos, state, 2);
            Identifier id = config.loot;
            if (id.toString().equals("infinity:random")) id = VersionMethods.id(InfinityMod.provider.randomName(randomArg, ConfigType.LOOT_TABLES));
            RandomizableContainer.setBlockEntityLootTable(structureWorldAccess, random, blockPos, ResourceKey.create(Registries.LOOT_TABLE, id));
            BlockState blockState = Blocks.TORCH.defaultBlockState();

            for (Direction direction : Direction.Plane.HORIZONTAL) {
                BlockPos blockPos2 = blockPos.relative(direction);
                if (blockState.canSurvive(structureWorldAccess, blockPos2) && structureWorldAccess.getFluidState(blockPos2).isEmpty())
                    structureWorldAccess.setBlock(blockPos2, blockState, 2);
            }
            return true;
        }
        return false;
    }

    public record Config(Identifier loot, Optional<BlockState> block) {
        public static final MapCodec<Config> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                (Identifier.CODEC.fieldOf("loot")).forGetter(a -> a.loot),
                (BlockState.CODEC.optionalFieldOf("block")).forGetter(a -> a.block)).apply(
                instance, Config::new));
    }
}
