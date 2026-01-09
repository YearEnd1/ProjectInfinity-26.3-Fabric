package net.lerariemann.infinity.entity.custom;

import net.lerariemann.infinity.InfinityMod;
import net.lerariemann.infinity.iridescence.Iridescence;
import net.lerariemann.infinity.util.InfinityMethods;
import net.lerariemann.infinity.util.VersionMethods;
import net.lerariemann.infinity.util.core.ConfigType;
import net.lerariemann.infinity.util.core.NbtUtils;
import net.lerariemann.infinity.util.core.RandomProvider;
//? if >1.21 {
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.codec.ByteBufCodecs;
//?}
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.EatBlockGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
//? if >1.21.4 {
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
//?}
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.Nullable;

//? if neoforge {
import javax.annotation.ParametersAreNonnullByDefault;
//?}
import java.util.*;
import java.util.function.Supplier;

//? if neoforge {
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
//?}
public class ChaosPawn extends AbstractChessFigure {

    public static final EntityDataAccessor<CompoundTag> colors = SynchedEntityData.defineId(ChaosPawn.class, EntityDataSerializers.COMPOUND_TAG);
    public static final EntityDataAccessor<Integer> special_case = SynchedEntityData.defineId(ChaosPawn.class, EntityDataSerializers.INT);

    public ChaosPawn(EntityType<? extends ChaosPawn> entityType, Level world) {
        super(entityType, world);
    }
    public void setColors(CompoundTag i) {
        this.entityData.set(colors, i);
    }
    public CompoundTag getColors() {
        return this.entityData.get(colors);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.ATTACK_DAMAGE, 5.0)
                .add(Attributes.FOLLOW_RANGE, 35.0)
                //? if >1.21
                .add(Attributes.SCALE, 0.9)
                .add(Attributes.MOVEMENT_SPEED, 0.25F);
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
        builder.define(colors, new CompoundTag());
        builder.define(special_case, -1);
    }
    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(5, new EatBlockGoal(this));
    }

    @Override
    public void ate() {
        super.ate();
        this.setAllColors(this.level().getBiome(this.blockPosition()).value().getGrassColor(this.getX(), this.getZ()));
    }

    @Override
    public Component getTypeName() {
        int i = getCase();
        return switch (i) {
            case 0 -> Component.translatable("entity.infinity.pawn_black");
            case 1 -> Component.translatable("entity.infinity.pawn_white");
            default -> super.getTypeName();
        };
    }

    public int getCase() {
        return entityData.get(special_case);
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
        //? if >1.21.4 {
        NbtUtils.putColors(nbt, "colors", getColors());
        //?} else {
        /*nbt.put("colors", getColors());
        *///?}
        nbt.putInt("case", getCase());
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
        //? if >1.21.4 {
        this.setColors(NbtUtils.getColors(nbt, "colors"));
        //?} else {
        /*this.setColors(nbt.getCompound("colors"));
        *///?}
        this.entityData.set(special_case, NbtUtils.getInt(nbt, "case"));
    }

//    @Override
    public
    //? if >1.21 {
    ResourceKey<LootTable>
    //?} else {
    /*ResourceLocation
    *///?}
     getDefaultLootTable() {
        ResourceLocation i = switch (getCase()) {
            case 0 -> InfinityMethods.getId("entities/chaos_pawn_black");
            case 1 -> InfinityMethods.getId("entities/chaos_pawn_white");
            default -> {
                boolean bl = RandomProvider.rule("pawnsCanDropIllegalItems");
                if (bl) yield VersionMethods.id(""); //loot is defined in dropEquipment instead
                else yield VersionMethods.id(InfinityMod.provider.randomName(random, ConfigType.LOOT_TABLES));
            }
        };
        //? if >1.21 {
        return ResourceKey.create(Registries.LOOT_TABLE, i);
        //?} else {
        /*return i;
        *///?}
    }

    public static CompoundTag getColorSetup(Supplier<Integer> colorSupplier) {
        CompoundTag c = new CompoundTag();
        Arrays.stream((new String[]{"body", "left_arm", "right_arm", "left_leg", "right_leg"})).forEach(
                s -> c.putInt(s, colorSupplier.get()));
        int head = colorSupplier.get();
        c.putInt("head", head);
        c.putInt("hat", 0xFFFFFF ^ head);
        return c;
    }

    public void setAllColors(int color) {
        this.setColors(getColorSetup(() -> color));
    }

    @Override
    public boolean isBlackOrWhite() {
        return entityData.get(special_case) != -1 && !Iridescence.isUnderEffect(this);
    }

    public void initFromBlock(BlockState state) {
        if (state.is(Blocks.WHITE_WOOL) || state.is(Blocks.WHITE_CONCRETE)) {
            chess(true);
        }
        else if (state.is(Blocks.BLACK_WOOL) || state.is(Blocks.BLACK_CONCRETE)) {
            chess(false);
        }
        else unchess();
    }

    public void chess(boolean white) {
        entityData.set(special_case, white ? 1 : 0);
        setAllColors(white ? 0xFFFFFF : 0);
    }
    
    public void unchess() {
        entityData.set(special_case, -1);
        setColors(getColorSetup(() -> random.nextInt(16777216)));
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
        initFromBlock(world.getBlockState(this.blockPosition().below()));
        double i = random.nextDouble() * 40;
        Objects.requireNonNull(this.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(i);
        this.setHealth((float)i);
        return super.finalizeSpawn(world, difficulty, spawnReason, entityData
            //? if <1.21
            /*,tag*/
        );
    }

    //? if >1.21 {
    @Override
    protected void dropCustomDeathLoot(ServerLevel world, DamageSource source, boolean causedByPlayer) {
        super.dropCustomDeathLoot(world, source, causedByPlayer);
    //?} else {
    /*@Override
    protected void dropCustomDeathLoot(DamageSource source, int lootingMultiplier, boolean allowDrops) {
        super.dropCustomDeathLoot(source, lootingMultiplier, allowDrops);
        *///?}
        dropLootInternal();
    }

    protected void dropLootInternal() {
        if (!this.isBlackOrWhite() && RandomProvider.rule("pawnsCanDropIllegalItems")) {
            String s = InfinityMod.provider.randomName(random, ConfigType.ITEMS);
            double i = Objects.requireNonNull(this.getAttribute(Attributes.MAX_HEALTH)).getBaseValue() / 10;
            ItemStack stack = VersionMethods.getItem(s).getDefaultInstance().copyWithCount((int) (i * i));
            //? if >1.21 {
            stack.applyComponents(DataComponentMap.builder().set(DataComponents.MAX_STACK_SIZE, 64).build());
             //?}
            this.spawnAtLocation(
                    //? if >1.21.2 {
                    (ServerLevel) this.level(),
                    //?}
                    stack);
        }
    }
}
