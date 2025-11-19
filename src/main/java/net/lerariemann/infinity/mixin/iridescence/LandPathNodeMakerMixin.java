package net.lerariemann.infinity.mixin.iridescence;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.lerariemann.infinity.entity.custom.AbstractChessFigure;
import net.lerariemann.infinity.iridescence.Iridescence;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


//? if >1.21 {
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.PathfindingContext;
//?} else {
/*import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
*///?}

@Mixin(WalkNodeEvaluator.class)
public abstract class LandPathNodeMakerMixin extends NodeEvaluator {
    /* Chess-type mobs avoid iridescence */
    //? if >1.21 {
    @Inject(method = "getPathTypeOfMob(Lnet/minecraft/world/level/pathfinder/PathfindingContext;IIILnet/minecraft/world/entity/Mob;)Lnet/minecraft/world/level/pathfinder/PathType;",
            at = @At("HEAD"), cancellable = true)
    private void inj(PathfindingContext context, int x, int y, int z, Mob mob, CallbackInfoReturnable<PathType> cir) {
     //?} else {
    /*@Inject(method = "getBlockPathType(Lnet/minecraft/world/level/BlockGetter;IIILnet/minecraft/world/entity/Mob;)Lnet/minecraft/world/level/pathfinder/BlockPathTypes;",
                at = @At("HEAD"), cancellable = true)
    private void inj(BlockGetter level, int x, int y, int z, Mob mob, CallbackInfoReturnable<BlockPathTypes> cir) {
    *///?}
        if (mob != null && Iridescence.isIridescence(mob.level(), new BlockPos(x, y, z))) {
            if (mob instanceof AbstractChessFigure figure && figure.isBlackOrWhite())
                cir.setReturnValue(
                        //? if >1.21 {
                        PathType
                        //?} else {
                        /*BlockPathTypes
                        *///?}
                        .BLOCKED);
        }
    }

    /* This allows other mobs pathfind when swimming in iridescence */
    @ModifyExpressionValue(method= "getStart",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"))
    boolean inj(boolean original, @Local BlockState blockState) {
        return original || blockState.getFluidState().is(FluidTags.WATER);
    }
}
