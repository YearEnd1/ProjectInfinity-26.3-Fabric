package net.codexarchonic.infinity.mixin.mobs;

import net.codexarchonic.infinity.entity.custom.ChaosSlime;
import net.minecraft.world.entity.monster.cubemob.AbstractCubeMob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.codexarchonic.infinity.entity.custom.ChaosSlime.color;

/**
 * 26.3: slime splitting lives in {@link AbstractCubeMob} (Slime extends it), so the
 * injection target moved there. Allows chaos slimes to inherit their additional data
 * when splitting.
 */
@Mixin(AbstractCubeMob.class)
public class SlimeEntityMixin {
    @Inject(method = "lambda$remove$0", at = @At(value = "RETURN"))
    private void chaosSlimesSplit(int i, float f, float g, AbstractCubeMob instance, CallbackInfo ci) {
        AbstractCubeMob e = (AbstractCubeMob) (Object) (this);
        if (e instanceof ChaosSlime slime_mom && instance instanceof ChaosSlime slime_son) {
            slime_son.setCore(slime_mom.getCoreForChild());
            slime_son.setColor(slime_mom.getEntityData().get(color));
        }
    }
}
