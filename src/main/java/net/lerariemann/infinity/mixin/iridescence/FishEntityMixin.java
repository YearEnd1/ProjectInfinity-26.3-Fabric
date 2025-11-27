package net.lerariemann.infinity.mixin.iridescence;

import net.lerariemann.infinity.iridescence.Iridescence;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

/* These two mixin classes are to make fish swim backwards in iridescence */
@Mixin(AbstractFish.class)
public class FishEntityMixin extends WaterAnimal {
    protected FishEntityMixin(EntityType<? extends WaterAnimal> entityType, Level world) {
        super(entityType, world);
    }

    @ModifyArg(method="travel", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/animal/AbstractFish;moveRelative(FLnet/minecraft/world/phys/Vec3;)V"),
    index = 1)
    Vec3 fishSwimBackwards(Vec3 par2) {
        if (Iridescence.isUnderEffect(this)) {
            return par2.multiply(-1, 1, -1);
        }
        return par2;
    }

    @Mixin(AbstractFish.FishMoveControl.class)
    public static class FishMoveControlMixin extends MoveControl {
        @Final
        @Shadow
        private AbstractFish fish;

        public FishMoveControlMixin(Mob entity) {
            super(entity);
        }

        //? if forge {
        /*@ModifyArg(method = "tick", at= @At(value = "INVOKE",
                target = "Lnet/minecraft/util/Mth;atan2(DD)D"), index = 0)
        double inj(double y) {
            if (Iridescence.isUnderEffect(fish)) {
                return -y;
            }
            return y;
        }
        @ModifyArg(method = "tick", at= @At(value = "INVOKE",
                target = "Lnet/minecraft/util/Mth;atan2(DD)D"), index = 1)
        double inj2(double y) {
            if (Iridescence.isUnderEffect(fish)) {
                return -y;
            }
            return y;
        }
        *///?} else {
        @ModifyArgs(method = "tick", at= @At(value = "INVOKE",
                target = "Lnet/minecraft/util/Mth;atan2(DD)D"))
        void fishSwimBackwards(Args args) {
            if (Iridescence.isUnderEffect(fish)) {
                double g = args.get(0);
                double d = args.get(1);
                args.set(0, -1*g);
                args.set(1, -1*d);
            }
        }
        //?}
    }
}
