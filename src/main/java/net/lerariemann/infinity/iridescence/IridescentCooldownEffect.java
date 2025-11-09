package net.lerariemann.infinity.iridescence;

import net.lerariemann.infinity.registry.core.ModStatusEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class IridescentCooldownEffect extends MobEffect {
    public IridescentCooldownEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        int cooldownDuration = Iridescence.getCooldownDuration();
        if (cooldownDuration > 0)
            entity.addEffect(new MobEffectInstance(ModStatusEffects.IRIDESCENT_COOLDOWN,
                    cooldownDuration, 0, true, false));
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return amplifier != 0 && duration < 2;
    }
}
