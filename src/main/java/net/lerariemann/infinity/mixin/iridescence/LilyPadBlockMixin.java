package net.lerariemann.infinity.mixin.iridescence;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.WaterlilyBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WaterlilyBlock.class)
public class LilyPadBlockMixin {
    /* make lilypads plantable on iridescence */
    @Inject(method = "mayPlaceOn", at = @At(value = "RETURN"), cancellable = true)
    void plantLilypadsOnIridescence(BlockState floor, BlockGetter world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (world.getFluidState(pos).is(FluidTags.WATER) && world.getFluidState(pos.above()).getType() == Fluids.EMPTY) {
            cir.setReturnValue(true);
        }
    }
}
