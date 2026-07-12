package net.codexarchonic.infinity.mixin.core;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/* Provides hooks for NetherPortalBlockMixin. */
@Mixin(BlockBehaviour.class)
public class AbstractBlockMixin {
    @Inject(method = "onPlace", at = @At("RETURN"))
    protected void injected_onBlockAdded(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify, CallbackInfo ci) {}

    @Inject(method = "tick(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;)V", at = @At("RETURN"))
    protected void injected_scheduledTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random, CallbackInfo ci) {}
}
