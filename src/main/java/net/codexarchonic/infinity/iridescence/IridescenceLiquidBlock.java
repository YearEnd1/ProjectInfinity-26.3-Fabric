package net.codexarchonic.infinity.iridescence;

import dev.architectury.core.block.ArchitecturyLiquidBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
//? if >1.21.4 {
import net.minecraft.world.entity.InsideBlockEffectApplier;
//?}
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;

import java.util.function.Supplier;

public class IridescenceLiquidBlock extends ArchitecturyLiquidBlock {
    public IridescenceLiquidBlock(Supplier<? extends FlowingFluid> fluid, Properties properties) {
        super(fluid, properties);
    }

    @Override
    public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity
            //? if >1.21.5 {
            , InsideBlockEffectApplier applier
            //?}
             //? if >1.21.9 {
            /*, boolean intersects
             *///?}
    ) {
        super.entityInside(state, world, pos, entity
                //? if >1.21.5 {
                , applier
                //?}
                //? if >1.21.9 {
                /*, intersects
                 *///?}
        );
        if (world.getFluidState(pos).getAmount() > 3 && world instanceof ServerLevel w)
            Iridescence.touchIridescence(entity, w);
    }
}
