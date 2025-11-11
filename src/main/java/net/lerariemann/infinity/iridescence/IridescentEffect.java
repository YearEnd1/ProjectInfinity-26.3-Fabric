package net.lerariemann.infinity.iridescence;

import net.lerariemann.infinity.InfinityMod;
import net.lerariemann.infinity.entity.custom.ChaosPawn;
import net.lerariemann.infinity.options.InfinityOptions;
import net.lerariemann.infinity.util.VersionMethods;
import net.lerariemann.infinity.util.core.RandomProvider;
import net.lerariemann.infinity.util.loading.ShaderLoader;
import net.lerariemann.infinity.util.teleport.WarpLogic;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.InstantenousMobEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import net.lerariemann.infinity.registry.core.ModStatusEffects;
import java.util.Objects;

//? if >1.21 {
/*import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.FastColor;
import java.awt.Color;
 *///?}

import static net.lerariemann.infinity.iridescence.Iridescence.*;

public class IridescentEffect extends MobEffect implements ModStatusEffects.SpecialEffect {
    public IridescentEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    //? if >1.21 {
    /*public void onEffectStarted(LivingEntity entity, int amplifier) {
        super.onEffectStarted(entity, amplifier);
    *///?} else {
    public void addAttributeModifiers(LivingEntity entity, AttributeMap attributes, int amplifier) {
        super.addAttributeModifiers(entity, attributes, amplifier);
    //?}
        if (entity.hasEffect(VersionMethods.effectOrHolder(ModStatusEffects.IRIDESCENT_SETUP))) {
            entity.removeEffect(VersionMethods.effectOrHolder(ModStatusEffects.IRIDESCENT_SETUP));
        }
        if (entity instanceof NeutralMob ang) ang.stopBeingAngry();
        if (entity instanceof ServerPlayer player
                && shouldApplyShader(player))
            loadShader(player);
    }

    public void onRemoved(LivingEntity entity) {
        if (Objects.requireNonNull(entity) instanceof Player player) {
            if (player instanceof ServerPlayer serverPlayer) {
                if (player.isInvulnerable()) player.setInvulnerable(false);
                if (getPhase(entity) == Phase.PLATEAU) endJourney(serverPlayer, true, 0);
                unloadShader(serverPlayer);
            } else {
                ShaderLoader.reloadShaders(Minecraft.getInstance(), InfinityOptions.ofClient().data, false);
            }
        } else if (entity instanceof ChaosPawn pawn) {
            if (pawn.getRandom().nextBoolean()) {
                pawn.unchess();
                pawn.playSound(SoundEvents.ZOMBIE_VILLAGER_CONVERTED, 1.0f, 1.0f);
                convTriggers(pawn);
            }
        } else if (entity instanceof Mob currEntity) {
            endConversion(currEntity);
        }
    }

    @Override
    public void tryApplySpecial(LivingEntity entity, int duration, int amplifier) {
        if (entity instanceof Player p) {
            if (p instanceof ServerPlayer player) {
                if (shouldWarp(duration, amplifier)) {
                    if (Iridescence.getPhase(duration + ticksInHour, amplifier) != Phase.PLATEAU) { //the first warp
                        if (RandomProvider.rule("iridSafeMode")) player.setInvulnerable(true);
                        saveCookie(player);
                    }
                    ResourceLocation id = getIdForWarp(player);
                    WarpLogic.requestWarp(player, id, false);
                }
                if (shouldReturn(duration, amplifier)) {
                    endJourney(player, false, amplifier);
                }
                if (shouldRequestShaderLoad(duration, amplifier))
                    loadShader(player);
                if (amplifier == 0 && duration == 2) {
                    player.addEffect(new MobEffectInstance(VersionMethods.effectOrHolder(ModStatusEffects.AFTERGLOW),
                            getAfterglowDuration() / 2, 0, true, true));
                }
            }
            else {
                Iridescence.updateAtomics(duration, amplifier);
                if (amplifier == 0) return;
                double prog = ShaderLoader.iridProgress.get();
                if (prog > 0.5) {
                    if (InfinityMod.random.nextDouble() < 0.015*(2*prog - 1)*amplifier)
                        entity.playSound(SoundEvents.AMETHYST_BLOCK_CHIME, 1.0f, 1f + InfinityMod.random.nextFloat());
                }
            }
        }
    }

    //? if >1.21 {
    /*@Override
    public ParticleOptions createParticleOptions(MobEffectInstance effect) {
        float hue = effect.getDuration() / 13.0f;
        return ColorParticleOption.create(
                ParticleTypes.ENTITY_EFFECT, FastColor.ARGB32.opaque(Color.HSBtoRGB(hue - (int)hue, 1.0f, 1.0f)));
    }
    *///?}

    public static class Setup extends InstantenousMobEffect {
        public Setup(MobEffectCategory category, int color) {
            super(category, color);
        }

        @Override
        //? if >1.21 {
        /*public void onEffectStarted(LivingEntity entity, int amplifier) {
            super.onEffectStarted(entity, amplifier);
        *///?} else {
        public void addAttributeModifiers(LivingEntity entity, AttributeMap attributes, int amplifier) {
            super.addAttributeModifiers(entity, attributes, amplifier);
        //?}
            tryBeginJourney(entity, amplifier, true);
        }
    }
}
