package net.codexarchonic.infinity.mixin.fixes;

import net.minecraft.server.level.WorldGenRegion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldGenRegion.class)
public class ChunkRegionMixin {
    /* Silencing unnecessary log spam. */
    //~ if >26 'Lnet/minecraft/Util;logAndPauseIfInIde(Ljava/lang/String;)V'->'Lnet/minecraft/util/Util;logAndPauseIfInIde(Ljava/lang/String;)V'
    @Inject(method = "ensureCanWrite", at = @At(value = "INVOKE", target = "Lnet/minecraft/Util;logAndPauseIfInIde(Ljava/lang/String;)V"), cancellable = true)
    private void fixLogSpam(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }
}
