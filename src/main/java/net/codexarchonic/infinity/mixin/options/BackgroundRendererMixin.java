package net.codexarchonic.infinity.mixin.options;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.codexarchonic.infinity.options.InfinityOptions;
//? if >1.21.2 {
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.fog.FogRenderer;
//?} else {
/*import net.codexarchonic.infinity.util.InfinityMethods;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.FogRenderer;
*///?}
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.material.FogType;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.awt.*;

@Mixin(FogRenderer.class)
public class BackgroundRendererMixin {
    //? if <1.21.2 {
    /*@Shadow
    private static float fogRed;
    @Shadow
    private static float fogGreen;
    @Shadow
    private static float fogBlue;


    @Inject(method = "setupColor(Lnet/minecraft/client/Camera;FLnet/minecraft/client/multiplayer/ClientLevel;IF)V",
            at= @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/FogRenderer;getPriorityFogFunction(Lnet/minecraft/world/entity/Entity;F)Lnet/minecraft/client/renderer/FogRenderer$MobEffectFogFunction;"))
    private static void injected(Camera camera, float tickDelta, ClientLevel world, int viewDistance, float skyDarkness, CallbackInfo ci) {
        InfinityOptions options = InfinityOptions.access(world);
        if (options.getSkyType().equals("rainbow") && camera.getFluidInCamera() == FogType.NONE) {
            var vec4f = infinity$applyRainbowFog(world, tickDelta);
            fogRed = vec4f.x;
            fogGreen = vec4f.y;
            fogBlue = vec4f.z;
        }
    }

    /^ Disable void fog in custom dimensions. ^/
    @ModifyExpressionValue(method = "setupColor(Lnet/minecraft/client/Camera;FLnet/minecraft/client/multiplayer/ClientLevel;IF)V",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel$ClientLevelData;getClearColorScale()F"))
    private static float inj(float original, @Local(argsOnly = true) ClientLevel world) {
        if (!InfinityMethods.isInfinity(world)) return original;
        return InfinityOptions.access(world).getHorizonShadingRatio();
    }


    *///?} else {

    @ModifyReturnValue(method = "computeFogColor",
            at= @At(value = "RETURN"))
    private static Vector4f injected(Vector4f original, Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, boolean isFoggy) {
        InfinityOptions options = InfinityOptions.access(level);
        if (options.getSkyType().equals("rainbow") && camera.getFluidInCamera() == FogType.NONE) {
            return infinity$applyRainbowFog(level, Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaTicks());
        }
        return original;
    }

    //?}

    @Unique
    private static Vector4f infinity$applyRainbowFog(ClientLevel world, float tickDelta) {
        float main = world.getTimeOfDay(tickDelta) * 2 + 0.5f;
        main -= (int)main;
        int color = Color.getHSBColor(main, 1.0f, 1.0f).getRGB();
        float f = Mth.clamp(Mth.cos(world.getTimeOfDay(tickDelta) * ((float)Math.PI * 2)) * 2.0f + 0.5f, 0.2f, 1.0f);
        var red = f * (float)(color >> 16 & 0xFF) / 255.0f;
        var green = f * (float)(color >> 8 & 0xFF) / 255.0f;
        var blue = f * (float)(color & 0xFF) / 255.0f;
        return new Vector4f(red, green, blue, 1.0F);
    }

}
