package net.lerariemann.infinity.mixin.iridescence;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
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

    //? if >1.21.2 {
    @WrapOperation(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isFullyFrozen()Z"))
    boolean inj(LivingEntity entity, Operation<Boolean> original) {
        var frozen = original.call(entity);
        if (entity instanceof Mob ent && Iridescence.isConvertible(ent)) {
            return (frozen || Iridescence.isUnderEffect(ent));
        }
        return frozen;
    }
    //?} else {
    /*@Inject(method = "isShaking", at = @At("RETURN"), cancellable = true)
    void inj(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof Mob ent && Iridescence.isConvertible(ent)) {
            cir.setReturnValue(cir.getReturnValue() || Iridescence.isUnderEffect(ent));
        }
    }
    *///?}
}
