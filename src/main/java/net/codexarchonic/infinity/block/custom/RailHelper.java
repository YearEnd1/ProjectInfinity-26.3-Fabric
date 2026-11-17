package net.codexarchonic.infinity.block.custom;

import net.codexarchonic.infinity.compat.CreateCompat;
import net.codexarchonic.infinity.compat.ModCompat;
import net.codexarchonic.infinity.registry.core.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class RailHelper extends BaseEntityBlock {
    public RailHelper(Properties settings) {
        super(settings);
    }


    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
        world.scheduleTick(pos, this, 1);
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        //? if <1.21.2 || fabric {
        if (ModCompat.CREATE) {
            CreateCompat.reattachRails(world, pos, getBlockEntity(world, pos));
        }else
        //?}
            world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
    }

    public static RHBEntity getBlockEntity(ServerLevel world, BlockPos pos) {
        return ((RHBEntity)world.getBlockEntity(pos));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RHBEntity(pos, state);
    }

    public static class RHBEntity extends BlockEntity {
        public ResourceLocation trackBlock;
        public String shape;

        public RHBEntity(BlockPos pos, BlockState state) {
            super(ModBlockEntities.RAIL_HELPER.get(), pos, state);
            trackBlock = null;
            shape = "xo";
        }
    }
}