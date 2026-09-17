package net.codexarchonic.infinity.entity.custom;

import net.codexarchonic.infinity.iridescence.Iridescence;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.TimeUtil;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
//? if >1.21.4 {
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
//?}
import net.minecraft.world.phys.AABB;
import net.minecraft.world.scores.Team;
import org.jspecify.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

public abstract class AbstractChessFigure extends Monster implements NeutralMob {
    //~ if >26 'int'->'long'
    protected int angerTime;
    @Nullable
    //~ if >26 'UUID'->'EntityReference<LivingEntity>'
    protected UUID angryAt;

    protected AbstractChessFigure(EntityType<? extends AbstractChessFigure> entityType, Level world) {
        super(entityType, world);
    }

    public abstract boolean isBlackOrWhite();

    @Override
    protected void registerGoals() {
        initRegularGoals();
        initChessGoals();
        initAttackType();
    }
    protected  void initAttackType() {
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0, false));
    }
    protected void initChessGoals() {
        this.targetSelector.addGoal(1, new ChessRevengeGoal(this).setAlertOthers());
        this.targetSelector.addGoal(3, new ChaosCleanseGoal<>(this, ChaosSlime.class, true));
        this.targetSelector.addGoal(3, new ChaosCleanseGoal<>(this, ChaosSkeleton.class, true));
        this.targetSelector.addGoal(3, new ChessUniversalAngerGoal(this));
    }
    protected void initRegularGoals() {
        this.goalSelector.addGoal(0, new SwimWithVehicleGoal(this));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0f));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    //? if >26 {
    /*@Override
    public void setPersistentAngerEndTime(long angerTime) {
        this.angerTime = angerTime;
    }
    @Override
    public long getPersistentAngerEndTime() {
        return this.angerTime;
    }
    @Override
    public void setPersistentAngerTarget(@Nullable EntityReference<LivingEntity> angryAt) {
        this.angryAt = angryAt;
    }
    @Override
    public @Nullable EntityReference<LivingEntity> getPersistentAngerTarget() {
        return this.angryAt;
    }
    *///?} else {
    @Override
    public void setRemainingPersistentAngerTime(int angerTime) {
        this.angerTime = angerTime;
    }
    @Override
    public int getRemainingPersistentAngerTime() {
        return this.angerTime;
    }
    @Override
    public void setPersistentAngerTarget(@Nullable UUID angryAt) {
        this.angryAt = angryAt;
    }
    @Override
    @Nullable
    public UUID getPersistentAngerTarget() {
        return this.angryAt;
    }
    //?}
    @Override
    public void startPersistentAngerTimer() {
        //~ if >26 'setRemainingPersistentAngerTime'->'setPersistentAngerEndTime'
        this.setRemainingPersistentAngerTime(TimeUtil.rangeOfSeconds(20, 40).sample(this.random));
    }

    public boolean isInBattle() {
        return isInBattle("battle");
    }
    public boolean isInBattle(String battleName) {
        Team t = getTeam();
        return (t != null && t.getName().contains(battleName));
    }

    @Override
    public void addAdditionalSaveData(
            //~ if >1.21.2 'CompoundTag'->'ValueOutput'
            ValueOutput nbt) {
        super.addAdditionalSaveData(nbt);
        this.addPersistentAngerSaveData(nbt);
    }
    @Override
    public void readAdditionalSaveData(
            //~ if >1.21.2 'CompoundTag'->'ValueInput'
            ValueInput nbt) {
        super.readAdditionalSaveData(nbt);
        this.readPersistentAngerSaveData(this.level(), nbt);
    }

    @Override
    protected void customServerAiStep(
            //? if >1.21.4 {
            ServerLevel serverLevel
            //?}
    ) {
        //? if <1.21.4 {
        /*var serverLevel = (ServerLevel)this.level();
        *///?}
        this.updatePersistentAnger(serverLevel, false);
        super.customServerAiStep(
                //? if >1.21.4 {
                serverLevel
                //?}
        );
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader world) {
        if (!isBlackOrWhite()) return 0.0f;
        if (Iridescence.isIridescence(world, pos)) return -1.0F;
        return 0.0f;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return isBlackOrWhite() ? SoundEvents.PLAYER_BREATH : SoundEvents.AMETHYST_BLOCK_CHIME;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return isBlackOrWhite() ? SoundEvents.PLAYER_HURT : SoundEvents.AMETHYST_BLOCK_HIT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return isBlackOrWhite() ? SoundEvents.PLAYER_DEATH : SoundEvents.AMETHYST_BLOCK_BREAK;
    }

    public boolean shouldPursueRegularGoals() {
        return (!Iridescence.isUnderEffect(this));
    }
    public boolean shouldPursueChessGoals() {
        return shouldPursueRegularGoals() && isBlackOrWhite();
    }

    public static boolean isAngerCompatible(AbstractChessFigure fig1, AbstractChessFigure fig2) {
        if (fig1 instanceof ChaosPawn p1 && fig2 instanceof ChaosPawn p2) return p1.getCase() == p2.getCase();
        return fig1.isBlackOrWhite() ^ !fig2.isBlackOrWhite();
    }

    public static class SwimWithVehicleGoal extends Goal {
        private final Mob mob;

        public SwimWithVehicleGoal(Mob mob) {
            this.mob = mob;
            this.setFlags(EnumSet.of(Flag.JUMP));
            mob.getNavigation().setCanFloat(true);
        }

        @Override
        public void start() {
            if (mob.getControlledVehicle() instanceof Mob vehicle) {
                vehicle.getNavigation().setCanFloat(true);
            }
        }

        @Override
        public boolean canUse() {
            if (mob.getControlledVehicle() instanceof Mob vehicle) {
                return shouldMobSwim(vehicle);
            }
            return shouldMobSwim(mob);
        }

        public static boolean shouldMobSwim(Mob mob) {
            return mob.isInWater() && mob.getFluidHeight(FluidTags.WATER) > mob.getFluidJumpThreshold() || mob.isInLava();
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            if (mob.getRandom().nextFloat() < 0.8F) {
                if (mob.getControlledVehicle() instanceof Mob vehicle) {
                    vehicle.getJumpControl().jump();
                }
                mob.getJumpControl().jump();
            }
        }
    }

    public static class ChaosCleanseGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
        public ChaosCleanseGoal(Mob mob, Class<T> targetClass, boolean checkVisibility) {
            super(mob, targetClass, checkVisibility);
        }

        @Override
        public boolean canUse() {
            if (mob instanceof AbstractChessFigure e && !e.shouldPursueChessGoals()) return false;
            return super.canUse();
        }
    }

    public static class ChessUniversalAngerGoal extends Goal {
        private final AbstractChessFigure mob;
        private int lastAttackedTime;

        public ChessUniversalAngerGoal(AbstractChessFigure mob) {
            this.mob = mob;
        }

        @Override
        public boolean canUse() {
            //~ if >26 'getBoolean(GameRules.RULE_UNIVERSAL_ANGER)'->'get(GameRules.UNIVERSAL_ANGER)'
            return ((ServerLevel) this.mob.level()).getGameRules().getBoolean(GameRules.RULE_UNIVERSAL_ANGER) && this.canStartUniversalAnger();
        }

        private boolean canStartUniversalAnger() {
            return this.mob.getLastHurtByMob() != null
                    && this.mob.getLastHurtByMob().getType() == EntityType.PLAYER
                    && this.mob.getLastHurtByMobTimestamp() > this.lastAttackedTime
                    && this.mob.shouldPursueRegularGoals();
        }

        @Override
        public void start() {
            this.lastAttackedTime = this.mob.getLastHurtByMobTimestamp();
            this.mob.forgetCurrentTargetAndRefreshUniversalAnger();
            this.getOthersInRange().stream().filter(entity -> {
                if (entity == mob) return false;
                if (Iridescence.isUnderEffect(entity)) return false;
                if (entity instanceof AbstractChessFigure) {
                    return isAngerCompatible(mob, entity);
                }
                return true;
            }).map(entity -> (NeutralMob)entity).forEach(NeutralMob::forgetCurrentTargetAndRefreshUniversalAnger);
            super.start();
        }

        private List<? extends AbstractChessFigure> getOthersInRange() {
            double d = this.mob.getAttributeValue(Attributes.FOLLOW_RANGE);
            AABB box = AABB.unitCubeFromLowerCorner(this.mob.position()).inflate(d, 10.0, d);
            return this.mob.level().getEntitiesOfClass(this.mob.getClass(), box, EntitySelector.NO_SPECTATORS);
        }
    }

    public static class ChessRevengeGoal extends HurtByTargetGoal {
        public ChessRevengeGoal(PathfinderMob mob, Class<?>... noRevengeTypes) {
            super(mob, noRevengeTypes);
        }

        @Override
        protected void alertOthers() {
            if (mob instanceof AbstractChessFigure figure && figure.shouldPursueRegularGoals()) {
                double d = this.getFollowDistance();
                AABB box = AABB.unitCubeFromLowerCorner(figure.position()).inflate(d, 10.0, d);
                List<AbstractChessFigure> list = figure.level().getEntitiesOfClass(AbstractChessFigure.class, box, EntitySelector.NO_SPECTATORS);
                for (AbstractChessFigure pawn2 : list) {
                    if (figure != pawn2
                            && pawn2.getTarget() == null
                            && !pawn2.isAlliedTo(figure.getLastHurtByMob())
                            && !Iridescence.isUnderEffect(pawn2)
                            && isAngerCompatible(pawn2, figure))
                        this.alertOther(pawn2, figure.getLastHurtByMob());
                }
            }
        }
    }
}
