package net.lerariemann.infinity.mixin.iridescence;

import net.lerariemann.infinity.registry.core.ModStatusEffects;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(MobEffectUtil.class)
public class StatusEffectUtilMixin {
    @Inject(method = "hasDigSpeed", at = @At("RETURN"), cancellable = true)
    private static void hasDigSpeedFromAfterglow(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity.hasEffect(ModStatusEffects.AFTERGLOW
        //? if <1.21
        /*.value()*/
        ))
            cir.setReturnValue(true);
    }

    @Inject(method = "getDigSpeedAmplification", at = @At("RETURN"), cancellable = true)
    private static void increaseDigSpeedWithAfterglow(LivingEntity entity, CallbackInfoReturnable<Integer> cir) {
        if (entity.hasEffect(ModStatusEffects.AFTERGLOW
        //? if <1.21
        /*.value()*/
        ))
            cir.setReturnValue(cir.getReturnValue() +
                    Objects.requireNonNull(entity.getEffect(ModStatusEffects.AFTERGLOW
                    //? if <1.21
                    /*.value()*/
                    )).getAmplifier());
    }
}
