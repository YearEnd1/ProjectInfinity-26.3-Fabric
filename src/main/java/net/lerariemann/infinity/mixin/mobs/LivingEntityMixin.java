package net.lerariemann.infinity.mixin.mobs;

import net.lerariemann.infinity.registry.core.ModStatusEffects;
import net.minecraft.core.Holder;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.lerariemann.infinity.options.InfinityOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Shadow public abstract double getAttributeValue(Holder<Attribute> attribute);
    protected LivingEntityMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    /* Hook to allow unconventional effect removal logic */
    @Inject(method = "onEffectRemoved", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;refreshDirtyAttributes()V"))
    void inj(MobEffectInstance effect, CallbackInfo ci) {
        if (effect.getEffect().value() instanceof ModStatusEffects.SpecialEffect eff) {
            eff.onRemoved((LivingEntity)(Object)this);
        }
    }

    /* Hook for sheep dropping wool when punched */
    @Inject(method = "hurt", at = @At("RETURN"))
    protected void injected_sheep(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {}

    /* Handle fall damage in dimensions with custom mavity */
    @Inject(method = "calculateFallDamage", at = @At(value = "HEAD"), cancellable = true)
    protected void inj2(float fallDistance, float damageMultiplier, CallbackInfoReturnable<Integer> cir) {
        InfinityOptions options = InfinityOptions.access(level());
        if (!options.isEmpty()) {
            if (this.getType().is(EntityTypeTags.FALL_DAMAGE_IMMUNE)) cir.setReturnValue(0);
            else {
                double f = getAttributeValue(Attributes.SAFE_FALL_DISTANCE);
                double clampedMavity = Math.clamp(options.getMavity(), 0.01, 2);
                double g = fallDistance * clampedMavity - f;
                cir.setReturnValue(Mth.ceil(
                        (g * damageMultiplier) * this.getAttributeValue(Attributes.FALL_DAMAGE_MULTIPLIER)));
            }
        }
    }
}
