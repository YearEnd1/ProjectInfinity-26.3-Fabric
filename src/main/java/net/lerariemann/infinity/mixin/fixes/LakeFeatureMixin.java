package net.lerariemann.infinity.mixin.fixes;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.LakeFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** Mojang goofed their lakes into deprecation, i'm fixing :D */
@Mixin(LakeFeature.class)
public class LakeFeatureMixin {
    @WrapOperation(method="place(Lnet/minecraft/world/level/levelgen/feature/FeaturePlaceContext;)Z", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/WorldGenLevel;getBiome(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/core/Holder;"))
    Holder<Biome> fixVanillaLakeCrash(WorldGenLevel instance, BlockPos blockPos, Operation<Holder<Biome>> original,
                      @Local(argsOnly = true) FeaturePlaceContext<LakeFeature.Configuration> context) {
        return context.level().getBiome(context.origin());
    }
}
