package net.lerariemann.infinity.mixin.options;

import com.llamalad7.mixinextras.sugar.Local;
import net.lerariemann.infinity.options.InfinityOptions;
import net.minecraft.client.multiplayer.ClientLevel;
//? if >1.21.2 {
import net.minecraft.client.renderer.fog.environment.AirBasedFogEnvironment;
//?} else {
/*import net.minecraft.client.renderer.FogRenderer;
*///?}
import net.minecraft.util.Mth;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

//? if >1.21.2 {
@Mixin(AirBasedFogEnvironment.class)
public class FogRendererMixin {
    @ModifyArg(method = "getBaseColor", at = @At(value = "INVOKE", target = "Lorg/joml/Vector3f;dot(Lorg/joml/Vector3fc;)F"))
//?} else {
/*@Mixin(FogRenderer.class)
public class FogRendererMixin {
    @ModifyArg(method = "setupColor", at = @At(value = "INVOKE", target = "Lorg/joml/Vector3f;dot(Lorg/joml/Vector3fc;)F"))
*///?}
    private static Vector3fc inj(Vector3fc v, @Local(argsOnly = true, ordinal = 0) float partialTicks, @Local(argsOnly = true) ClientLevel level) {
        float solarTilt = InfinityOptions.ofClient().getSolarTilt();
        if (solarTilt != -90.0F) {
            boolean isSunset = Mth.sin(level.getSunAngle(partialTicks)) > 0.0F;
            float angle = (solarTilt + (isSunset ? 0.0f : 180.0f)) * (float) (Math.PI / 180.0f);
            return new Vector3f(Mth.sin(angle), 0, Mth.cos(angle));
        }
        else return v;
    }
}