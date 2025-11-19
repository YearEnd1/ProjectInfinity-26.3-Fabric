package net.lerariemann.infinity.mixin.mobs;

import net.lerariemann.infinity.util.InfinityMethods;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.entity.SpawnGroupData;
//? if >1.21.5 {
import net.minecraft.world.entity.animal.sheep.Sheep;
//?} else {
/*import net.minecraft.world.entity.animal.Sheep;
*///?}
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Sheep.class)
public abstract class SheepEntityMixin extends LivingEntityMixin implements Shearable {
    protected SheepEntityMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Shadow public abstract void setColor(DyeColor color);

    //? if <1.21.5 {
    /*@Shadow public abstract void shear(SoundSource shearedSoundCategory);
    *///?} else {
    @Shadow public abstract void shear(ServerLevel serverLevel, SoundSource shearedSoundCategory, ItemStack stack);
    //?}

    @Shadow public abstract boolean isSheared();

    /* Makes sheep drop their wool when punched in the Classic biome. */
    @Override
    protected void injected_sheep(
            //? if >1.21.5 {
            ServerLevel serverLevel,
            //?}
            DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if(cir.getReturnValue() && !this.isSheared() && source.getEntity() != null &&
                source.getEntity() instanceof Player &&
                level().getBiome(blockPosition())
                        //? if >1.21 {
                        .getRegisteredName().equals("infinity:classic"))
        //?} else {
                        /*.is(InfinityMethods.getId("classic")))
        *///?}
        {
            //? if >1.21.5 {
            this.shear(serverLevel, SoundSource.AMBIENT, Items.SHEARS.getDefaultInstance());
            //?} else {
            /*this.shear(SoundSource.AMBIENT);
            *///?}
        }
    }

    /* Controls wool color with which sheep spawn based on the biome. */
    //? if >1.21.2 {
    @Inject(method = "finalizeSpawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/sheep/Sheep;setColor(Lnet/minecraft/world/item/DyeColor;)V", shift = At.Shift.AFTER))
    //?} else {
    /*@Inject(method = "finalizeSpawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/Sheep;setColor(Lnet/minecraft/world/item/DyeColor;)V", shift = At.Shift.AFTER))
    *///?}
    private void injected(ServerLevelAccessor world, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData entityData,
    //? if <1.21 {
    /*CompoundTag compoundTag,
    *///?}
    CallbackInfoReturnable<SpawnGroupData> cir) {
        if (world.getBiome(blockPosition()).is(InfinityMethods.getId("classic"))) {
            setColor(DyeColor.WHITE);
        }
        else if (InfinityMethods.isBiomeInfinity(world, blockPosition())) {
            setColor(DyeColor.byId(world.getRandom().nextInt(16)));
        }
    }
}
