package net.lerariemann.infinity.iridescence;

import dev.architectury.registry.registries.RegistrySupplier;
import net.lerariemann.infinity.InfinityMod;
import net.lerariemann.infinity.registry.core.ModEntities;
import net.lerariemann.infinity.registry.core.ModStatusEffects;
import net.lerariemann.infinity.registry.payload.s2c.ShaderS2CPayload;
import net.lerariemann.infinity.registry.var.ModCriteria;
import net.lerariemann.infinity.registry.var.ModStats;
import net.lerariemann.infinity.registry.var.ModTags;
import net.lerariemann.infinity.util.VersionMethods;
import net.lerariemann.infinity.util.core.CommonIO;
import net.lerariemann.infinity.util.platform.InfinityPlatform;
import net.lerariemann.infinity.entity.custom.ChaosCreeper;
import net.lerariemann.infinity.entity.custom.ChaosPawn;
import net.lerariemann.infinity.util.InfinityMethods;
import net.lerariemann.infinity.util.core.NbtUtils;
import net.lerariemann.infinity.util.loading.ShaderLoader;
import net.lerariemann.infinity.util.teleport.WarpLogic;
import net.lerariemann.infinity.util.var.ColorLogic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
//? neoforge {
/*import net.neoforged.neoforge.event.EventHooks;
 *///?}

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Random;
//? if >1.21.2 {
import static net.minecraft.world.entity.EntitySpawnReason.CONVERSION;
//?} else {
/*import static net.minecraft.world.entity.MobSpawnType.CONVERSION;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
*///?}

public interface Iridescence {
    ResourceLocation TEXTURE = InfinityMethods.getId("block/iridescence");
    ResourceLocation FLOWING_TEXTURE = InfinityMethods.getId("block/iridescence");
    ResourceLocation OVERLAY_TEXTURE = InfinityMethods.getId("block/iridescence_overlay");

    static boolean isInfinite(Level world) {
        return switch (world.dimension().location().toString()) {
            case "infinity:chaos", "infinity:colors" -> true;
            default -> false;
        };
    }
    static boolean isIridescence(FluidState st) {
        return st.is(InfinityPlatform.INSTANCE.getIridescenceStill().get()) || st.is(InfinityPlatform.INSTANCE.getIridescenceFlowing().get());
    }
    static boolean isIridescence(BlockGetter world, BlockPos pos) {
        return Iridescence.isIridescence(world.getFluidState(pos));
    }
    static boolean isIridescentItem(ItemStack stack) {
        return stack.is(ModTags.IRIDESCENT_ITEMS);
    }

    static boolean isUnderEffect(LivingEntity entity) {
        return entity.hasEffect(VersionMethods.effectOrHolder(ModStatusEffects.IRIDESCENT_EFFECT));
    }

    static int getPosBasedColor(BlockPos pos) {
        return ColorLogic.getPureHue(InfinityMethods.sample(pos));
    }

    //todo: figure out how minecraft offsets animations to sync this with irid items' textures' animation loops
    static int getTimeBasedColor() {
        long timeMS = LocalTime.now().toNanoOfDay() / 1000000;
        int hue = (int)(timeMS % 24000);
        return ColorLogic.getPureHue(hue / 24000.0);
    }

    List<DyeColor> dyeColors = List.of(
            DyeColor.RED,
            DyeColor.ORANGE,
            DyeColor.YELLOW,
            DyeColor.LIME,
            DyeColor.GREEN,
            DyeColor.CYAN,
            DyeColor.LIGHT_BLUE,
            DyeColor.BLUE,
            DyeColor.PURPLE,
            DyeColor.MAGENTA,
            DyeColor.PINK);

    static Block getRandomColorBlock(LevelAccessor world, String str) {
        return VersionMethods.getFromRegistry(BuiltInRegistries.BLOCK, VersionMethods.id(dyeColors.get(world.getRandom().nextInt(dyeColors.size())).getName() + "_" + str));
    }
    static Block getRandomColorBlock(double d, String str) {
        return VersionMethods.getFromRegistry(BuiltInRegistries.BLOCK, VersionMethods.id(dyeColors.get((int)(d* dyeColors.size())).getName() + "_" + str));
    }

