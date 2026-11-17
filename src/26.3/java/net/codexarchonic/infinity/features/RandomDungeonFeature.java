package net.codexarchonic.infinity.features;

import net.minecraft.world.level.chunk.ChunkGenerator;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.Codec;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.RandomizableContainer;
import java.util.Optional;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.slf4j.Logger;

public class RandomDungeonFeature implements Feature {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final BlockState AIR = Blocks.CAVE_AIR.defaultBlockState();

    public static final MapCodec<RandomDungeonFeature> CODEC = Config.CODEC.xmap(RandomDungeonFeature::new, feature -> feature.config);

    private final Config config;

    public RandomDungeonFeature(Config config) {
        this.config = config;
    }

    @Override
    public MapCodec<RandomDungeonFeature> codec() {
        return CODEC;
    }

    @Override
    public boolean place(WorldGenLevel levelArg, ChunkGenerator generator, RandomSource randomArg, BlockPos originArg) {
        BlockPos blockPos2;
        int u;
        int t;
        int s;
        Predicate<BlockState> predicate = state -> !state.is(BlockTags.FEATURES_CANNOT_REPLACE);
        BlockPos blockPos = originArg;
        RandomSource random = randomArg;
        WorldGenLevel structureWorldAccess = levelArg;
        int j = random.nextInt(2) + config.size();
        int k = -j - 1;
        int l = j + 1;
        int o = random.nextInt(2) + config.size();
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
                        var minY = structureWorldAccess.getMinY();
                        if (blockPos2.getY() >= minY && !structureWorldAccess.getBlockState(blockPos2.below()).isSolid()) {
                            structureWorldAccess.setBlock(blockPos2, AIR, Block.UPDATE_CLIENTS);
                            continue;
                        }
                        if (!blockState.isSolid() || blockState.is(Blocks.CHEST)) continue;
                        if (t == -1 && random.nextInt(4) != 0) {
                            this.safeSetBlock(structureWorldAccess, blockPos2, config.decorationProvider(), predicate);
                            continue;
                        }
                        this.safeSetBlock(structureWorldAccess, blockPos2, config.mainProvider(), predicate);
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
                RandomizableContainer.setBlockEntityLootTable
                (structureWorldAccess, random, blockPos3, BuiltInLootTables.SIMPLE_DUNGEON);
                continue block6;
            }
        }
        this.safeSetBlock(structureWorldAccess, blockPos, Blocks.SPAWNER.defaultBlockState(), predicate);
        BlockEntity blockEntity = structureWorldAccess.getBlockEntity(blockPos);
        if (blockEntity instanceof SpawnerBlockEntity mobSpawnerBlockEntity) {
            mobSpawnerBlockEntity.setEntityId(Optional.ofNullable(Identifier.tryParse(config.mob())).flatMap(BuiltInRegistries.ENTITY_TYPE::getOptional).orElse(EntityTypes.PIG), random);
        } else {
            LOGGER.error("Failed to fetch mob spawner entity at ({}, {}, {})", blockPos.getX(), blockPos.getY(), blockPos.getZ());
        }
        return true;
    }

    public record Config(BlockState mainProvider, BlockState decorationProvider, String mob,
                                             int size) {
        public static final MapCodec<Config> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                (BlockState.CODEC.fieldOf("main_state")).forGetter(a -> a.mainProvider),
                (BlockState.CODEC.fieldOf("decor_state")).forGetter(a -> a.decorationProvider),
                (Codec.STRING.fieldOf("mob")).orElse("minecraft:pig").forGetter(a -> a.mob),
                (Codec.INT.fieldOf("size")).orElse(2).forGetter(a -> a.size)).apply(
                instance, Config::new));

    }
}
