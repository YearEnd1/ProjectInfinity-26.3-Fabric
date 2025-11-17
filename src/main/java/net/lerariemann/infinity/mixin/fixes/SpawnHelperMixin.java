package net.lerariemann.infinity.mixin.fixes;

import com.llamalad7.mixinextras.sugar.Local;
import net.lerariemann.infinity.access.MobEntityAccess;
import net.lerariemann.infinity.util.InfinityMethods;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Many mobs including vanilla elder guardians, which are never intended to spawn from a biome, are always set as persistent.
 * This leads to them not respecting mobcaps and lagging everything out.
 * This mixin fixes that :D */
@Mixin(NaturalSpawner.class)
public class SpawnHelperMixin {
    @Inject(method = "spawnCategoryForPosition(Lnet/minecraft/world/entity/MobCategory;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/chunk/ChunkAccess;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/NaturalSpawner$SpawnPredicate;Lnet/minecraft/world/level/NaturalSpawner$AfterSpawnCallback;)V",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;addFreshEntityWithPassengers(Lnet/minecraft/world/entity/Entity;)V"))
    private static void persistentMobSpawnFix(MobCategory group, ServerLevel world, ChunkAccess chunk, BlockPos pos, NaturalSpawner.SpawnPredicate checker, NaturalSpawner.AfterSpawnCallback runner, CallbackInfo ci,
                            @Local Mob mobEntity) {
        if (InfinityMethods.isBiomeInfinity(world, pos)) {
            ((MobEntityAccess)mobEntity).infinity$setPersistent(false);
        }
    }
}
