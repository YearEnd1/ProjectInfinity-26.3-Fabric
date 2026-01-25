package net.lerariemann.infinity.mixin.qol;

import net.lerariemann.infinity.util.InfinityMethods;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.CoralBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CoralBlock.class)
public class CoralBlockBlockMixin {
    @Inject(method = "scanForWater(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Z",
            at = @At("HEAD"), cancellable = true)
    private void noCoralDecay(BlockGetter world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        try {
            if (InfinityMethods.isBiomeInfinity(world, pos)) cir.setReturnValue(true);
        } catch (IllegalStateException ignored) {}
    }
}
