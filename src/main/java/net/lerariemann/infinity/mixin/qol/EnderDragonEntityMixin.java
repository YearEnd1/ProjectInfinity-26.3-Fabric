package net.lerariemann.infinity.mixin.qol;

import net.lerariemann.infinity.util.InfinityMethods;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnderDragon.class)
public abstract class EnderDragonEntityMixin extends Mob {
    protected EnderDragonEntityMixin(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }
    /* Don't destroy blocks in infdims */
    @Inject(method = "checkWalls", at = @At(value = "HEAD",
            target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"), cancellable = true)
    void inj(AABB box, CallbackInfoReturnable<Boolean> cir) {
        if(InfinityMethods.isInfinity(level())) cir.setReturnValue(true);
    }
}
