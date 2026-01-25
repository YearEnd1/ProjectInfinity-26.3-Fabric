package net.lerariemann.infinity.mixin.core;

import net.lerariemann.infinity.util.InfinityMethods;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BaseFireBlock.class)
public class AbstractFireBlockMixin {
    /* Allows to light portals in infdims. */
    @Inject(method = "inPortalDimension(Lnet/minecraft/world/level/Level;)Z", at = @At("RETURN"), cancellable = true)
    private static void lightPortalsInInfDims(Level world, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(cir.getReturnValue() || InfinityMethods.isInfinity(world));
    }
}
