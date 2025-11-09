package net.lerariemann.infinity.mixin.core;

import net.lerariemann.infinity.registry.core.ModBlocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.PortalShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PortalShape.class)
public class NetherPortalMixin {
    /* Allows infinity portals to consider themselves valid block configurations. */
    @Inject(method = "isEmpty(Lnet/minecraft/world/level/block/state/BlockState;)Z", at = @At("RETURN"), cancellable = true)
    private static void injected(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(cir.getReturnValue() || state.is(ModBlocks.PORTAL));
    }
}
