package net.lerariemann.infinity.mixin.qol;

import net.lerariemann.infinity.util.InfinityMethods;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseCoralPlantTypeBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//Prevents coral in infinite dimensions from drying up.
@Mixin(BaseCoralPlantTypeBlock.class)
public class CoralParentBlockMixin {
    @Inject(method = "scanForWater(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Z",
    at = @At("HEAD"), cancellable = true)
    private static void noParentCoralDecay(BlockState state, BlockGetter world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        try {
            if (InfinityMethods.isBiomeInfinity(world, pos)) cir.setReturnValue(true);
        } catch (IllegalStateException ignored) {}
    }
}
