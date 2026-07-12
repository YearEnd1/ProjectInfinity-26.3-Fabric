package net.codexarchonic.infinity.mixin.mavity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractArrow.class)
public abstract class PersistentProjectileEntityMixin extends Projectile {

    public PersistentProjectileEntityMixin(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
    }

    //? if <1.21 {
    /*@ModifyArg(method = "tick", at = @At(value="INVOKE", target="Lnet/minecraft/world/entity/projectile/AbstractArrow;setDeltaMovement(DDD)V"), index = 1)
    double arrowMavity(double x) {
        return x - 0.05 * (getMavity(level()) - 1);
    }
    *///?}
}
