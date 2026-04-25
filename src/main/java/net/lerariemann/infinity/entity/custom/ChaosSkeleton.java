package net.lerariemann.infinity.entity.custom;

import net.lerariemann.infinity.InfinityMod;
import net.lerariemann.infinity.registry.core.ModEntities;
import net.lerariemann.infinity.util.VersionMethods;
import net.lerariemann.infinity.util.core.ConfigType;
import net.lerariemann.infinity.util.core.NbtUtils;
import net.lerariemann.infinity.util.platform.InfinityPlatform;
import net.minecraft.core.Holder;
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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
//? if >1.21 {
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.core.component.DataComponents;
//?} else {
/*import net.minecraft.world.item.alchemy.PotionUtils;
*///?}
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
//? if >1.21.4 {
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import static net.minecraft.world.entity.EntitySpawnReason.CONVERSION;
//?}
import org.jspecify.annotations.Nullable;

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
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty,
                                        //? if >1.21.2 {
                                        net.minecraft.world.entity.EntitySpawnReason
                                        //?} else {
                                        /*net.minecraft.world.entity.MobSpawnType
                                         *///?}
                                        spawnReason, @Nullable SpawnGroupData entityData
    //? if <1.21 {
    /*,CompoundTag tag
    *///?}
    ) {
        Random r = new Random();
        CompoundTag effect = InfinityMod.provider.randomElement(r, ConfigType.EFFECTS);
        this.setEffect(effect);
        this.setDuration(r.nextInt(600));
        return super.finalizeSpawn(world, difficulty, spawnReason, entityData
        //? if <1.21
        /*,tag*/
        );
    }

    @Override
    protected void defineSynchedData(
            //? if >1.21 {
            SynchedEntityData.Builder builder
             //?}
    ) {
        super.defineSynchedData(
                //? if >1.21 {
                builder
                 //?}
        );
        //? if <1.21 {
        /*var builder = this.entityData;
        *///?}
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
                MobEffect newEffect = VersionMethods.getFromRegistry(BuiltInRegistries.MOB_EFFECT, VersionMethods.id(i));
                if (newEffect != null) {
                    ChaosSkeleton newSkeleton;
                    if (!this.level().isClientSide() && (newSkeleton = ModEntities.CHAOS_SKELETON.get().create(this.level()
                            //? if >1.21.4 {
                            ,CONVERSION
                            //?}
                            )) != null) {
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
            if (!this.level().isClientSide() && (newSkeleton = EntityType.SKELETON.create(this.level()
                    //? if >1.21.4 {
                    , CONVERSION
                    //?}
            )) != null) {
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
    //? if >1.21.4 {

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putString("effect", this.getEffect());
        output.putInt("duration", this.getDuration());
        output.putInt("color", this.getColor());
    }

    //?} else {
    /*@Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putString("effect", this.getEffect());
        nbt.putInt("duration", this.getDuration());
        nbt.putInt("color", this.getColor());
    }
    *///?}

    @Override
    public void readAdditionalSaveData(
            //? if >1.21.2 {
            ValueInput
            //?} else {
            /*CompoundTag
            *///?}
            nbt) {
        super.readAdditionalSaveData(nbt);
        this.setEffect(NbtUtils.getString(nbt, "effect"), NbtUtils.getInt(nbt, "color"));
        this.setDuration(NbtUtils.getInt(nbt,"duration"));
    }

    public static ItemStack setPotion(ItemStack stack, int color, String effect, int duration) {
        List<MobEffectInstance> customEffects = new ArrayList<>();
        //? if >1.21.4 {
        customEffects.add(new MobEffectInstance(VersionMethods.getFromId(BuiltInRegistries.MOB_EFFECT, VersionMethods.id(effect)), duration));
        //?} else {
        /*CompoundTag potionEffect = new CompoundTag();
        potionEffect.putString("id", effect);
        potionEffect.putInt("duration", duration);
        customEffects.add(MobEffectInstance.load(potionEffect));
        *///?}
        //? if >1.21.4 {
        stack.set(DataComponents.POTION_CONTENTS, new PotionContents(Optional.empty(), Optional.of(color), customEffects, Optional.empty()));
        stack.set(DataComponents.ITEM_NAME, Component.translatable("potion.infinity.skeleton"));
        //?} else if >1.21 {
        /*stack.set(DataComponents.POTION_CONTENTS, new PotionContents(Optional.empty(), Optional.of(color), customEffects));
        stack.set(DataComponents.ITEM_NAME, Component.translatable("potion.infinity.skeleton"));
        *///?} else {
        /*PotionUtils.setCustomEffects(stack, customEffects);
        stack.setHoverName(Component.translatable("potion.infinity.skeleton"));
        *///?}
        return stack;
    }

    @Override
    public ItemStack getProjectile(ItemStack stack) {
        return getProjectileType();
    }

    public ItemStack getProjectileType() {
        return setPotion(Items.TIPPED_ARROW.getDefaultInstance(), this.getColor(), this.getEffect(), this.getDuration());
    }

    //? if >1.21 {
    @Override
    protected void dropCustomDeathLoot(ServerLevel world, DamageSource source, boolean causedByPlayer) {
        super.dropCustomDeathLoot(world, source, causedByPlayer);
        ItemStack weapon = source.getWeaponItem();
        Holder<Enchantment> looting = VersionMethods.getRegistry(world.getServer().registryAccess(), Registries.ENCHANTMENT).
                //? if >1.21.4 {
                getOrThrow
                //?} else {
                /*getHolderOrThrow
                *///?}
                (Enchantments.LOOTING);
        int lootingLevel = weapon == null ? 0 : InfinityPlatform.INSTANCE.getEnchantments(weapon, world.getServer()).getLevel(looting);
        dropEquipment(lootingLevel, world);
    }
    //?} else {
    /*@Override
    protected void dropCustomDeathLoot(DamageSource source, int lootingLevel, boolean allowDrops) {
        super.dropCustomDeathLoot(source, lootingLevel, allowDrops);
        dropEquipment(lootingLevel, (ServerLevel) this.level());
    }
    *///?}

    private void dropEquipment(int lootingLevel, ServerLevel world) {
        int count = world.random.nextIntBetweenInclusive(0, 2 + lootingLevel);
        this.spawnAtLocation(
                //? if >1.21.4
                world,
                getProjectileType().copyWithCount(count));
    }
}
