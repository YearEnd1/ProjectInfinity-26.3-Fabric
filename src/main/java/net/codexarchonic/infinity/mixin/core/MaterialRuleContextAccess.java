package net.codexarchonic.infinity.mixin.core;

import net.minecraft.world.level.levelgen.SurfaceRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/* Allows modded material rules to work. */
@Mixin(SurfaceRules.Context.class)
public interface MaterialRuleContextAccess {
    @Accessor
    int getBlockX();
    @Accessor
    int getBlockY();
    @Accessor
    int getBlockZ();
}