    static int getAmplifierOnApply(LivingEntity entity, int original, boolean willingly) {
        MobEffectInstance effect = entity.getEffect(VersionMethods.effectOrHolder(ModStatusEffects.IRIDESCENT_EFFECT));
        if (effect != null) {
            return effect.getAmplifier() < original ? original : -1;
        }
        MobEffectInstance cooldown = entity.getEffect(VersionMethods.effectOrHolder(ModStatusEffects.IRIDESCENT_COOLDOWN));
        if (cooldown == null) return original;
        else if (cooldown.getAmplifier() < 1) {
            if (original > 0) return original / 2;
        }
        return -1;
    }

    int ticksInHour = 1200;

    static int getFullEffectLength(int amplifier) {
        return getOnsetLength(amplifier) + getEffectLength(amplifier);
    }
    static int getEffectLength(int amplifier) { //amplifier is 0 to 4
        return getComeupLength() + getPeakLength(amplifier) + getOffsetLength(amplifier); //6 to 12 minutes
    }
    static int getOnsetLength(int amplifier) {
        return ticksInHour * Math.max(20, 60 - 5*amplifier) / 120;
    }
    static int getComeupLength() {
        return ticksInHour;
    }
    static int getPeakLength(int amplifier) {
        return ticksInHour * (4 + amplifier);
    }
    static int getOffsetLength(int amplifier) {
        return ticksInHour * (4 + amplifier) / 2;
    }
    static int getAfterglowDuration() {
        int minDuration = ticksInHour * 20;
        return (int)Math.floor(minDuration * Math.pow(2, InfinityMod.random.nextDouble(-1, 1)));
    }
    static int getCooldownDuration() {
        return ticksInHour * 20 * 7;
    }

    enum Phase {
        INITIAL,
        UPWARDS,
        PLATEAU,
        DOWNWARDS
    }

    static void clientUpdate(Player entity, int duration, int amplifier) {
        Iridescence.updateAtomics(duration, amplifier);
        if (amplifier == 0) return;
        double prog = ShaderLoader.iridProgress.get();
        if (prog > 0.5) {
            if (InfinityMod.random.nextDouble() < 0.015*(2*prog - 1)*amplifier)
                entity.playSound(SoundEvents.AMETHYST_BLOCK_CHIME, 1.0f, 1f + InfinityMod.random.nextFloat());
        }
    }

    static void updateAtomics(int duration, int amplifier) {
        ShaderLoader.iridLevel.set(amplifier);
        ShaderLoader.iridProgress.set(switch (getPhase(duration, amplifier)) {
            case INITIAL -> 0.0;
            case UPWARDS -> (getEffectLength(amplifier) - duration) / (1.0 * getComeupLength());
            case PLATEAU -> 1.0;
            case DOWNWARDS -> duration / (1.0 * getOffsetLength(amplifier));
        });
    }

    static Phase getPhase(LivingEntity entity) {
        MobEffectInstance effect = entity.getEffect(VersionMethods.effectOrHolder(ModStatusEffects.IRIDESCENT_EFFECT));
        if (effect == null) return Phase.INITIAL;
        return getPhase(effect.getDuration(), effect.getAmplifier());
    }
    static Phase getPhase(int duration, int amplifier) {
        int time_passed = getEffectLength(amplifier) - duration;
        if (time_passed < 0) return Phase.INITIAL;
        return (time_passed < getComeupLength()) ? Phase.UPWARDS : (duration <= getOffsetLength(amplifier)) ? Phase.DOWNWARDS : Phase.PLATEAU;
    }

    static boolean shouldWarp(int duration, int amplifier) {
        return (amplifier > 0) && (Iridescence.getPhase(duration, amplifier) == Phase.PLATEAU) && ((duration - getOffsetLength(amplifier)) % ticksInHour == 0);
    }
    static boolean shouldReturn(int duration, int amplifier) {
        return (amplifier > 0) && (duration == getOffsetLength(amplifier));
    }
    static boolean shouldRequestShaderLoad(int duration, int amplifier) {
        int time_passed = getEffectLength(amplifier) - duration;
        return (time_passed == 0);
    }

