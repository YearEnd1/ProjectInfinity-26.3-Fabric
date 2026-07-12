package net.codexarchonic.infinity.registry.core;

import net.codexarchonic.infinity.iridescence.IridescentCooldownEffect;
import net.codexarchonic.infinity.iridescence.IridescentEffect;
import net.codexarchonic.infinity.util.InfinityMethods;
import net.codexarchonic.infinity.util.platform.InfinityPlatform;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
//? if >1.21 {
import net.minecraft.resources.ResourceLocation;
//?}
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class ModStatusEffects {
    //? if forge {
    /*public static Holder<? extends MobEffect> IRIDESCENT_EFFECT;
    public static Holder<? extends MobEffect> IRIDESCENT_SETUP;
    public static Holder<? extends MobEffect> IRIDESCENT_COOLDOWN;
    public static Holder<? extends MobEffect> AFTERGLOW;
    *///?} else {
    public static Holder<MobEffect> IRIDESCENT_EFFECT;
    public static Holder<MobEffect> IRIDESCENT_SETUP;
    public static Holder<MobEffect> IRIDESCENT_COOLDOWN;
    public static Holder<MobEffect> AFTERGLOW;
    //?}

    public static void registerModEffectsFabric() {
        //? if fabric {
        IRIDESCENT_EFFECT = (Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, InfinityMethods.getId("iridescence"),
                new IridescentEffect(MobEffectCategory.NEUTRAL, 0xFF66FF)));
        IRIDESCENT_SETUP = (Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, InfinityMethods.getId("iridescent_setup"),
                new IridescentEffect.Setup(MobEffectCategory.NEUTRAL, 0xFF00FF)));
        IRIDESCENT_COOLDOWN = (Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, InfinityMethods.getId("iridescent_cooldown"),
                new IridescentCooldownEffect(MobEffectCategory.NEUTRAL, 0x884488)));
        AFTERGLOW = (Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, InfinityMethods.getId("afterglow"),
                getAfterglowInstanceForReg()));
        //?}
    }

    public static MobEffect getAfterglowInstanceForReg() {
        //? if >1.21 {
        var add = AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL;
        ResourceLocation afterglow = InfinityMethods.getId("effect.afterglow");
        //?} else {
        /*var add = AttributeModifier.Operation.ADDITION;
        String afterglow =  "AF8B6E3F-3328-4C0A-AA36-5BA2BB9DBEF4";
        *///?}
        return new AccessibleEffect(MobEffectCategory.BENEFICIAL, 0xAA77DD)
                .addAttributeModifier(Attributes.ATTACK_SPEED,
                        afterglow, 0.1F,
                        add)
                .addAttributeModifier(Attributes.MOVEMENT_SPEED,
                        afterglow, 0.2F,
                        add)
                //? if >1.21 {
                .addAttributeModifier(Attributes.GRAVITY,
                        afterglow, -0.1F,
                        add)
               //?}
                ;
    }

    public static void registerModEffects() {
        if (InfinityPlatform.INSTANCE.isFabric()) registerModEffectsFabric();
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

