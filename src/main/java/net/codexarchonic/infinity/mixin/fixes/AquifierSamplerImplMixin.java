package net.codexarchonic.infinity.mixin.fixes;

import net.minecraft.world.level.levelgen.Aquifer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Aquifer.NoiseBasedAquifer.class)
public class AquifierSamplerImplMixin {
    @Mutable
    @Final
    @Shadow
    private long[] aquiferLocationCache;

    /* for some reason minecraft's own code can fail with an array out of bounds exception if i don't clamp this */
    @Inject(method = "getIndex(III)I", at = @At("RETURN"), cancellable = true)
    void fixAquifers(int x, int y, int z, CallbackInfoReturnable<Integer> cir) {
        int i = cir.getReturnValue();
        if (i >= aquiferLocationCache.length) cir.setReturnValue(aquiferLocationCache.length - 1);
    }
}
