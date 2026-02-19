package net.lerariemann.infinity.block.custom;

import com.mojang.serialization.MapCodec;
import net.lerariemann.infinity.block.entity.ChromaticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

public class ChromaticBlock extends BaseEntityBlock implements Boopable {
    //? if >1.21
    public static final MapCodec<ChromaticBlock> CODEC = simpleCodec(ChromaticBlock::new);
    public ChromaticBlock(Properties settings) {
        super(settings);
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        appendBoop(builder);
    }

    @Override
    public ItemStack getCloneItemStack(
            //? if >1.21 {
            LevelReader
             //?} else {
            /*BlockGetter
            *///?}
            world, BlockPos pos, BlockState state
    //? if >1.21.2 {
            , boolean bl
    //?}
    ) {
        ItemStack res = super.getCloneItemStack(world, pos, state
        //? if >1.21.2 {
        , true
        //?}
        );
        if (!res.isEmpty() && world.getBlockEntity(pos) instanceof ChromaticBlockEntity cbe) {
            //? if >1.21 {
            res.applyComponents(cbe.asMap());
            //?} else {
            /*res.setTag(cbe.asMap());
            *///?}
        }
        return res;
    }

    @Override
    //? if <1.21 {
    /*public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
     *///?} else {
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        //?}
        if (world.getBlockEntity(pos) instanceof ChromaticBlockEntity cbe) {
            ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
            if (cbe.onUse(world, pos, stack)) {
                return InteractionResult.SUCCESS;
            }
        }
        //? if <1.21 {
        /*return super.use(state, world, pos, player, hand, hit);
        *///?} else {
        return super.useWithoutItem(state, world, pos, player, hit);
        //?}
    }

    public static class Carpet extends ChromaticBlock {
        protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);

        public Carpet(Properties settings) {
            super(settings);
        }

        @Override
        //? if >1.21 {
        protected
        //?} else {
        /*public
        *///?}
        VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
            return SHAPE;
        }

        @Override
        //? if >1.21 {
        protected
        //?} else {
        /*public
            *///?}
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
        //? if >1.21 {
        protected
        //?} else {
        /*public
            *///?}
         boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
            return !world.isEmptyBlock(pos.below());
        }
    }
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
    //? if >1.21 {
    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
    //?}
    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ChromaticBlockEntity(pos, state);
    }
}
