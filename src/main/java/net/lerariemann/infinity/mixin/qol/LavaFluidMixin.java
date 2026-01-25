package net.lerariemann.infinity.mixin.qol;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.lerariemann.infinity.registry.core.ModBlocks;
import net.lerariemann.infinity.iridescence.Iridescence;
import net.lerariemann.infinity.util.InfinityMethods;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.LavaFluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LavaFluid.class)
public class LavaFluidMixin {
    /* Disabling fire tick in infdims */
    @Inject(method = "randomTick", at = @At(value = "HEAD"), cancellable = true)
    void inj(
            //? if >1.21.2 {
            ServerLevel
            //?} else {
            /*Level
            *///?}
                    world, BlockPos pos, FluidState state, RandomSource random, CallbackInfo ci) {
        if (InfinityMethods.isInfinity(world)) ci.cancel();
    }

    @ModifyExpressionValue(method = "spreadTo", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Block;defaultBlockState()Lnet/minecraft/world/level/block/state/BlockState;"))
    BlockState customCobbleGen(BlockState original, @Local(argsOnly = true) LevelAccessor w, @Local(argsOnly = true) BlockPos p) {
        if (w.getBlockState(p).is(ModBlocks.IRIDESCENCE.get())) {
            return Iridescence.getRandomColorBlock(w, "glazed_terracotta").defaultBlockState();
        }
        return original;
    }
}
