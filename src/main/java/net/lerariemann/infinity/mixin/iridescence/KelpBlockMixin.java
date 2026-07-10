package net.lerariemann.infinity.mixin.iridescence;

import net.lerariemann.infinity.registry.core.ModBlocks;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.KelpBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.lerariemann.infinity.iridescence.Iridescence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(KelpBlock.class)
public abstract class KelpBlockMixin extends GrowingPlantHeadBlock {
    protected KelpBlockMixin(Properties settings, Direction growthDirection, VoxelShape outlineShape, boolean tickWater, double growthChance) {
        super(settings, growthDirection, outlineShape, tickWater, growthChance);
    }

    /* A hook to correctly place kelp in iridescence (see IridescentKelpBlock) */
    @Inject(method="getStateForPlacement(Lnet/minecraft/world/item/context/BlockPlaceContext;)Lnet/minecraft/world/level/block/state/BlockState;", at= @At(value = "HEAD"), cancellable = true)
    void placeKelp(BlockPlaceContext ctx, CallbackInfoReturnable<BlockState> cir) {
        if (Iridescence.isIridescence(ctx.getLevel(), ctx.getClickedPos())) {
            BlockState blockState = ctx.getLevel().getBlockState(ctx.getClickedPos().relative(this.growthDirection));
            if (blockState.is(ModBlocks.IRIDESCENT_KELP_PLANT.get()) || blockState.is(ModBlocks.IRIDESCENT_KELP.get()))
                cir.setReturnValue(ModBlocks.IRIDESCENT_KELP_PLANT.get().defaultBlockState());
            cir.setReturnValue(ModBlocks.IRIDESCENT_KELP.get().getStateForPlacement(ctx
                    //? if <1.21.2
                    //.getLevel()
            ));
        }
    }
}
