package net.lerariemann.infinity.block.custom;

import net.lerariemann.infinity.registry.core.ModBlocks;
import net.lerariemann.infinity.util.platform.PlatformMethods;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.KelpBlock;
import net.minecraft.world.level.block.KelpPlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public class IridescentKelpBlock extends KelpBlock {
    public IridescentKelpBlock(Properties settings) {
        super(settings);
    }

    @Override
    protected boolean canGrowInto(BlockState state) {
        return state.is(ModBlocks.IRIDESCENCE.get());
    }

    @Override
    //? if >1.21 {
    protected
    //?} else {
    /*public
    *///?}
    Block getBodyBlock() {
        return ModBlocks.IRIDESCENT_KELP_PLANT.get();
    }

    public static class Plant extends KelpPlantBlock {
        public Plant(Properties settings) {
            super(settings);
        }

        @Override
        //? if >1.21 {
        protected
        //?} else {
        /*public
        *///?}
        FluidState getFluidState(BlockState state) {
            return PlatformMethods.getIridescenceStill().get().getSource(false);
        }

        @Override
        protected GrowingPlantHeadBlock getHeadBlock() {
            return ModBlocks.IRIDESCENT_KELP.get();
        }
    }
}
