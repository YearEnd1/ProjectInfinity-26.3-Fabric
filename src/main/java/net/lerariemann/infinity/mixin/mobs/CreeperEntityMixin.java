package net.lerariemann.infinity.mixin.mobs;

import net.lerariemann.infinity.entity.custom.ChaosCreeper;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Creeper.class)
public abstract class CreeperEntityMixin {
    @Shadow
    private void spawnLingeringCloud(){}

    /* A hook for chaos creepers' explosion logic. */
    @Inject(method = "explodeCreeper()V", at = @At("HEAD"), cancellable = true)
    private void injected(CallbackInfo ci) {
        if (((Creeper)(Object)this) instanceof ChaosCreeper) {
            ChaosCreeper e = (ChaosCreeper)(Object)this;
            Level w = e.level();
            if (!w.isClientSide) {
                e.blow_up();
                spawnLingeringCloud();
                ci.cancel();
            }
        }
    }
}
