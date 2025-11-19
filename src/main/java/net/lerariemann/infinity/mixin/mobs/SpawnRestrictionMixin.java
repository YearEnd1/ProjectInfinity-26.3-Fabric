package net.lerariemann.infinity.mixin.mobs;

import net.lerariemann.infinity.util.InfinityMethods;
import net.lerariemann.infinity.util.var.InfinitySpawnHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Assumes direct control over restrictions on mob spawning in infinity biomes. */
@Mixin(SpawnPlacements.class)
public class SpawnRestrictionMixin {
    @Inject(method = "checkSpawnRules", at = @At("HEAD"), cancellable = true)
    private static <T extends Entity> void inj(EntityType<T> type, ServerLevelAccessor world, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random, CallbackInfoReturnable<Boolean> cir) {
        if (InfinityMethods.isBiomeInfinity(world, pos)) {
            cir.setReturnValue(InfinitySpawnHelper.canSpawn(type, world, spawnReason, pos, random));
        }
    }
}
