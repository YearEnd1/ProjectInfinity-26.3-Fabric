package net.codexarchonic.infinity.mixin.options;

import net.codexarchonic.infinity.access.InfinityOptionsAccess;
import net.codexarchonic.infinity.options.InfinityOptions;
import net.minecraft.world.level.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(DimensionType.class)
public abstract class DimensionTypeMixin implements InfinityOptionsAccess {
    @Unique
    public InfinityOptions infinity$options;

    //? if <26 {
    @ModifyArg(method = "timeOfDay", at = @At(value="INVOKE", target="Lnet/minecraft/util/Mth;frac(D)D"), index = 0)
    private double customTimeOfDay(double value) {
        try {
            double timescale = infinity$getOptions().getTimeScale();
            return timescale*(value + 0.25) - 0.25;
        } catch (Exception e) {
            return value;
        }
    }
    //?}
    //TODO 26.1
    public InfinityOptions infinity$getOptions() {
        return InfinityOptions.nullSafe(infinity$options);
    }
    @Override
    public void infinity$setOptions(InfinityOptions options) {
        infinity$options = options;
    }
}
