package net.lerariemann.infinity.entity.custom;

import net.lerariemann.infinity.InfinityMod;
import net.lerariemann.infinity.block.custom.BiomeBottleBlock;
import net.lerariemann.infinity.registry.core.ModComponentTypes;
import net.lerariemann.infinity.registry.core.ModEntities;
import net.lerariemann.infinity.registry.core.ModItems;
import net.lerariemann.infinity.util.InfinityMethods;
import net.lerariemann.infinity.util.VersionMethods;
import net.lerariemann.infinity.util.core.ConfigType;
import net.lerariemann.infinity.util.core.NbtUtils;
import net.minecraft.MethodsReturnNonnullByDefault;
//? if >1.21 {
/*import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.EitherHolder;
import net.minecraft.world.item.JukeboxPlayable;
*///?}
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

//? if neoforge {
/*import javax.annotation.ParametersAreNonnullByDefault;
*///?}

@MethodsReturnNonnullByDefault
//? if neoforge {
/*@ParametersAreNonnullByDefault
 *///?}
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
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType spawnReason, @Nullable SpawnGroupData entityData
                                        //? if <1.21 {
                                         , @Nullable CompoundTag entityNbt
                                        //}
    ) {
        CompoundTag biome = InfinityMod.provider.randomElement(world.getRandom(), ConfigType.BIOMES);
        this.setColor(NbtUtils.getInt(biome, "Color", 7842607));
        this.setRandomCharge();
        this.setBiome(NbtUtils.getString(biome, "Name"));
        return super.finalizeSpawn(world, difficulty, spawnReason, entityData
                //? if <1.21 {
                , entityNbt
                //}
                );
    }

    @Override
    protected void defineSynchedData(
            //? if >1.21 {
            /*SynchedEntityData.Builder builder
            *///?}
    ) {
        super.defineSynchedData(
                //? if >1.21 {
                /*builder
                *///?}
                );
        //? if <1.21 {
        SynchedEntityData builder = this.entityData;
        //?}
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
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putFloat("range", this.getRange());
        nbt.putInt("color", this.getColor());
        nbt.putString("biome", this.getBiome());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.setRange(NbtUtils.getFloat(nbt, "range"));
        this.setColor(NbtUtils.getInt(nbt, "color"));
        this.setBiome(NbtUtils.getString(nbt, "biome"));
    }

    public void blow_up() {
        float f = 3 * this.getRange() / 16;
        this.dead = true;
        MinecraftServer s = this.getServer();
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
            ItemStack itemStack2 = new ItemStack(itemStack.getItem());
            this.playSound(SoundEvents.BOTTLE_FILL, 1.0f, 1.0f);
            Creeper newCreeper;
            try (Level level = this.level()) {
                if (!level.isClientSide() && (newCreeper = EntityType.CREEPER.create(this.level())) != null) {
                    //? if >1.21 {
                    
                    /*itemStack2.applyComponents(BiomeBottleBlock.addComponents(DataComponentMap.builder(),
                            VersionMethods.id(getBiome()), getColor(), getCharge()).build());
                    *///?} else {
                    CompoundTag compound = new CompoundTag();
                    CompoundTag blockEntityTag = new CompoundTag();
                    blockEntityTag.putString("Biome", getBiomeId());
                    blockEntityTag.putInt("Color", getColor());
                    blockEntityTag.putInt("Charge", getCharge());
                    compound.put("BlockEntityTag", blockEntityTag);
                    itemStack2.setTag(compound);
                    //?}
                    ItemStack itemStack3 = ItemUtils.createFilledResult(itemStack, player, itemStack2, false);
                    player.setItemInHand(hand, itemStack3);
                    this.discard();
                    ModEntities.copy(this, newCreeper);
                    level.addFreshEntity(newCreeper);
                    return InteractionResult.SUCCESS;
                }
            }
            catch (Exception ignored) {
            }
        }
        return super.mobInteract(player, hand);
    }

    //? if >1.21 {
    /*@Override
    protected void dropCustomDeathLoot(ServerLevel world, DamageSource source, boolean causedByPlayer) {
        if (source.getEntity() != null && source.getEntity().getType().is(EntityTypeTags.SKELETONS)) {
            ItemStack stack = ModItems.DISC.get().getDefaultInstance();
            ResourceLocation song = VersionMethods.id(InfinityMod.provider.randomName(world.random, ConfigType.JUKEBOXES));
            stack.applyComponents(DataComponentMap.builder()
                    .set(DataComponents.JUKEBOX_PLAYABLE, new JukeboxPlayable(new EitherHolder<>(
                            ResourceKey.create(Registries.JUKEBOX_SONG, song)), true))
                    .set(ModComponentTypes.COLOR.get(), (int)InfinityMethods.getNumericFromId(song)).build());
            this.spawnAtLocation(stack);
        }
    }
    *///?}
}