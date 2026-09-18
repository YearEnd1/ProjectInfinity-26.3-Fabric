package net.codexarchonic.infinity.mixin.qol;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.codexarchonic.infinity.util.InfinityMethods;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.FireBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FireBlock.class)
public class FireBlockMixin {
    /* Disabling fire tick in infdims */
    //~ if >26 'Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z'->'Lnet/minecraft/server/level/ServerLevel;canSpreadFireAround(Lnet/minecraft/core/BlockPos;)Z'
    @ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"))
    boolean disableFireTick(boolean original, @Local(argsOnly = true) ServerLevel world) {
        if (InfinityMethods.isInfinity(world)) return false;
        return original;
    }
}
