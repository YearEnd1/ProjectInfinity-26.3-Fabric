package net.codexarchonic.infinity.mixin.options;

import com.llamalad7.mixinextras.sugar.Local;
import net.codexarchonic.infinity.options.InfinityOptions;
import net.codexarchonic.infinity.util.VersionMethods;
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
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

//? if >26 {
/*@Mixin(AirBasedFogEnvironment.class)
public class FogRendererMixin {
    @ModifyArgs(method = "getBaseColor", at = @At(value = "INVOKE", target = "Lorg/joml/Vector3fc;dot(FFF)F"))
    private static void customFog(Args args, @Local(name = "partialTicks") float partialTicks, @Local(argsOnly = true) ClientLevel level) {
        var vector =  getVector3fc(new Vector3f(args.get(0), args.get(1), args.get(2)), partialTicks, level);
        args.set(0, vector.x());
        args.set(1, vector.y());
        args.set(2, vector.z());
    }
*///?} else if >1.21.2 {
@Mixin(AirBasedFogEnvironment.class)
public class FogRendererMixin {
    @ModifyArg(method = "getBaseColor", at = @At(value = "INVOKE", target = "Lorg/joml/Vector3f;dot(Lorg/joml/Vector3fc;)F"))
    private static Vector3fc customFog(Vector3fc v, @Local(argsOnly = true, ordinal = 0) float partialTicks, @Local(argsOnly = true) ClientLevel level) {
        return getVector3fc(v, partialTicks, level);
    }
//?} else {
/*@Mixin(FogRenderer.class)
public class FogRendererMixin {
    @ModifyArg(method = "setupColor", at = @At(value = "INVOKE", target = "Lorg/joml/Vector3f;dot(Lorg/joml/Vector3fc;)F"))
    private static Vector3fc customFog(Vector3fc v, @Local(argsOnly = true, ordinal = 0) float partialTicks, @Local(argsOnly = true) ClientLevel level) {
        return getVector3fc(v, partialTicks, level);
    }
*///?}


    @Unique
	private static Vector3fc getVector3fc(Vector3fc v, float partialTicks, ClientLevel level) {
        float solarTilt = InfinityOptions.ofClient().getSolarTilt();
        if (solarTilt != -90.0F) {
            boolean isSunset = Mth.sin((float) VersionMethods.getSunAngle(level, partialTicks)) > 0.0F;
            float angle = (solarTilt + (isSunset ? 0.0f : 180.0f)) * (float) (Math.PI / 180.0f);
            return new Vector3f(Mth.sin(angle), 0, Mth.cos(angle));
        }
        else return v;
    }
}