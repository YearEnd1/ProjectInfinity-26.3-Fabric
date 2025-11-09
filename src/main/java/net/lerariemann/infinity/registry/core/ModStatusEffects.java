package net.lerariemann.infinity.registry.core;

import dev.architectury.platform.Platform;
import net.lerariemann.infinity.iridescence.IridescentCooldownEffect;
import net.lerariemann.infinity.iridescence.IridescentEffect;
import net.lerariemann.infinity.util.InfinityMethods;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class ModStatusEffects {
    public static Holder<MobEffect> IRIDESCENT_EFFECT;
    public static Holder<MobEffect> IRIDESCENT_SETUP;
    public static Holder<MobEffect> IRIDESCENT_COOLDOWN;
    public static Holder<MobEffect> AFTERGLOW;

    public static void registerModEffectsFabric() {
        IRIDESCENT_EFFECT = Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, InfinityMethods.getId("iridescence"),
                new IridescentEffect(MobEffectCategory.NEUTRAL, 0xFF66FF));
        IRIDESCENT_SETUP = Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, InfinityMethods.getId("iridescent_setup"),
                new IridescentEffect.Setup(MobEffectCategory.NEUTRAL, 0xFF00FF));
        IRIDESCENT_COOLDOWN = Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, InfinityMethods.getId("iridescent_cooldown"),
                new IridescentCooldownEffect(MobEffectCategory.NEUTRAL, 0x884488));
        AFTERGLOW = Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, InfinityMethods.getId("afterglow"),
                getAfterglowInstanceForReg());
    }

    public static MobEffect getAfterglowInstanceForReg() {
        return new AccessibleEffect(MobEffectCategory.BENEFICIAL, 0xAA77DD)
                .addAttributeModifier(Attributes.ATTACK_SPEED,
                        InfinityMethods.getId("effect.afterglow"), 0.1F,
                        AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .addAttributeModifier(Attributes.MOVEMENT_SPEED,
                        InfinityMethods.getId("effect.afterglow"), 0.2F,
                        AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .addAttributeModifier(Attributes.GRAVITY,
                        InfinityMethods.getId("effect.afterglow"), -0.1F,
                        AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }

    public static void registerModEffects() {
        if (Platform.isFabric()) registerModEffectsFabric();
    }

    public static class AccessibleEffect extends MobEffect {
        public AccessibleEffect(MobEffectCategory category, int color) {
            super(category, color);
        }
    }
    public interface SpecialEffect {
        void onRemoved(LivingEntity entity);
        void tryApplySpecial(LivingEntity entity, int duration, int amplifier);
    }
}

