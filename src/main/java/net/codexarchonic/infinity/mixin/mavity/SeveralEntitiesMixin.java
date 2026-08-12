package net.codexarchonic.infinity.mixin.mavity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.projectile.LlamaSpit;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import static net.codexarchonic.infinity.util.InfinityMethods.getMavity;

@Mixin({FallingBlockEntity.class, PrimedTnt.class, ItemEntity.class, ExperienceOrb.class, AbstractMinecart.class, LlamaSpit.class})
public abstract class SeveralEntitiesMixin extends Entity {

    public SeveralEntitiesMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    //? if <1.21 {
    /*@ModifyArg(method = "tick", at = @At(value="INVOKE", target="Lnet/minecraft/world/phys/Vec3;add(DDD)Lnet/minecraft/world/phys/Vec3;"), index = 1)
    double mavity(double x) {
        return getMavity(this.level()) * x;
    }
    *///?}
}
