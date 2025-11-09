package net.lerariemann.infinity.mixin.iridescence;

import net.lerariemann.infinity.registry.core.ModStatusEffects;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEffectInstance.class)
public abstract class StatusEffectInstanceMixin {
    @Shadow public abstract Holder<MobEffect> getEffect();

    @Shadow private int duration;

    @Shadow private int amplifier;

    /* Hook for unconventional status effect ticking logic */
    @Inject(method = "tick", at = @At(target = "Lnet/minecraft/world/effect/MobEffectInstance;tickDownDuration()I", value = "INVOKE"))
    void inj(LivingEntity entity, Runnable overwriteCallback, CallbackInfoReturnable<Boolean> cir) {
        if (getEffect().value() instanceof ModStatusEffects.SpecialEffect eff) {
            eff.tryApplySpecial(entity, duration, amplifier);
        }
    }
}
