package net.lerariemann.infinity.block.custom;

import com.mojang.serialization.MapCodec;
import net.lerariemann.infinity.block.entity.BiomeBottleBlockEntity;
import net.lerariemann.infinity.registry.core.ModBlockEntities;
import net.lerariemann.infinity.registry.core.ModComponentTypes;
import net.lerariemann.infinity.util.InfinityMethods;
import net.lerariemann.infinity.util.Methods;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
//? if >1.21 {
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.BlockItemStateProperties;
//?}
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class BiomeBottleBlock extends BaseEntityBlock {
    //? if >1.21 {
    public static final MapCodec<BiomeBottleBlock> CODEC = simpleCodec(BiomeBottleBlock::new);
    //?}
    public static final IntegerProperty LEVEL = IntegerProperty.create("level", 0, 10);
    public static final VoxelShape MAIN = Block.box(2, 0, 2, 14, 12, 14);
    public static final VoxelShape TIP = Block.box(6, 12, 6, 10, 16, 10);
    public static final VoxelShape CORK = Block.box(5, 14, 5, 11, 15, 11);
    public static final VoxelShape SHAPE = Shapes.or(MAIN, TIP, CORK);
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    public BiomeBottleBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(defaultBlockState().setValue(LEVEL, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LEVEL);
    }

    //? if >1.21 {
    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
    //?}
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BiomeBottleBlockEntity(pos, state);
    }
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.BIOME_BOTTLE.get(), world.isClientSide ? null : BiomeBottleBlockEntity::serverTick);
    }


    @Override
    public ItemStack getCloneItemStack(
            //? if >1.21 {
            LevelReader world
            //?} else {
            /*BlockGetter world
            *///?}
            , BlockPos pos, BlockState state) {
        return world.getBlockEntity(pos) instanceof BiomeBottleBlockEntity bbbe
                ? bbbe.asStack()
                : super.getCloneItemStack(world, pos, state);
    }

    public static final int maxAllowedCharge = 10000;

    public static ResourceLocation defaultBiome() {
        return Methods.ofVanilla("plains");
    }

    public static Rarity getRarity(int charge) {
        return charge < 1000 ? Rarity.COMMON : charge < 9000 ? Rarity.UNCOMMON : Rarity.RARE;
    }

    //? if >1.21 {
    public static DataComponentMap.Builder updateCharge(DataComponentMap.Builder builder, int charge) {
        return builder.set(ModComponentTypes.CHARGE.get(), charge)
                .set(DataComponents.RARITY, getRarity(charge))
                .set(DataComponents.BLOCK_STATE, (new BlockItemStateProperties(Map.of()))
                        .with(LEVEL, getLevel(charge)));
    }
    public static void updateCharge(ItemStack stack, int charge) {
        stack.applyComponents(updateCharge(DataComponentMap.builder(), charge).build());
    }
    public static void updateCharge(ItemStack stack) {
        int charge = getCharge(stack);
        if (charge == 0) stack.remove(ModComponentTypes.BIOME_CONTENTS.get());
        updateCharge(stack, charge);
    }
    //?} else {
    /*public static void updateCharge(ItemStack stack, int charge) {
        if (stack.hasTag()) {
            assert stack.getTag() != null;
            stack.getTag().getCompound("BlockEntityTag").putInt("Charge", charge);
        }
    }
    public static void updateCharge(ItemStack stack) {
        int charge = getCharge(stack);
        if (charge > 0) updateCharge(stack, charge);
    }
    *///?}

    public static int getLevel(int charge) {
        return Mth.clamp(charge / 100, 0, 10);
    }

    public static boolean isEmpty(ItemStack stack) {
        return getCharge(stack) == 0;
    }

    public static ResourceLocation getBiome(ItemStack stack) {
        //? if >1.21 {
        return stack.getComponents().getOrDefault(ModComponentTypes.BIOME_CONTENTS.get(), defaultBiome());
        //?} else {
        /*if (stack.hasTag()) {
            assert stack.getTag() != null;
            return Methods.id(stack.getTag().getCompound("BlockEntityTag").getString("Biome"));
        }
        return defaultBiome();
        *///?}
    }

    public static int getCharge(ItemStack stack) {
        //? if >1.21 {
        return stack.getComponents().getOrDefault(ModComponentTypes.CHARGE.get(), 0);
        //?} else {
        /*if (stack.hasTag()) {
            assert stack.getTag() != null;
            return stack.getTag().getCompound("BlockEntityTag").getInt("Charge");
        }
        return 0;
        *///?}

    }

    public static void playSploosh(ServerLevel world, BlockPos pos) {
        world.playSound(null, pos, SoundEvents.GENERIC_SPLASH, SoundSource.BLOCKS, 1f, 1f);
        world.sendParticles(ParticleTypes.SPLASH, pos.getX() + 0.5,
                pos.getY() + 0.5, pos.getZ() + 0.5, 30, 0.5, 0.5, 0.5, 0.2);
    }

    //? if >1.21 {
    public static DataComponentMap.Builder addComponents(DataComponentMap.Builder componentMapBuilder,
                                     ResourceLocation biome, int color, int charge) {
        componentMapBuilder.set(ModComponentTypes.BIOME_CONTENTS.get(), biome);
        componentMapBuilder.set(ModComponentTypes.COLOR.get(), color);
        updateCharge(componentMapBuilder, charge);
        return componentMapBuilder;
    }
    //?}

    public static Holder<Biome> biomeFromId(ServerLevel world, ResourceLocation id) {
        //? if >1.21 {
        Optional<Holder.Reference<Biome>> entry = world.getServer().registryAccess().registryOrThrow(Registries.BIOME).getHolder(id);
        return entry.orElse(null);
        //?} else {
        /*Registry<Biome> registry = world.getServer().registryAccess().registryOrThrow(Registries.BIOME);
        Biome biome = registry.get(id);
        return registry.wrapAsHolder(biome);
        *///?}
    }

    public static void spreadCircle(ServerLevel world, BlockPos origin, ResourceLocation biomeId, int charge) {
        spreadRing(world, origin, biomeId, 0, charge);
    }
    public static void spreadRing(ServerLevel world, BlockPos origin, ResourceLocation biomeId, int chargemin, int chargemax) {
        Set<BlockPos> posSet = new HashSet<>();
        Set<ChunkAccess> set = new HashSet<>();
        origin = origin.below(origin.getY());
        double ramax = chargemax / Math.PI;
        double ramin = chargemin / Math.PI;
        for (int i = 0; i*i < ramax; i++) {
            for (int j = 0; i*i + j*j < ramax; j++) if (i*i + j*j >= ramin) {
                List<BlockPos> signs = offsets(origin, i, j);
                posSet.addAll(signs);
                set.addAll(signs.stream().map(ChunkPos::new)
                        .map(chunkPos -> world.getChunk(chunkPos.getWorldPosition()))
                        .filter(Objects::nonNull).collect(Collectors.toSet()));
            }
        }
        spread(world, set, posSet, biomeFromId(world, biomeId));
    }

    public static void spread(ServerLevel world, Set<ChunkAccess> set, Set<BlockPos> posSet, Holder<Biome> biome) {
        if (biome == null) return;
        set.forEach(chunk -> {
            if (chunk != null) {
                chunk.fillBiomesFromNoise((x, y, z, noise) -> {
                    int i = QuartPos.toBlock(x);
                    int k = QuartPos.toBlock(z);
                    Holder<Biome> registryEntry2 = chunk.getNoiseBiome(x, y, z);
                    if (posSet.contains(new BlockPos(i, 0, k))) {
                        return biome;
                    }
                    return registryEntry2;
                }, world.getChunkSource().randomState().sampler());
                chunk.setUnsaved(true);
            }
        });
        world.getChunkSource().chunkMap.resendBiomesForChunks(set.stream().toList());
    }

    public static List<BlockPos> offsets(BlockPos origin, int i, int j) {
        return List.of(origin.offset(i, 0, j), origin.offset(j, 0, i), origin.offset(-i, 0, j), origin.offset(j, 0, -i),
                origin.offset(i, 0, -j), origin.offset(-j, 0, i), origin.offset(-i, 0, -j), origin.offset(-j, 0, -i));
    }
}
