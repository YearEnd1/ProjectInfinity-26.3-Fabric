package net.codexarchonic.infinity.mixin.core;

import net.codexarchonic.infinity.util.InfinityMethods;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(Player.class)
public class PlayerMixin {
    //? if <1.21 {
    /*@Inject(method = "findRespawnPositionAndUseSpawnBlock", at = @At("HEAD"), cancellable = true)
    private static void doNotRespawnInTimebombedDimension(ServerLevel serverLevel, BlockPos blockPos, float f, boolean bl, boolean bl2, CallbackInfoReturnable<Optional<Vec3>> cir) {
        if (InfinityMethods.isTimebombed(serverLevel)) cir.setReturnValue(Optional.empty());
    }
    *///?}
}
