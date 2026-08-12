package net.codexarchonic.infinity.entity.custom;

import net.codexarchonic.infinity.InfinityMod;
import net.codexarchonic.infinity.util.core.ConfigType;
import net.codexarchonic.infinity.util.core.NbtUtils;
import net.codexarchonic.infinity.util.var.BishopBattle;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
//? if >1.21.2 {
import net.minecraft.world.entity.EntitySpawnReason;
//?} else {
/*import net.minecraft.world.entity.MobSpawnType;
 *///?}
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.PathFinder;
//? if >1.21.4 {
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
//?}
import org.jspecify.annotations.Nullable;

@MethodsReturnNonnullByDefault
public class BishopEntity extends AbstractChessFigure implements RangedAttackMob {
    @Nullable
    public BishopBattle battle;

    private RangedBowAttackGoal<BishopEntity> bowAttackGoal;
    private MeleeAttackGoal meleeAttackGoal;

    public BishopEntity(EntityType<? extends BishopEntity> entityType, Level world) {
        super(entityType, world);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.35)
                .add(Attributes.MAX_HEALTH, 150)
                .add(Attributes.ATTACK_DAMAGE, 5.0);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty,
                                        //? if >1.21.2 {
                                        EntitySpawnReason
                                        //?} else {
                                        /*MobSpawnType
                                         *///?}
                                        spawnReason, @Nullable SpawnGroupData entityData
        //? if <1.21 {
        /*,CompoundTag tag
        *///?}
    ) {
        entityData = super.finalizeSpawn(world, difficulty, spawnReason, entityData
            //? if <1.21
            //,tag
        );
        updateWeapon(random.nextBoolean(), true);
        updateAttackType();
        return entityData;
    }

    @Override
    protected void registerGoals() {
        targetSelector.addGoal(2, new AntEntity.AntBattleGoal<>(this, Player.class, true));
        super.registerGoals();
    }
    @Override
    protected void initAttackType() {
        bowAttackGoal = new RangedBowAttackGoal<>(this, 1.0, 20, 15.0F);
        meleeAttackGoal = new MeleeAttackGoal(this, 1.5, false) {
            @Override
            public void stop() {
                super.stop();
                mob.setAggressive(false);
            }

            @Override
            public void start() {
                super.start();
                mob.setAggressive(true);
            }
        };
    }

    @Override
    public void aiStep() {
        if (tickCount % 200 == 0 && getTarget() != null)
            updateWeapon(random.nextBoolean(), false);
        super.aiStep();
    }
    public void updateWeapon(boolean newAttackBow, boolean initial) {
        if (this.level() != null && !this.level().isClientSide()) {
            boolean isHoldingBow = isHolding(Items.BOW);
            if (!newAttackBow && (isHoldingBow || initial)) {
                setItemSlot(EquipmentSlot.MAINHAND, (random.nextBoolean() ? Items.IRON_SWORD : Items.IRON_AXE).getDefaultInstance());
            }
            if (newAttackBow && !isHoldingBow) {
                setItemSlot(EquipmentSlot.MAINHAND, Items.BOW.getDefaultInstance());
            }
        }
    }
    @Override
    public void setItemSlot(EquipmentSlot slot, ItemStack stack) {
        super.setItemSlot(slot, stack);
        if (!this.level().isClientSide()) {
            this.updateAttackType();
        }
    }
    public void updateAttackType() {
        boolean bow = isHolding(Items.BOW);
        goalSelector.removeGoal(bow ? meleeAttackGoal : bowAttackGoal);
        goalSelector.addGoal(2, bow ? bowAttackGoal : meleeAttackGoal);
    }

    @Override
    public void rideTick() {
        super.rideTick();
        if (this.getControlledVehicle() instanceof PathfinderMob pathAwareEntity) {
            this.yBodyRot = pathAwareEntity.yBodyRot;
        }
    }

    @Override
    protected PathNavigation createNavigation(Level world) {
        return new GroundPathNavigation(this, world) {
            @Override
            protected PathFinder createPathFinder(int range) {
                this.nodeEvaluator = new BishopNodeMaker();
                this.nodeEvaluator.setCanPassDoors(true);
                return new PathFinder(this.nodeEvaluator, range);
            }
        };
    }

    //~ if >1.21.2 'CompoundTag'->'ValueOutput' {
    @Override
    public void addAdditionalSaveData(ValueOutput nbt) {
        super.addAdditionalSaveData(nbt);
        if (battle != null) nbt.putString("battle", battle.teamName);
    }
    //~}
    //~ if >1.21.2 'CompoundTag'->'ValueInput' {
    @Override
    public void readAdditionalSaveData(ValueInput nbt) {
        super.readAdditionalSaveData(nbt);
        if (NbtUtils.compoundHasString(nbt, "battle") && level() instanceof ServerLevel w)
            battle = new BishopBattle(w, NbtUtils.getString(nbt, "battle"));
        updateAttackType();
    }
    //~}
    public void addToBattle(BishopBattle battle) {
        this.battle = battle;
        battle.addEntity(this);
    }
    @Override
    //? if >1.21.4 {
    protected void triggerOnDeathMobEffects(ServerLevel level, RemovalReason reason) {
        super.triggerOnDeathMobEffects(level, reason);
    //?} else if >1.21 {
    /*protected void triggerOnDeathMobEffects(RemovalReason reason) {
        super.triggerOnDeathMobEffects(reason);
     *///?} else {
    
    /*public void onClientRemoval() {
        super.onClientRemoval();
    *///?}
        if (battle != null) battle.stop();
    }

    @Override
    public boolean isInBattle(String battleName) {
        return battle != null && battle.teamName.contains(battleName);
    }
    @Override
    public boolean isInBattle() {
        return battle != null;
    }
    @Override
    public boolean isBlackOrWhite() {
        return true;
    }

    @Override
    public void performRangedAttack(LivingEntity target, float pullProgress) {
        AbstractArrow persistentProjectileEntity =
                ProjectileUtil.getMobArrow(this, getProjectileType(), pullProgress
                        //? if >1.21 {
                        , Items.BOW.getDefaultInstance()
                        //?}
                        );
        double d = target.getX() - this.getX();
        double e = target.getY(0.333) - persistentProjectileEntity.getY();
        double f = target.getZ() - this.getZ();
        double g = Math.sqrt(d * d + f * f);
        persistentProjectileEntity.shoot(d, e + g * 0.2F, f, 1.6F, (float)(14 - this.level().getDifficulty().getId() * 4));
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level().addFreshEntity(persistentProjectileEntity);
    }

    @Override
    public ItemStack getProjectile(ItemStack stack) {
        return getProjectileType();
    }

    public ItemStack getProjectileType() {
        CompoundTag effect = InfinityMod.provider.randomElement(random, ConfigType.EFFECTS);
        if (!NbtUtils.getString(effect, "Category").equals("harmful")
                || NbtUtils.getBoolean(effect, "Instant")) return Items.ARROW.getDefaultInstance();
        return ChaosSkeleton.setPotion(Items.TIPPED_ARROW.getDefaultInstance(),
                NbtUtils.getInt(effect, "Color"),
                NbtUtils.getString(effect, "Name"),
                200);
    }
}
