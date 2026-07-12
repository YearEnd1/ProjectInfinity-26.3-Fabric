package net.codexarchonic.infinity.mixin.mobs;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.codexarchonic.infinity.util.InfinityMethods;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.entity.animal.TropicalFish;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TropicalFish.class)
public abstract class TropicalFishEntityMixin extends AbstractSchoolingFish {
    public TropicalFishEntityMixin(EntityType<? extends AbstractSchoolingFish> entityType, Level world) {
        super(entityType, world);
    }

    /* In infinity biomes, tropical fish spawn in a fully random variant. */
    @ModifyExpressionValue(method="finalizeSpawn", at= @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextFloat()F"))
    float randomTropicalFish(float original, @Local(argsOnly = true) ServerLevelAccessor world){
        if (InfinityMethods.isBiomeInfinity(world, blockPosition())) return 1.0f;
        return original;
    }
}
