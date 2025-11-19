package net.lerariemann.infinity.entity.custom;

import net.lerariemann.infinity.block.custom.AntBlock;
import net.lerariemann.infinity.util.VersionMethods;
import net.lerariemann.infinity.util.core.NbtUtils;
import net.lerariemann.infinity.util.var.BishopBattle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
//? if >1.21.4 {
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
//?}
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class AntEntity extends AbstractChessFigure {
    @Nullable
    protected BlockPos lastChangedPos;
    private Direction direction;
    private boolean dropsLoot;

    public AntEntity(EntityType<? extends AbstractChessFigure> entityType, Level world) {
        super(entityType, world);
        direction = Direction.EAST;
        dropsLoot = true;
    }

    @Override
    public boolean isBlackOrWhite() {
        return true;
    }
    @Override
    public boolean shouldDropLoot() {
        return dropsLoot;
    }
    public void addToBattle(BishopBattle battle) {
        battle.addEntity(this);
        dropsLoot = false;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.SILVERFISH_HURT;
    }
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SILVERFISH_DEATH;
    }

    @Override
    protected void registerGoals() {
        targetSelector.addGoal(2, new AntBattleGoal<>(this, Player.class, true));
        goalSelector.addGoal(3, new AntBlockRecolorGoal(this));
        super.registerGoals();
    }
    @Override
    protected void initRegularGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(5, new WanderConditionalGoal(this, 1.0));
        goalSelector.addGoal(6, new LookAtEntityConditionalGoal(this, Player.class, 6.0F));
        goalSelector.addGoal(7, new LookAroundConditionalGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.1f)
                .add(Attributes.MAX_HEALTH, 6);
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
        this.dropsLoot = NbtUtils.getBoolean(nbt, "dropsLoot", true);
        this.direction = switch(NbtUtils.getString(nbt, "direction")) {
            case "N" -> Direction.NORTH;
            case "W" -> Direction.WEST;
            case "S" -> Direction.SOUTH;
            default -> Direction.EAST;
        };
        if (nbt.contains("last_changed_pos")) {
            //? if >1.21.2 {
            this.lastChangedPos = nbt.read("last_changed_pos", BlockPos.CODEC).orElse(lastChangedPos);
            //?} else {
            /*CompoundTag pos = NbtUtils.getCompound(nbt, "last_changed_pos");
            this.lastChangedPos = new BlockPos(NbtUtils.getInt(pos, "x"), NbtUtils.getInt(nbt,"y"), NbtUtils.getInt(nbt, "z"));
            *///?}
        }
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
        nbt.putBoolean("dropsLoot", dropsLoot);
        nbt.putString("direction", switch(this.direction) {
                    case NORTH -> "N";
                    case WEST -> "W";
                    case SOUTH -> "S";
                    default -> "E";
                });
        if (lastChangedPos != null) {
            //? if >1.21.2 {
            nbt.store("last_changed_pos", BlockPos.CODEC, lastChangedPos);
            //?} else {
            /*CompoundTag pos = new CompoundTag();
            pos.putInt("x", lastChangedPos.getX());
            pos.putInt("y", lastChangedPos.getY());
            pos.putInt("z", lastChangedPos.getZ());
            nbt.put("last_changed_pos", pos);
            *///?}
        }
    }

    @Override
    public boolean shouldPursueRegularGoals() {
        Level w = level();
        Optional<BlockPos> bp = mainSupportingBlockPos;
        return (bp.isEmpty()
                || isInBattle()
                || !AntBlock.isSafeToRecolor(w, bp.get()))
                && super.shouldPursueRegularGoals();
    }

    //? if >1.21 {
    @Override
    public boolean canBeLeashed() {
        return !isInBattle();
    }
    //?}
    @Override
    public boolean canStandOnFluid(FluidState state) {
        return state.is(FluidTags.WATER);
    }

    public static VoxelShape getWaterCollisionShape(int level) {
        return Block.box(0.0, 0.0, 0.0, 16.0, level, 16.0);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.firstTick && this.fluidHeight.getDouble(FluidTags.WATER) > 0.0) {
            CollisionContext shapeContext = CollisionContext.of(this);
            if (shapeContext.isAbove(getWaterCollisionShape(15),
                    this.blockPosition(), true)
                    && !this.level().getFluidState(this.blockPosition().above()).is(FluidTags.WATER)) {
                this.setOnGround(true);
            } else {
                this.setDeltaMovement(this.getDeltaMovement().scale(0.5).add(0.0, 0.05, 0.0));
            }
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (!this.isVehicle()
                //? if >1.21
                && this.getAttributeValue(Attributes.SCALE) > 2
                && player.getItemInHand(hand).isEmpty()) {
            this.putPlayerOnBack(player);
            return VersionMethods.sidedSuccess(this.level().isClientSide());
        }
        return super.mobInteract(player, hand);
    }
    protected void putPlayerOnBack(Player player) {
        if (!this.level().isClientSide()) {
            player.setYRot(this.getYRot());
            player.setXRot(this.getXRot());
            player.startRiding(this);
        }
    }
    @Override
    protected Vec3 getRiddenInput(Player controllingPlayer, Vec3 movementInput) {
        float f = controllingPlayer.zza;
        return new Vec3(controllingPlayer.xxa * 0.5f, 0, f < 0 ? f*0.25f : f);
    }
    @Override
    protected float getRiddenSpeed(Player controllingPlayer) {
        return (float)this.getAttributeValue(Attributes.MOVEMENT_SPEED) * 0.8f;
    }
    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        if (this.getFirstPassenger() instanceof Player player) {
            return player;
        }
        return super.getControllingPassenger();
    }
    @Override
    protected void positionRider(Entity passenger, MoveFunction positionUpdater) {
        super.positionRider(passenger, positionUpdater);
        if (passenger instanceof LivingEntity) {
            ((LivingEntity)passenger).yBodyRot = this.yBodyRot;
        }
    }
    @Override
    protected void tickRidden(Player controllingPlayer, Vec3 movementInput) {
        super.tickRidden(controllingPlayer, movementInput);
        this.setRot(controllingPlayer.getYRot(),controllingPlayer.getXRot() * 0.5F);
        this.yRotO = this.yBodyRot = this.yHeadRot = this.getYRot();
    }

    public static class AntBattleGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
        public AntBattleGoal(Mob mob, Class<T> targetClass, boolean checkVisibility) {
            super(mob, targetClass, checkVisibility);
        }

        @Override
        public boolean canUse() {
            if (mob instanceof AbstractChessFigure e && !e.isInBattle()) return false;
            return super.canUse();
        }
    }

    public static class AntBlockRecolorGoal extends Goal {
        private final AntEntity mob;
        @Nullable
        private BlockPos targetPos;

        public AntBlockRecolorGoal(AntEntity mob) {
            this.mob = mob;
        }

        @Override
        public boolean canUse() {
            if (mob.shouldPursueRegularGoals()) return false;
            Optional<BlockPos> bp = mob.mainSupportingBlockPos;
            if (bp.isEmpty() || bp.get().equals(mob.lastChangedPos)) return false;
            mob.lastChangedPos = bp.get();

            BlockState down = mob.level().getBlockState(bp.get());
            AntBlock.Clockwiseness cw = AntBlock.getCW(down.getBlock());
            if (cw == null) return false;

            Direction direction = mob.direction;
            Direction direction2 = cw.equals(AntBlock.Clockwiseness.CW)
                    ? direction.getClockWise()
                    : direction.getCounterClockWise();

            Block newBlock = AntBlock.recolor(down.getBlock(), cw.equals(AntBlock.Clockwiseness.CCW));
            if (newBlock == null) return false;

            targetPos = mob.blockPosition().relative(direction2);
            mob.level().setBlock(bp.get(), newBlock.withPropertiesOf(down), 19);
            mob.direction = direction2;
            return true;
        }

        @Override
        public boolean canContinueToUse() {
            return false;
        }

        @Override
        public void start() {
            if (targetPos == null) return;
            //? if >1.21 {
            Vec3 v = targetPos.getBottomCenter();
            //?} else {
            /*Vec3 v = targetPos.getCenter();
            *///?}
            mob.randomTeleport(v.x, v.y, v.z, false);
            mob.setYRot(mob.direction.toYRot());
        }
    }

    public static class WanderConditionalGoal extends WaterAvoidingRandomStrollGoal {
        private final AntEntity mob1;
        public WanderConditionalGoal(AntEntity pathAwareEntity, double d) {
            super(pathAwareEntity, d);
            mob1 = pathAwareEntity;
        }

        @Override
        public boolean canUse() {
            return super.canUse() && mob1.shouldPursueRegularGoals();
        }
    }

    public static class LookAroundConditionalGoal extends RandomLookAroundGoal {
        private final AntEntity mob1;
        public LookAroundConditionalGoal(AntEntity mob) {
            super(mob);
            mob1 = mob;
        }

        @Override
        public boolean canUse() {
            return super.canUse() && mob1.shouldPursueRegularGoals();
        }
    }

    public static class LookAtEntityConditionalGoal extends LookAtPlayerGoal {
        private final AntEntity mob1;

        public LookAtEntityConditionalGoal(AntEntity mob, Class<? extends LivingEntity> targetType, float range) {
            super(mob, targetType, range);
            mob1 = mob;
        }

        @Override
        public boolean canUse() {
            return super.canUse() && mob1.shouldPursueRegularGoals();
        }
    }
}
