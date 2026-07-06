package net.lerariemann.infinity.block.custom;

import com.mojang.serialization.MapCodec;
import net.lerariemann.infinity.block.entity.ChromaticBlockEntity;
import net.lerariemann.infinity.registry.core.ModBlocks;
import net.lerariemann.infinity.registry.core.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

public class IridescentBlock extends Block {
    public static int num_models = 24;
    public static final IntegerProperty COLOR_OFFSET = IntegerProperty.create("color", 0, num_models - 1);
    //? if >1.21
    public static final MapCodec<IridescentBlock> CODEC = simpleCodec(IridescentBlock::new);

    public IridescentBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(defaultBlockState().setValue(COLOR_OFFSET, 0));
    }

    //? if >1.21 {
    @Override
    public MapCodec<? extends IridescentBlock> codec() {
        return CODEC;
    }
    //?}

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(COLOR_OFFSET);
    }

    @Nullable
    public BlockState toStatic(BlockState state) {
        return ModBlocks.CHROMATIC_WOOL.get().defaultBlockState();
    }

    @Override
    //? if <1.21 {
    /*public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
     *///?} else {
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        //?}
        if (player.getItemInHand(InteractionHand.MAIN_HAND).is(ModItems.STAR_OF_LANG.get())) {
            BlockState state1 = toStatic(state);
            if (state1 != null) {
                world.setBlockAndUpdate(pos, state1);
                if (world.getBlockEntity(pos) instanceof ChromaticBlockEntity cbe) {
                    cbe.setColor(state.getValue(COLOR_OFFSET)*(360 / num_models), 255, 255, null);
                }
                world.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_BREAK, SoundSource.BLOCKS, 1f, 1f);
                return InteractionResult.SUCCESS;
            }
        }
        //? if <1.21 {
        /*return super.use(state, world, pos, player, hand, hit);
        *///?} else {
        return super.useWithoutItem(state, world, pos, player, hit);
         //?}
    }

    public static class Carpet extends IridescentBlock {
        protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);

        public Carpet(Properties settings) {
            super(settings);
        }

        @Override
        public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
            return SHAPE;
        }

        @Override
        public
        //? if >1.21.2 {
        BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess scheduledTickAccess, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
            //?} else {
            /*BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
             *///?}
            if (!state.canSurvive(world, pos)) return Blocks.AIR.defaultBlockState();
            //? if >1.21.2 {
            return super.updateShape(state, world, scheduledTickAccess, pos, direction, neighborPos, neighborState, random);
            //?} else {
            /*return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
             *///?}
        }

        @Override
        public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
            return !world.isEmptyBlock(pos.below());
        }

        @Override
        public BlockState toStatic(BlockState state) {
            return ModBlocks.CHROMATIC_CARPET.get().defaultBlockState();
        }
    }
}
