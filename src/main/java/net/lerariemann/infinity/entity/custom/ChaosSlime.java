package net.lerariemann.infinity.entity.custom;

import net.lerariemann.infinity.InfinityMod;
import net.lerariemann.infinity.util.InfinityMethods;
import net.lerariemann.infinity.util.VersionMethods;
import net.lerariemann.infinity.util.core.ConfigType;
import net.lerariemann.infinity.util.core.NbtUtils;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
//? if >1.21.4 {
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
//?}
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jetbrains.annotations.Nullable;

//? if neoforge {
import javax.annotation.ParametersAreNonnullByDefault;
//?}
import java.util.Optional;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Function;

@MethodsReturnNonnullByDefault
//? if neoforge {
@ParametersAreNonnullByDefault
//?}
public class ChaosSlime extends Slime implements TintableEntity {
    public static final EntityDataAccessor<BlockState> core = SynchedEntityData.defineId(ChaosSlime.class, EntityDataSerializers.BLOCK_STATE);
    public static final EntityDataAccessor<Integer> color = SynchedEntityData.defineId(ChaosSlime.class, EntityDataSerializers.INT);

    public ChaosSlime(EntityType<? extends ChaosSlime> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    public int getColorNamed() {
        return hasCustomName() ? TintableEntity.getColorNamed(getName().getString(), tickCount, getId()) : -1;
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
        builder.define(core, Blocks.STONE.defaultBlockState());
        builder.define(color, 0);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.2f);
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
        Random r = InfinityMod.random;
        this.entityData.set(core, VersionMethods.getFromRegistry(BuiltInRegistries.BLOCK, VersionMethods.id(InfinityMod.provider.randomName(r, ConfigType.ALL_BLOCKS))).defaultBlockState());
        this.entityData.set(color, r.nextInt(16777216));
        SpawnGroupData res = super.finalizeSpawn(world, difficulty, spawnReason, entityData
        //? if <1.21
                /*, tag*/
        );
        this.setSize(1 << r.nextInt(3), true);
        return res;
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader world) {
        return world.isUnobstructed(this);
    }

    public void setColor(int c) {
        this.entityData.set(color, c);
    }
    @Override
    public int getColor() {
        return this.entityData.get(color);
    }

    public void setCore(BlockState c) {
        this.entityData.set(core, c);
    }
    public BlockState getCore() {
        return this.entityData.get(core);
    }
    public BlockState getCoreForChild() {
        return Blocks.AIR.defaultBlockState();
    }

    @Override
    protected ParticleOptions getParticleType() {
        return new DustParticleOptions(
                //? if >1.21.2 {
                this.getColorForRender()
                //?} else {
                /*particleColorFromInt(this.getColorForRender())
                *///?}
                , 1.0f);
    }
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return this.getCore().getSoundType().getHitSound();
    }
    @Override
    protected SoundEvent getDeathSound() {
        return this.getCore().getSoundType().getBreakSound();
    }
    @Override
    protected SoundEvent getSquishSound() {
        return this.getCore().getSoundType().getStepSound();
    }
    @Override
    protected SoundEvent getJumpSound() {
        return this.getCore().getSoundType().getFallSound();
    }

    public
    //? if >1.21.4 {
    Optional<ResourceKey<LootTable>>
    //?} else if >1.21 {
    /*ResourceKey<LootTable>
    *///?} else {
    /*ResourceLocation
    *///?}
    getDefaultLootTable() {
        return this.getCore().getBlock().getLootTable();
    }

    //? if >1.21.4 {
    @Override
    protected void dropFromLootTable(ServerLevel level, DamageSource damageSource, boolean playerKill) {
        Optional<ResourceKey<LootTable>> optional = getDefaultLootTable();
        if (optional.isPresent()) {
            LootTable loottable = level.getServer().reloadableRegistries().getLootTable(optional.get());
            LootParams.Builder lootparams$builder = new LootParams.Builder(level)
                    .withParameter(LootContextParams.THIS_ENTITY, this)
                    .withParameter(LootContextParams.ORIGIN, this.position())
                    .withParameter(LootContextParams.DAMAGE_SOURCE, damageSource)
                    .withOptionalParameter(LootContextParams.ATTACKING_ENTITY, damageSource.getEntity())
                    .withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, damageSource.getDirectEntity());
            Player player = this.getLastHurtByPlayer();
            if (playerKill && player != null) {
                lootparams$builder = lootparams$builder.withParameter(LootContextParams.LAST_DAMAGE_PLAYER, player).withLuck(player.getLuck());
            }

            LootParams lootparams = lootparams$builder.create(LootContextParamSets.ENTITY);
            loottable.getRandomItems(lootparams, this.getLootTableSeed(), stack -> this.spawnAtLocation(level, stack));
        }
    }
    //?}

    @Override
    public void addAdditionalSaveData(
            //? if >1.21.2 {
            ValueOutput
            //?} else {
            /*CompoundTag
             *///?}
            nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putInt("color", getColor());
        nbt.putString("core", BuiltInRegistries.BLOCK.getKey(this.getCore().getBlock()).toString());
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
        this.setColor(NbtUtils.getInt(nbt, "color"));
        Block b = VersionMethods.getBlock(VersionMethods.id(NbtUtils.getString(nbt,"core")));
        this.setCore(b.defaultBlockState());
    }

    public static boolean canSpawn(EntityType<ChaosSlime> type, ServerLevelAccessor world,
                                   //? if >1.21.2 {
                                   net.minecraft.world.entity.EntitySpawnReason
                                   //?} else {
                                   /*net.minecraft.world.entity.MobSpawnType
                                    *///?}
                                   spawnReason, BlockPos pos, net.minecraft.util.RandomSource random) {
        if (world.getDifficulty() != Difficulty.PEACEFUL && InfinityMethods.chaosMobsEnabled()) {
            if (!(world instanceof WorldGenLevel)) {
                return false;
            }
            return Slime.checkMobSpawnRules(type, world, spawnReason, pos, random);
        }
        return false;
    }
}
