package net.lerariemann.infinity.iridescence;

import net.lerariemann.infinity.registry.core.ModStatusEffects;
import net.lerariemann.infinity.util.VersionMethods;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class IridescentCooldownEffect extends MobEffect {
    public IridescentCooldownEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public
    //? if >1.21 {
    boolean
     //?} else {
    /*void
    *///?}
    applyEffectTick(
            //? if >1.21.2 {
            ServerLevel serverLevel,
            //?}
            LivingEntity entity, int amplifier) {
        int cooldownDuration = Iridescence.getCooldownDuration();
        if (cooldownDuration > 0)
            entity.addEffect(new MobEffectInstance(VersionMethods.effectOrHolder(ModStatusEffects.IRIDESCENT_COOLDOWN),
                    cooldownDuration, 0, true, false));
        //? if >1.21 {
        return true;
        //?}
    }

    @Override
    public boolean
    //? if >1.21 {
    shouldApplyEffectTickThisTick
     //?} else {
    /*isDurationEffectTick
    *///?}
            (int duration, int amplifier) {
        return amplifier != 0 && duration < 2;
    }
}
