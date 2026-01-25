package net.lerariemann.infinity.mixin.mobs;

import net.lerariemann.infinity.registry.core.ModStatusEffects;
import net.lerariemann.infinity.util.VersionMethods;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.level.Level;
import net.lerariemann.infinity.options.InfinityOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//? if >1.21 {
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.Collection;
//?}

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    protected LivingEntityMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    /* Hook to allow unconventional effect removal logic */
    //? if >1.21 {
    @Shadow public abstract double getAttributeValue(Holder<Attribute> attribute);
    //?}

    //? if >1.21.2 {
    @Inject(method = "onEffectsRemoved", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;refreshDirtyAttributes()V"))
    void removeSpecialEffects(Collection<MobEffectInstance> effects, CallbackInfo ci) {
        effects.forEach(effect-> {
            if (VersionMethods.unholdEffect(effect.getEffect()) instanceof ModStatusEffects.SpecialEffect eff) {
                eff.onRemoved((LivingEntity)(Object)this);
            }
        });
    }
    //?} else if =1.21.1 {
    /*@Inject(method = "onEffectRemoved", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;refreshDirtyAttributes()V"))
    *///?} else if =1.20.1 {
    /*@Inject(method = "onEffectRemoved", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getAttributes()Lnet/minecraft/world/entity/ai/attributes/AttributeMap;"))
    *///?}
    //? if <1.21.2 {
    /*void removeSpecialEffects(MobEffectInstance effect, CallbackInfo ci) {
        if (VersionMethods.unholdEffect(effect.getEffect()) instanceof ModStatusEffects.SpecialEffect eff) {
            eff.onRemoved((LivingEntity)(Object)this);
        }
    }
    *///?}

    /* Hook for sheep dropping wool when punched */
    //? if >1.21.5 {
    @Inject(method = "hurtServer", at = @At("RETURN"))
    protected void injected_sheep(ServerLevel level, DamageSource damageSource, float amount, CallbackInfoReturnable<Boolean> cir) {}
    //?} else {
    /*@Inject(method = "hurt", at = @At("RETURN"))
    protected void injected_sheep(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {}
    *///?}

    /* Handle fall damage in dimensions with custom mavity */
    @Inject(method = "calculateFallDamage", at = @At(value = "HEAD"), cancellable = true)
    protected void handleMavityFallDamage(
            //? if >1.21.2 {
            double
            //?} else {
            /*float
            *///?}
                    fallDistance, float damageMultiplier, CallbackInfoReturnable<Integer> cir) {
        InfinityOptions options = InfinityOptions.access(level());
        if (!options.isEmpty()) {
            if (this.getType().is(EntityTypeTags.FALL_DAMAGE_IMMUNE)) cir.setReturnValue(0);
            else {
                double f =
                //? if >1.21 {
                getAttributeValue(Attributes.SAFE_FALL_DISTANCE);
                //?} else {
                /*3.0;
                *///?}
                double clampedMavity = Mth.clamp(options.getMavity(), 0.01, 2);
                double g = fallDistance * clampedMavity - f;
                double h =
                //? if >1.21 {
                getAttributeValue(Attributes.FALL_DAMAGE_MULTIPLIER);
                //?} else {
                /*1.0;
                *///?}
                cir.setReturnValue(Mth.ceil((g * damageMultiplier) * h));
            }
        }
    }
}
