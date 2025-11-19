package net.lerariemann.infinity.mixin.fixes;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Level.class)
public class WorldMixin {

    //? if <1.21 {
    /*/^ for some reason minecraft's own code can softlock the game when beating the dragon without this check ^/
    @Inject(method = "getBlockEntity", at = @At(value = "HEAD"), cancellable = true)
    private void fixEnderDragonCrash(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (pos == null) {
            cir.setReturnValue(null);
        }
    }
    *///?}
}
