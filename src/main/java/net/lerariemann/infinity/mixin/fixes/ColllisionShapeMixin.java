package net.lerariemann.infinity.mixin.fixes;

import net.lerariemann.infinity.entity.custom.AntEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public class ColllisionShapeMixin {
    @Inject(method = "getCollisionShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;",
            at = @At("RETURN"), cancellable = true)
    private void antsStandOnWater(BlockGetter world, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
        //? if >1.21.3 {
        try {
        //?}
            FluidState fluidState = world.getFluidState(pos);
            if (!fluidState.is(FluidTags.WATER)) return;
            int level = fluidState.getAmount();
            if (level == 0) return;
            int trueLevel = 2 * (level - 1) + 1;
            if (context.isAbove(AntEntity.getWaterCollisionShape(trueLevel - 1), pos, true)
                    && context.canStandOnFluid(world.getFluidState(pos.above()), fluidState)) {
                cir.setReturnValue(Shapes.or(cir.getReturnValue(), AntEntity.getWaterCollisionShape(trueLevel)));
            }
            //? if >1.21.3 {
        } catch (Exception ignored) {}
        //?}
    }
}
