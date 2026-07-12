package net.codexarchonic.infinity.mixin.iridescence;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GroundPathNavigation.class)
public class MobNavigationMixin {
    /* This allows mobs pathfind when swimming in iridescence */
    @ModifyExpressionValue(method="getSurfaceY", at= @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"))
    boolean allowPathfindingInIridescence(boolean original, @Local BlockState blockState) {
        return original || (blockState.getFluidState().is(FluidTags.WATER) &&
                !blockState.getFluidState().equals(Fluids.WATER.getSource(false)));
    }
}
