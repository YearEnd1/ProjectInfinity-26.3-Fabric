package net.codexarchonic.infinity.mixin.core;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Player.class)
public class PlayerMixin {
    //? if <1.21 {
    /*@Inject(method = "findRespawnPositionAndUseSpawnBlock", at = @At("HEAD"), cancellable = true)
    private static void doNotRespawnInTimebombedDimension(ServerLevel serverLevel, BlockPos blockPos, float f, boolean bl, boolean bl2, CallbackInfoReturnable<Optional<Vec3>> cir) {
        if (InfinityMethods.isTimebombed(serverLevel)) cir.setReturnValue(Optional.empty());
    }
    *///?}
}
