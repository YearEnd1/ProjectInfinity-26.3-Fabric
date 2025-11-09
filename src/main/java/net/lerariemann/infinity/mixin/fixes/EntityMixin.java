package net.lerariemann.infinity.mixin.fixes;

import net.lerariemann.infinity.util.platform.PlatformMethods;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
    /* Neoforge-exclusive mixins working around its code (which causes mobs to be unable to swim in iridescence) */
    @Inject(method="updateFluidHeightAndDoFluidPushing", at = @At(value = "RETURN"), cancellable = true)
    void inj(TagKey<Fluid> tag, double speed, CallbackInfoReturnable<Boolean> cir) {
        if (tag.equals(FluidTags.WATER))
            if (PlatformMethods.acidTest((Entity)(Object)this, false))
                cir.setReturnValue(true);
    }

    @Inject(method = "isEyeInFluid", at = @At("RETURN"), cancellable = true)
    void inj(TagKey<Fluid> fluidTag, CallbackInfoReturnable<Boolean> cir) {
        if (fluidTag.equals(FluidTags.WATER))
            if (PlatformMethods.acidTest((Entity)(Object)this, true))
                cir.setReturnValue(true);
    }

    @Inject(method = "getFluidHeight", at = @At("RETURN"), cancellable = true)
    void inj2(TagKey<Fluid> fluid, CallbackInfoReturnable<Double> cir) {
        if (fluid.equals(FluidTags.WATER))
            cir.setReturnValue(Math.max(cir.getReturnValue(), PlatformMethods.acidHeightTest((Entity)(Object)this)));
    }
}

