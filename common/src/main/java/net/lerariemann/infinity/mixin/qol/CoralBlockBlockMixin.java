package net.lerariemann.infinity.mixin.qol;

import net.lerariemann.infinity.util.InfinityMethods;
import net.minecraft.block.CoralBlockBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CoralBlockBlock.class)
public class CoralBlockBlockMixin {
    @Inject(method = "isInWater(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)Z",
            at = @At("HEAD"), cancellable = true)
    private void inj(BlockView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (InfinityMethods.isBiomeInfinity(world, pos)) cir.setReturnValue(true);
    }
}
