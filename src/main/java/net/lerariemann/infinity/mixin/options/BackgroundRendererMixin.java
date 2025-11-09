package net.lerariemann.infinity.mixin.options;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.lerariemann.infinity.options.InfinityOptions;
import net.lerariemann.infinity.util.InfinityMethods;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.level.material.FogType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.Color;

@Mixin(FogRenderer.class)
public class BackgroundRendererMixin {
    @Shadow
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
            infinity$applyRainbowFog(world, tickDelta);
        }
    }

    /* Disable void fog in custom dimensions. */
    @ModifyExpressionValue(method = "setupColor(Lnet/minecraft/client/Camera;FLnet/minecraft/client/multiplayer/ClientLevel;IF)V",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel$ClientLevelData;getClearColorScale()F"))
    private static float inj(float original, @Local(argsOnly = true) ClientLevel world) {
        if (!InfinityMethods.isInfinity(world)) return original;
        return InfinityOptions.access(world).getHorizonShadingRatio();
    }

    @Unique
    private static void infinity$applyRainbowFog(ClientLevel world, float tickDelta) {
        float main = world.getTimeOfDay(tickDelta) * 2 + 0.5f;
        main -= (int)main;
        int color = Color.getHSBColor(main, 1.0f, 1.0f).getRGB();
        float f = Mth.clamp(Mth.cos(world.getTimeOfDay(tickDelta) * ((float)Math.PI * 2)) * 2.0f + 0.5f, 0.2f, 1.0f);
        fogRed = f * (float)(color >> 16 & 0xFF) / 255.0f;
        fogGreen = f * (float)(color >> 8 & 0xFF) / 255.0f;
        fogBlue = f * (float)(color & 0xFF) / 255.0f;
    }
}
