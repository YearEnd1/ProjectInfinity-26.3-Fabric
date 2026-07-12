package net.codexarchonic.infinity.mixin.fixes;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.animal.Bee;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** By some lunacy Mojang made a thread-unsafe random call in a really inconvenient place,
 * which spams logs with warnings when a bee tries to spawn.
 * I fixed that :D */
@Mixin(Bee.BeeGoToHiveGoal.class)
public abstract class BeeGoalMixin {
    //? if <1.21.2 {
    /*@WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextInt(I)I"))
    private int fixBeeCrash(RandomSource instance, int i, Operation<Integer> original) {
        return 10;
    }
    *///?}
    @Mixin(Bee.BeeGoToKnownFlowerGoal.class)
    public abstract static class BeeGoalMixin2 {
        //? if <1.21.2 {
        /*@WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextInt(I)I"))
        private int fixBeeCrash(RandomSource instance, int i, Operation<Integer> original) {
            return 10;
        }
        *///?}
    }
}
