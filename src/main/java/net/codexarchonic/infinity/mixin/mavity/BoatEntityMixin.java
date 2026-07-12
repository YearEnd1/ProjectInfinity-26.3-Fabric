package net.codexarchonic.infinity.mixin.mavity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Boat.class)
public abstract class BoatEntityMixin extends Entity {
    public BoatEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    //? if <1.21 {
    /*@ModifyVariable(method="floatBoat", at=@At("STORE"), ordinal=1)
    double boatMavity(double constant) {
        return constant * (this.isNoGravity() ? 0.0 : InfinityMethods.getMavity(level()));
    }
    *///?}
}