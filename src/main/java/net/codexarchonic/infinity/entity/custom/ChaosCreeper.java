package net.codexarchonic.infinity.entity.custom;

import net.codexarchonic.infinity.InfinityMod;
import net.codexarchonic.infinity.block.custom.BiomeBottleBlock;
import net.codexarchonic.infinity.item.DiscItem;
import net.codexarchonic.infinity.registry.core.ModEntities;
import net.codexarchonic.infinity.registry.core.ModItems;
import net.codexarchonic.infinity.util.VersionMethods;
import net.codexarchonic.infinity.util.core.ConfigType;
import net.codexarchonic.infinity.util.core.NbtUtils;
//? if >1.21 {
import net.minecraft.core.component.DataComponentMap;
//?} else {
/*import net.minecraft.world.entity.monster.AbstractSkeleton;
*///?}
//? if >1.21.2 {
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import static net.minecraft.world.entity.EntitySpawnReason.CONVERSION;
 //?} else {
/*import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
*///?}
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jspecify.annotations.Nullable;

public class ChaosCreeper extends Creeper implements TintableEntity {
    public static EntityDataAccessor<Integer> color = SynchedEntityData.defineId(ChaosCreeper.class, EntityDataSerializers.INT);
    public static EntityDataAccessor<Float> range = SynchedEntityData.defineId(ChaosCreeper.class, EntityDataSerializers.FLOAT);
    public static EntityDataAccessor<String> biome = SynchedEntityData.defineId(ChaosCreeper.class, EntityDataSerializers.STRING);

    public ChaosCreeper(EntityType<? extends Creeper> entityType, Level world) {
        super(entityType, world);
    }

    public void setRandomCharge() {
        setRange((float)(10*(1 + random.nextFloat()*(Math.sqrt(10) - 1))));
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty,
                                        //? if >1.21.2 {
                                        EntitySpawnReason
                                        //?} else {
                                        /*MobSpawnType
                                        *///?}
                                                spawnReason, @Nullable SpawnGroupData entityData
                                        //? if <1.21 {
                                         /*, @Nullable CompoundTag entityNbt
                                        *///?}
    ) {
        CompoundTag biome = InfinityMod.provider.randomElement(world.getRandom(), ConfigType.BIOMES);
        this.setColor(NbtUtils.getInt(biome, "Color", 7842607));
        this.setRandomCharge();
        this.setBiome(NbtUtils.getString(biome, "Name"));
        return super.finalizeSpawn(world, difficulty, spawnReason, entityData
                //? if <1.21 {
                /*, entityNbt
                *///?}
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
        /*SynchedEntityData builder = this.entityData;
        *///?}
        builder.define(color, 7842607);
        builder.define(range, 16.0f);
        builder.define(biome, "minecraft:plains");
    }

    public void setBiome(String s) {
        this.entityData.set(biome, s);
    }
    public String getBiome() {
        return this.entityData.get(biome);
    }
    public String getBiomeId() {
        return this.entityData.get(biome);
    }

    public void setColor(int c) {
        this.entityData.set(color, c);
    }

    @Override
    public int getColorNamed() {
        return hasCustomName() ? TintableEntity.getColorNamed(getName().getString(), tickCount, getId()) : -1;
    }
    @Override
    public int getColor() {
        return this.entityData.get(color);
    }

    public void setRange(float s) {
        this.entityData.set(range, s);
    }

    public float getRange() {
        return this.entityData.get(range);
    }

    @Override
    public void addAdditionalSaveData(
            //? if >1.21.2 {
            ValueOutput
            //?} else {
            /*CompoundTag
             *///?}
            nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putFloat("range", this.getRange());
        nbt.putInt("color", this.getColor());
        nbt.putString("biome", this.getBiome());
    }

    @Override
    public void readAdditionalSaveData(
            //? if >1.21.2 {
            ValueInput
            //?} else {
            /*CompoundTag
            *///?}
                    nbt) {
        super.readAdditionalSaveData(nbt);
        this.setRange(NbtUtils.getFloat(nbt, "range"));
        this.setColor(NbtUtils.getInt(nbt, "color"));
        this.setBiome(NbtUtils.getString(nbt, "biome"));
    }

    public void blow_up() {
        float f = 3 * this.getRange() / 16;
        this.dead = true;
        MinecraftServer s = this.level().getServer();
        try (Level level = this.level()) {
            if (s != null) {
                ServerLevel serverWorld = s.getLevel(level.dimension());
                if (serverWorld != null) {
                    BiomeBottleBlock.spreadCircle(serverWorld, blockPosition(), VersionMethods.id(getBiome()), getCharge());
                }
            }
            level.explode(this, this.getX(), this.getY(), this.getZ(), f, Level.ExplosionInteraction.NONE);
        }
        catch (Exception ignored) {
        }
        this.discard();
    }

    public int getCharge() {
        return (int)(this.getRange()*this.getRange());
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.is(ModItems.BIOME_BOTTLE_ITEM.get()) && BiomeBottleBlock.isEmpty(itemStack)) {
            this.playSound(SoundEvents.BOTTLE_FILL, 1.0f, 1.0f);
            Level level = this.level();
            if (level instanceof ServerLevel) {
                Creeper newCreeper = EntityType.CREEPER.create(level
                        //? if >1.21.2
                        , CONVERSION
                );
                if (newCreeper != null) {
                    ItemStack itemStack2 = new ItemStack(itemStack.getItem());
                    //? if >1.21 {
                    itemStack2.applyComponents(BiomeBottleBlock.addComponents(DataComponentMap.builder(),
                            VersionMethods.id(getBiome()), getColor(), getCharge()).build());
                    //?} else {
                    /*CompoundTag compound = new CompoundTag();
                    CompoundTag blockEntityTag = new CompoundTag();
                    blockEntityTag.putString("Biome", getBiomeId());
                    blockEntityTag.putInt("Color", getColor());
                    blockEntityTag.putInt("Charge", getCharge());
                    compound.put("BlockEntityTag", blockEntityTag);
                    itemStack2.setTag(compound);
                    *///?}
                    ItemStack itemStack3 = ItemUtils.createFilledResult(itemStack, player, itemStack2, false);
                    player.setItemInHand(hand, itemStack3);
                    this.discard();
                    ModEntities.copy(this, newCreeper);
                    level.addFreshEntity(newCreeper);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return super.mobInteract(player, hand);
    }

    @Override
    //? if >1.21 {
    protected void dropCustomDeathLoot(ServerLevel world, DamageSource source, boolean causedByPlayer) {
        super.dropCustomDeathLoot(world, source, causedByPlayer);
        //~ if >26 'getType().is'->'is'
        if (source.getEntity() != null && source.getEntity().getType().is(EntityTypeTags.SKELETONS)) {
    //?} else {
    /*protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHit) {
        super.dropCustomDeathLoot(source, looting, recentlyHit);
        if (source.getEntity() instanceof AbstractSkeleton && getServer() != null) {
            var world = this.level();
    *///?}
            ItemStack stack = DiscItem.getRandom(world.getServer());
            this.spawnAtLocation(
                    //? if >1.21.2
                    (ServerLevel) level(),
                    stack);
        }
    }
}