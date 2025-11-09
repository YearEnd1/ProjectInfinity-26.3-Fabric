package net.lerariemann.infinity.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.lerariemann.infinity.InfinityMod;
import net.lerariemann.infinity.util.Methods;
import net.lerariemann.infinity.util.core.ConfigType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.RandomizableContainer;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import java.util.Optional;

public class RandomBonusChestFeature extends Feature<RandomBonusChestFeature.Config> {
    public RandomBonusChestFeature(Codec<Config> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<Config> context) {
        RandomSource random = context.random();
        WorldGenLevel structureWorldAccess = context.level();

        BlockPos blockPos = context.origin();
        if (structureWorldAccess.isEmptyBlock(blockPos) || structureWorldAccess.getBlockState(blockPos).getCollisionShape(structureWorldAccess, blockPos).isEmpty()) {
            BlockState state = context.config().block.orElseGet(Blocks.CHEST::defaultBlockState);
            structureWorldAccess.setBlock(blockPos, state, 2);
            ResourceLocation id = context.config().loot;
            if (id.toString().equals("infinity:random")) id = Methods.id(InfinityMod.provider.randomName(context.random(), ConfigType.LOOT_TABLES));
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

    public record Config(ResourceLocation loot, Optional<BlockState> block) implements FeatureConfiguration {
        public static final Codec<Config> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                (ResourceLocation.CODEC.fieldOf("loot")).forGetter(a -> a.loot),
                (BlockState.CODEC.optionalFieldOf("block")).forGetter(a -> a.block)).apply(
                instance, Config::new));
    }
}
