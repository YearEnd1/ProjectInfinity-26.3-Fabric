package net.lerariemann.infinity.features;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
//? if >1.21 {
import net.minecraft.world.RandomizableContainer;
 //?} else {
/*import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
*///?}
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.slf4j.Logger;

public class RandomDungeonFeature extends Feature<RandomDungeonFeature.Config> {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final BlockState AIR = Blocks.CAVE_AIR.defaultBlockState();

    public RandomDungeonFeature(Codec<Config> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<Config> context) {
        BlockPos blockPos2;
        int u;
        int t;
        int s;
        Predicate<BlockState> predicate = Feature.isReplaceable(BlockTags.FEATURES_CANNOT_REPLACE);
        BlockPos blockPos = context.origin();
        RandomSource random = context.random();
        WorldGenLevel structureWorldAccess = context.level();
        int j = random.nextInt(2) + context.config().size();
        int k = -j - 1;
        int l = j + 1;
        int o = random.nextInt(2) + context.config().size();
        int p = -o - 1;
        int q = o + 1;
        int r = 0;
        for (s = k; s <= l; ++s) {
            for (t = -1; t <= 4; ++t) {
                for (u = p; u <= q; ++u) {
                    blockPos2 = blockPos.offset(s, t, u);
                    boolean bl = structureWorldAccess.getBlockState(blockPos2).isSolid();
                    if (t == -1 && !bl) {
                        return false;
                    }
                    if (t == 4 && !bl) {
                        return false;
                    }
                    if (s != k && s != l && u != p && u != q || t != 0 || !structureWorldAccess.isEmptyBlock(blockPos2) || !structureWorldAccess.isEmptyBlock(blockPos2.above())) continue;
                    ++r;
                }
            }
        }
        if (r < 1 || r > 5) {
            return false;
        }
        for (s = k; s <= l; ++s) {
            for (t = 3; t >= -1; --t) {
                for (u = p; u <= q; ++u) {
                    blockPos2 = blockPos.offset(s, t, u);
                    BlockState blockState = structureWorldAccess.getBlockState(blockPos2);
                    if (s == k || t == -1 || u == p || s == l || t == 4 || u == q) {
                        //? if <1.21.2 {
                        /*var minY = structureWorldAccess.getMinBuildHeight();
                         *///?} else {
                        var minY = structureWorldAccess.getMinY();
                        //?}
                        if (blockPos2.getY() >= minY && !structureWorldAccess.getBlockState(blockPos2.below()).isSolid()) {
                            structureWorldAccess.setBlock(blockPos2, AIR, Block.UPDATE_CLIENTS);
                            continue;
                        }
                        if (!blockState.isSolid() || blockState.is(Blocks.CHEST)) continue;
                        if (t == -1 && random.nextInt(4) != 0) {
                            this.safeSetBlock(structureWorldAccess, blockPos2, context.config().decorationProvider(), predicate);
                            continue;
                        }
                        this.safeSetBlock(structureWorldAccess, blockPos2, context.config().mainProvider(), predicate);
                        continue;
                    }
                    if (blockState.is(Blocks.CHEST) || blockState.is(Blocks.SPAWNER)) continue;
                    this.safeSetBlock(structureWorldAccess, blockPos2, AIR, predicate);
                }
            }
        }
        block6: for (s = 0; s < 2; ++s) {
            for (t = 0; t < 3; ++t) {
                u = blockPos.getX() + random.nextInt(j * 2 + 1) - j;
                BlockPos blockPos3 = new BlockPos(u, blockPos.getY(), blockPos.getZ() + random.nextInt(o * 2 + 1) - o);
                if (!structureWorldAccess.isEmptyBlock(blockPos3)) continue;
                int x = 0;
                for (Direction direction : Direction.Plane.HORIZONTAL) {
                    if (!structureWorldAccess.getBlockState(blockPos3.relative(direction)).isSolid()) continue;
                    ++x;
                }
                if (x != 1) continue;
                this.safeSetBlock(structureWorldAccess, blockPos3, StructurePiece.reorient(structureWorldAccess, blockPos3, Blocks.CHEST.defaultBlockState()), predicate);
                //? if >1.21 {
                RandomizableContainer.setBlockEntityLootTable
                 //?} else {
                /*RandomizableContainerBlockEntity.setLootTable
                *///?}
                (structureWorldAccess, random, blockPos3, BuiltInLootTables.SIMPLE_DUNGEON);
                continue block6;
            }
        }
        this.safeSetBlock(structureWorldAccess, blockPos, Blocks.SPAWNER.defaultBlockState(), predicate);
        BlockEntity blockEntity = structureWorldAccess.getBlockEntity(blockPos);
        if (blockEntity instanceof SpawnerBlockEntity mobSpawnerBlockEntity) {
            mobSpawnerBlockEntity.setEntityId(EntityType.byString(context.config().mob()).orElse(EntityType.PIG), random);
        } else {
            LOGGER.error("Failed to fetch mob spawner entity at ({}, {}, {})", blockPos.getX(), blockPos.getY(), blockPos.getZ());
        }
        return true;
    }

    public record Config(BlockState mainProvider, BlockState decorationProvider, String mob,
                                             int size) implements FeatureConfiguration {
        public static final Codec<Config> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                (BlockState.CODEC.fieldOf("main_state")).forGetter(a -> a.mainProvider),
                (BlockState.CODEC.fieldOf("decor_state")).forGetter(a -> a.decorationProvider),
                (Codec.STRING.fieldOf("mob")).orElse("minecraft:pig").forGetter(a -> a.mob),
                (Codec.INT.fieldOf("size")).orElse(2).forGetter(a -> a.size)).apply(
                instance, Config::new));

    }
}


