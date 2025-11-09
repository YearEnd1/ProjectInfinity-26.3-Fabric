package net.lerariemann.infinity.mixin.qol;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.lerariemann.infinity.registry.core.ModBlocks;
import net.lerariemann.infinity.iridescence.Iridescence;
import net.lerariemann.infinity.util.InfinityMethods;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.LavaFluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LavaFluid.class)
public class LavaFluidMixin {
    /* Disabling fire tick in infdims */
    @Inject(method = "onRandomTick", at = @At(value = "HEAD"), cancellable = true)
    void inj(World world, BlockPos pos, FluidState state, Random random, CallbackInfo ci) {
        if (InfinityMethods.isInfinity(world)) ci.cancel();
    }

    @ModifyExpressionValue(method = "flow", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/block/Block;getDefaultState()Lnet/minecraft/block/BlockState;"))
    BlockState inj(BlockState original, @Local(argsOnly = true) WorldAccess w, @Local(argsOnly = true) BlockPos p) {
        if (w.getBlockState(p).isOf(ModBlocks.IRIDESCENCE.get())) {
            return Iridescence.getRandomColorBlock(w, "glazed_terracotta").getDefaultState();
        }
        return original;
    }
}
