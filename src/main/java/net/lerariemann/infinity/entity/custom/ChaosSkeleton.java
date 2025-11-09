package net.lerariemann.infinity.entity.custom;

import net.lerariemann.infinity.InfinityMod;
import net.lerariemann.infinity.registry.core.ModEntities;
import net.lerariemann.infinity.util.Methods;
import net.lerariemann.infinity.util.core.ConfigType;
import net.lerariemann.infinity.util.core.NbtUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ChaosSkeleton extends Skeleton implements TintableEntity {
    private static final EntityDataAccessor<String> effect = SynchedEntityData.defineId(ChaosSkeleton.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Integer> color = SynchedEntityData.defineId(ChaosSkeleton.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> duration = SynchedEntityData.defineId(ChaosSkeleton.class, EntityDataSerializers.INT);
    public static Map<String, String> effect_lookup = Map.ofEntries(Map.entry("minecraft:unluck", "minecraft:luck"),
            Map.entry("minecraft:bad_omen", "minecraft:hero_of_the_village"),
            Map.entry("minecraft:darkness", "minecraft:night_vision"),
            Map.entry("minecraft:blindness", "minecraft:night_vision"),
            Map.entry("minecraft:glowing", "minecraft:invisibility"),
            Map.entry("minecraft:hunger", "minecraft:saturation"),
            Map.entry("minecraft:levitation", "minecraft:slow_falling"),
            Map.entry("minecraft:mining_fatigue", "minecraft:haste"),
            Map.entry("minecraft:poison", "minecraft:regeneration"),
            Map.entry("minecraft:slowness", "minecraft:speed"),
            Map.entry("minecraft:weakness", "minecraft:strength"),
            Map.entry("minecraft:wither", "minecraft:regeneration"),
            Map.entry("minecraft:instant_damage", "minecraft:instant_health"),
            Map.entry("minecraft:instant_health", "minecraft:instant_damage"),
            Map.entry("minecraft:luck", "minecraft:unluck"),
            Map.entry("minecraft:hero_of_the_village", "minecraft:bad_omen"),
            Map.entry("minecraft:night_vision", "minecraft:darkness"),
            Map.entry("minecraft:invisibility", "minecraft:glowing"),
            Map.entry("minecraft:saturation", "minecraft:hunger"),
            Map.entry("minecraft:slow_falling", "minecraft:levitation"),
            Map.entry("minecraft:haste", "minecraft:mining_fatigue"),
            Map.entry("minecraft:regeneration", "minecraft:poison"),
            Map.entry("minecraft:speed", "minecraft:slowness"),
            Map.entry("minecraft:strength", "minecraft:weakness"));

    public ChaosSkeleton(EntityType<? extends Skeleton> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    public int getColorNamed() {
        return hasCustomName() ? TintableEntity.getColorNamed(getName().getString(), tickCount, getId()) : -1;
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType spawnReason, @Nullable SpawnGroupData entityData) {
        Random r = new Random();
        CompoundTag effect = InfinityMod.provider.randomElement(r, ConfigType.EFFECTS);
        this.setEffect(effect);
        this.setDuration(r.nextInt(600));
        return super.finalizeSpawn(world, difficulty, spawnReason, entityData);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(effect, "luck");
        builder.define(duration, 200);
        builder.define(color, 0x00FF00);
    }
    @Override
    public boolean isSunBurnTick() {
        return false;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.is(Items.FERMENTED_SPIDER_EYE) && effect_lookup.containsKey(this.getEffect())) {
            if (!player.getAbilities().instabuild) {
                itemStack.shrink(1);
            }
            if (player.level().getRandom().nextFloat() < 0.5) {
                String i = effect_lookup.get(this.getEffect());
                MobEffect newEffect = BuiltInRegistries.MOB_EFFECT.get(Methods.id(i));
                if (newEffect != null) {
                    ChaosSkeleton newSkeleton;
                    if (!this.level().isClientSide() && (newSkeleton = ModEntities.CHAOS_SKELETON.get().create(this.level())) != null) {
                        ((ServerLevel) this.level()).sendParticles(ParticleTypes.HEART, this.getX(), this.getY(0.5), this.getZ(), 1, 0.0, 0.0, 0.0, 0.0);
                        this.discard();
                        ModEntities.copy(this, newSkeleton);
                        newSkeleton.setDuration(this.getDuration());
                        newSkeleton.setEffect(i, newEffect.getColor());
                        this.level().addFreshEntity(newSkeleton);
                        return InteractionResult.SUCCESS;
                    }
                }
            }
        }
        if (itemStack.is(Items.GLASS_BOTTLE)) {
            ItemStack itemStack2 = setPotion(Items.POTION.getDefaultInstance(), this.getColorForRender(), this.getEffect(), this.getDuration() * 20);
            ItemStack itemStack3 = ItemUtils.createFilledResult(itemStack, player, itemStack2, false);
            player.setItemInHand(hand, itemStack3);
            this.playSound(SoundEvents.BOTTLE_FILL, 1.0f, 1.0f);
            Skeleton newSkeleton;
            if (!this.level().isClientSide() && (newSkeleton = EntityType.SKELETON.create(this.level())) != null) {
                this.discard();
                ModEntities.copy(this, newSkeleton);
                this.level().addFreshEntity(newSkeleton);
                return InteractionResult.SUCCESS;
            }
        }
        return super.mobInteract(player, hand);
    }

    public void setEffect(CompoundTag eff) {
        setEffect(NbtUtils.getString(eff, "Name"), NbtUtils.getInt(eff, "Color"));
    }
    public void setEffect(String eff, int c) {
        if (eff.isBlank()) {
            CompoundTag newEffect = InfinityMod.provider.randomElement(random, ConfigType.EFFECTS);
            eff = NbtUtils.getString(newEffect, "Name");
            c = NbtUtils.getInt(newEffect, "Color");
        }
        this.entityData.set(effect, eff);
        this.entityData.set(color, c);
    }
    public String getEffect() {
        return this.entityData.get(effect);
    }
    @Override
    public int getColor() {
        return this.entityData.get(color);
    }

    public void setDuration(int i) {
        this.entityData.set(duration, i);
    }
    public int getDuration() {
        return this.entityData.get(duration);
    }
    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putString("effect", this.getEffect());
        nbt.putInt("duration", this.getDuration());
        nbt.putInt("color", this.getColor());
    }
    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.setEffect(NbtUtils.getString(nbt, "effect"), NbtUtils.getInt(nbt, "color"));
        this.setDuration(NbtUtils.getInt(nbt,"duration"));
    }

    public static ItemStack setPotion(ItemStack stack, int color, String effect, int duration) {
        CompoundTag potionEffect = new CompoundTag();
        potionEffect.putString("id", effect);
        potionEffect.putInt("duration", duration);
        List<MobEffectInstance> customEffects = new ArrayList<>();
        customEffects.add(MobEffectInstance.load(potionEffect));
        stack.set(DataComponents.POTION_CONTENTS, new PotionContents(Optional.empty(), Optional.of(color), customEffects));
        stack.set(DataComponents.ITEM_NAME, Component.translatable("potion.infinity.skeleton"));
        return stack;
    }

    @Override
    public ItemStack getProjectile(ItemStack stack) {
        return getProjectileType();
    }

    public ItemStack getProjectileType() {
        return setPotion(Items.TIPPED_ARROW.getDefaultInstance(), this.getColor(), this.getEffect(), this.getDuration());
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel world, DamageSource source, boolean causedByPlayer) {
        super.dropCustomDeathLoot(world, source, causedByPlayer);
        ItemStack weapon = source.getWeaponItem();
        Holder<Enchantment> looting = world.getServer().registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.LOOTING);
        int lootingLevel = weapon == null ? 0 : weapon.getEnchantments().getLevel(looting);
        int count = world.random.nextIntBetweenInclusive(0, 2 + lootingLevel);
        this.spawnAtLocation(getProjectileType().copyWithCount(count));
    }
}
