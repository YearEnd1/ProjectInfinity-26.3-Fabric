package net.lerariemann.infinity.mixin.iridescence;

import net.lerariemann.infinity.iridescence.Iridescence;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {
    @Inject(method = "isShaking", at = @At("RETURN"), cancellable = true)
    void inj(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof Mob ent && Iridescence.isConvertible(ent)) {
            cir.setReturnValue(cir.getReturnValue() || Iridescence.isUnderEffect(ent));
        }
    }
}
