package net.lerariemann.infinity.mixin.fixes;

import net.lerariemann.infinity.access.MobEntityAccess;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Mob.class)
public class MobEntityMixin implements MobEntityAccess {
    @Shadow
    private boolean persistenceRequired;

    public void infinity$setPersistent(boolean bl) {
        this.persistenceRequired = bl;
    }
}
