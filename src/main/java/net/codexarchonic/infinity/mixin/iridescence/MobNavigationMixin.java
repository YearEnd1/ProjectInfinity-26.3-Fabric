package net.codexarchonic.infinity.mixin.iridescence;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 26.3: {@code GroundPathNavigation.getSurfaceY()} no longer calls
 * {@code BlockState.is(Block)}; the floatable-fluid decision is the inherited
 * {@code Entity.isInFloatableFluid()}. Reporting iridescence (a custom
 * water-tagged fluid) as floatable lets mobs pathfind while swimming in it.
 */
@Mixin(Entity.class)
public class MobNavigationMixin {
    @Inject(method = "isInFloatableFluid", at = @At("RETURN"), cancellable = true)
    private void infinity$allowPathfindingInIridescence(CallbackInfoReturnable<Boolean> cir) {
        if (Boolean.TRUE.equals(cir.getReturnValue())) {
            return;
        }
        Entity self = (Entity) (Object) this;
        BlockState blockState = self.level().getBlockState(
                BlockPos.containing(self.getX(), self.getY(), self.getZ()));
        if (blockState.getFluidState().is(FluidTags.WATER)
                && !blockState.getFluidState().equals(Fluids.WATER.getSource(false))) {
            cir.setReturnValue(true);
        }
    }
}
