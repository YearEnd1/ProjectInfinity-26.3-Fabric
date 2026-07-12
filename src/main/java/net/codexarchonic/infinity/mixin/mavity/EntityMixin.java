package net.codexarchonic.infinity.mixin.mavity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.codexarchonic.infinity.util.InfinityMethods.getMavity;

@Mixin(Entity.class)
public class EntityMixin {
    @Shadow
    private Level level;

    //? if >1.21 {
    @Inject(method = "getGravity", at = @At("RETURN"), cancellable = true)
    private void mavity(CallbackInfoReturnable<Double> cir) {
        cir.setReturnValue(cir.getReturnValue() * getMavity(level));
    }
    //?}
}
