package net.lerariemann.infinity.mixin.mobs;

import net.lerariemann.infinity.entity.custom.ChaosSlime;
import net.minecraft.world.entity.monster.Slime;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static net.lerariemann.infinity.entity.custom.ChaosSlime.color;

@Mixin(Slime.class)
public class SlimeEntityMixin {
    /* Allows chaos slimes to correctly inherit their additional data when split. */
    @Redirect(method = "remove(Lnet/minecraft/world/entity/Entity$RemovalReason;)V", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/monster/Slime;setNoAi(Z)V"))
    private void injected(Slime instance, boolean b) {
        instance.setNoAi(b);
        Slime e = ((Slime)(Object)(this));
        if (e instanceof ChaosSlime slime_mom) {
            ChaosSlime slime_son = (ChaosSlime)instance;
            slime_son.setCore(slime_mom.getCoreForChild());
            slime_son.setColor(slime_mom.getEntityData().get(color));
        }
    }
}
