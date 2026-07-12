package net.codexarchonic.infinity.mixin.mavity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    //? if <1.21 {
    /*@ModifyArg(method = "calculateFallDamage", at = @At(value="INVOKE", target="Lnet/minecraft/util/Mth;ceil(F)I"))
    float mavity(float value) {
        return (float)getMavity(this.level()) * value;
    }

    @ModifyConstant(method = "travel", constant = @Constant(doubleValue = 0.08))
    double mavity2(double value) {
        return value*getMavity(this.level());
    }
    *///?}
}