    static void loadShader(ServerPlayer player) {
        ShaderS2CPayload.sendTo(player, player.
                //? if >1.21.2 {
                level()
                //?} else {
                /*serverLevel()
                *///?}
                , true);
    }
    static void unloadShader(ServerPlayer player) {
        ShaderS2CPayload.sendTo(player, player.
                //? if >1.21.2 {
                level()
                //?} else {
                /*serverLevel()
                *///?}
                , false);
    }

    static boolean shouldApplyShader(@Nullable Player player) {
        if (player == null) return false;
        MobEffectInstance effect = player.getEffect(VersionMethods.effectOrHolder(ModStatusEffects.IRIDESCENT_EFFECT));
        return (effect != null && effect.getDuration() > 20
                && getPhase(effect.getDuration(), effect.getAmplifier()) != Phase.INITIAL);
    }

    static void tryBeginJourney(LivingEntity entity, int amplifier, boolean willingly) {
        amplifier = Iridescence.getAmplifierOnApply(entity, amplifier, willingly);
        if (amplifier >= 0) {
            entity.addEffect(new MobEffectInstance(VersionMethods.effectOrHolder(ModStatusEffects.IRIDESCENT_EFFECT),
                    Iridescence.getFullEffectLength(amplifier),
                    amplifier, true, true));
            entity.removeEffect(VersionMethods.effectOrHolder(ModStatusEffects.IRIDESCENT_COOLDOWN));
            int cooldownDuration = Iridescence.getCooldownDuration();
            if (cooldownDuration > 0)
                entity.addEffect(new MobEffectInstance(VersionMethods.effectOrHolder(ModStatusEffects.IRIDESCENT_COOLDOWN),
                        cooldownDuration, amplifier > 0 ? 1 : 0, true, false));
            if (entity instanceof ServerPlayer player) {
                ModCriteria.IRIDESCENT.get().trigger(player, willingly, amplifier);
            }
        }
    }

    static void saveCookie(ServerPlayer player) {
        CompoundTag compound = new CompoundTag();
        compound.putDouble("x", player.position().x);
        compound.putDouble("y", player.position().y);
        compound.putDouble("z", player.position().z);
        compound.putString("dim", player.level().dimension().location().toString());
        CommonIO.write(compound, InfinityMod.provider.savingPath, player.getStringUUID() + ".json");
    }

    static void endJourney(ServerPlayer player, boolean isEarlyCancel, int amplifier) {
        player.setInvulnerable(false);
        if (!isEarlyCancel) {
            player.awardStat(ModStats.IRIDESCENCE, 1);
            if (amplifier != 0)
                player.addEffect(new MobEffectInstance(VersionMethods.effectOrHolder(ModStatusEffects.AFTERGLOW),
                        getAfterglowDuration(), 0, true, true));
        }
        Path cookie = InfinityMod.provider.savingPath.resolve(player.getStringUUID() + ".json");
        CompoundTag comp = CommonIO.read(cookie);
        ServerLevel level = player.level().getServer().getLevel(ResourceKey.create(Registries.DIMENSION, VersionMethods.id(NbtUtils.getString(comp,"dim"))));
        if (level != null) {
            //? if >1.21.2 {
            VersionMethods.teleportTo(player, level, new Vec3(comp.getDoubleOr("x", 0), comp.getDoubleOr("y", 0), comp.getDoubleOr("z", 0)), player.getYRot(), player.getXRot());
            //?} else {
            /*player.teleportTo(level, comp.getDouble("x"), comp.getDouble("y"), comp.getDouble("z"), player.getYRot(), player.getXRot());
            *///?}
        }
        else WarpLogic.respawnAlive(player);
        try {
            Files.deleteIfExists(cookie);
        } catch (Exception ignored) {
        }
    }

