package net.codexarchonic.infinity.mixin.mavity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ThrowableProjectile.class)
public abstract class ThrownEntityMixin extends Projectile {
    public ThrownEntityMixin(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
    }

    //? if <1.21 {

    /*@Shadow protected abstract float getGravity();

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/ThrowableProjectile;getGravity()F"))
    protected float getGravity(ThrowableProjectile instance, Operation<Float> original) {
        return (float)getMavity(this.level()) * getGravity();
    }
    *///?}
}
