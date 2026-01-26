package net.lerariemann.infinity.mixin.mobs;

import net.lerariemann.infinity.entity.custom.ChaosSlime;
import net.minecraft.world.entity.monster.Slime;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.lerariemann.infinity.entity.custom.ChaosSlime.color;

@Mixin(Slime.class)
public class SlimeEntityMixin {
    /* Allows chaos slimes to correctly inherit their additional data when split. */
    //? if >1.21.2 && fabric {
    @Inject(method = "method_63653", at = @At(value = "RETURN"))
    private void chaosSlimesSplit(int i, float f, float g, Slime instance, CallbackInfo ci) {
        Slime e = ((Slime)(Object)(this));
        if (e instanceof ChaosSlime slime_mom) {
            ChaosSlime slime_son = (ChaosSlime)instance;
            slime_son.setCore(slime_mom.getCoreForChild());
            slime_son.setColor(slime_mom.getEntityData().get(color));
        }
    }
    //?} else if >1.21.2 {
    /*@Inject(method = "lambda$remove$1", at = @At(value = "RETURN"))
    private void chaosSlimesSplit(int i, float f, float g, Slime instance, CallbackInfo ci) {
        Slime e = ((Slime)(Object)(this));
        if (e instanceof ChaosSlime slime_mom) {
            ChaosSlime slime_son = (ChaosSlime)instance;
            slime_son.setCore(slime_mom.getCoreForChild());
            slime_son.setColor(slime_mom.getEntityData().get(color));
        }
    }
    *///?} else {
    /*@Redirect(method = "remove(Lnet/minecraft/world/entity/Entity$RemovalReason;)V", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/monster/Slime;setNoAi(Z)V"))
    private void chaosSlimesSplit(Slime instance, boolean b) {
        instance.setNoAi(b);
        Slime e = ((Slime)(Object)(this));
        if (e instanceof ChaosSlime slime_mom) {
            ChaosSlime slime_son = (ChaosSlime)instance;
            slime_son.setCore(slime_mom.getCoreForChild());
            slime_son.setColor(slime_mom.getEntityData().get(color));
        }
    }
    *///?}


}