    static ResourceLocation getIdForWarp(ServerPlayer player) {
        ServerLevel w = player.level().getServer().overworld();
        return InfinityMethods.getRandomId(new Random(w.getSeed() + w.getGameTime() / ticksInHour));
    }

    Map<EntityType<? extends Mob>, RegistrySupplier<? extends EntityType<? extends Mob>>> convertibles =
            Map.ofEntries(Map.entry(EntityType.SKELETON, ModEntities.CHAOS_SKELETON),
            Map.entry(EntityType.CREEPER, ModEntities.CHAOS_CREEPER),
            Map.entry(EntityType.SLIME, ModEntities.CHAOS_SLIME)
    );

    static boolean isConvertible(Mob entity) {
        return (convertibles.containsKey(entity.getType()) || (entity instanceof ChaosPawn pawn && pawn.isBlackOrWhite()));
    }

    static void tryApplyEffect(Mob ent) {
        if (!ent.hasEffect(VersionMethods.effectOrHolder(ModStatusEffects.IRIDESCENT_EFFECT))) {
            if (ent instanceof AbstractFish)
                ent.addEffect(new MobEffectInstance(VersionMethods.effectOrHolder(ModStatusEffects.IRIDESCENT_EFFECT), ticksInHour, 0,
                        true, false));
            else if (isConvertible(ent))
                ent.addEffect(new MobEffectInstance(VersionMethods.effectOrHolder(ModStatusEffects.IRIDESCENT_EFFECT), ticksInHour, 0,
                        true, true));
        }
    }

    static void endConversion(Mob currEntity) {
        EntityType<?> type = currEntity.getType();
        if (!convertibles.containsKey(type)) return;
        EntityType<? extends Mob> typeNew = convertibles.get(type).get();
        if (typeNew != null) {
            Mob newEntity = typeNew.create(currEntity.level()
                    //? if >1.21.2
                    , CONVERSION
            );
            if (newEntity != null) {
                currEntity.discard();
                ModEntities.copy(currEntity, newEntity);
                if (newEntity instanceof ChaosCreeper creeper) {
                    Holder<Biome> b = creeper.level().getBiome(creeper.blockPosition());
                    creeper.setBiome(VersionMethods.getRegisteredName(b));
                    creeper.setColor(b.value().getFoliageColor());
                    creeper.setRandomCharge();
                }
                else if (currEntity.level() instanceof ServerLevel w)

                //? fabric || forge {
                newEntity.finalizeSpawn(
                 //?}
                //? neoforge {
                /*EventHooks.finalizeMobSpawn(newEntity,
                *///?}
                w, w.getCurrentDifficultyAt(currEntity.blockPosition()), CONVERSION, null
                        //? if <1.21 {
                        /*, null
                        *///?}
                );
                if (currEntity instanceof Slime e1 && newEntity instanceof Slime e2) {
                    e2.setSize(e1.getSize(), true);
                }
                currEntity.level().addFreshEntity(newEntity);
                newEntity.playSound(SoundEvents.ZOMBIE_VILLAGER_CONVERTED, 1.0f, 1.0f);
                convTriggers(currEntity);
            }
        }
    }

    static void convTriggers(LivingEntity entity) {
        triggerConversion(entity.level().getNearestPlayer(entity.getX(), entity.getY(), entity.getZ(),
                50, false), entity);
        //? if >1.21.2 {
        entity.level().getEntities(entity, AABB.ofSize(entity.position(), 10,10, 10))
                .forEach(p -> triggerConversion(p, entity));
        //?} else {
        /*entity.level().getNearbyPlayers(TargetingConditions.DEFAULT, entity, AABB.ofSize(entity.position(), 10,10, 10))
                .forEach(p -> triggerConversion(p, entity));
        *///?}

    }

    static void triggerConversion(Entity potentialPlayer, LivingEntity entity) {
        if (potentialPlayer instanceof ServerPlayer np) {
            ModCriteria.CONVERT_MOB.get().trigger(np, entity);
        }
    }
}
