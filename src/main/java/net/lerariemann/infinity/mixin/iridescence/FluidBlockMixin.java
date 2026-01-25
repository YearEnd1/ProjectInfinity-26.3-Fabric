package net.lerariemann.infinity.mixin.iridescence;

import com.llamalad7.mixinextras.sugar.Local;
import net.lerariemann.infinity.iridescence.Iridescence;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LiquidBlock.class)
public class FluidBlockMixin {
    @ModifyArg(method = "shouldSpreadLiquid", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getFluidState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/material/FluidState;", ordinal = 0))
    BlockPos shouldSpreadIridesence(BlockPos original, @Local(argsOnly = true) Level world, @Local(argsOnly = true) BlockPos pos) {
        FluidState st = world.getFluidState(original);
        if (Iridescence.isIridescence(st) && !world.getFluidState(pos).isSource()) {
            world.setBlockAndUpdate(pos, Iridescence.getRandomColorBlock(world, "glazed_terracotta").defaultBlockState());
            return pos;
        }
        return original;
    }
}
