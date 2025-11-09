package net.lerariemann.infinity.mixin.options;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexBuffer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lerariemann.infinity.access.WorldRendererAccess;
import net.lerariemann.infinity.options.InfinityOptions;
import net.lerariemann.infinity.options.SkyRenderer;
import net.lerariemann.infinity.util.InfinityMethods;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(LevelRenderer.class)
public abstract class WorldRendererMixin implements WorldRendererAccess {
    @Shadow private ClientLevel level;
    @Final
    @Shadow private Minecraft minecraft;
    @Shadow private VertexBuffer skyBuffer;
    @Shadow private VertexBuffer starBuffer;

    @Shadow protected abstract void createStars();
    @Shadow protected abstract boolean doesMobEffectBlockSky(Camera camera);

    @Shadow public abstract void renderLevel(DeltaTracker tickCounter, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightmapTextureManager, Matrix4f matrix4f, Matrix4f matrix4f2);

    @Override
    public void infinity$setNeedsStars(boolean b) {
        createStars();
    }

    @Inject(method = "renderSky(Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;FLnet/minecraft/client/Camera;ZLjava/lang/Runnable;)V",
            at=@At("HEAD"), cancellable=true)
    private void injected4(Matrix4f matrix4f, Matrix4f projectionMatrix, float tickDelta, Camera camera, boolean thickFog, Runnable fogCallback, CallbackInfo ci) {
        if (InfinityMethods.isInfinity(level)) {
            infinity$renderEntireSky(matrix4f, projectionMatrix, tickDelta, camera, thickFog, fogCallback);
            ci.cancel();
        }
    }
    @ModifyConstant(method = "drawStars(Lcom/mojang/blaze3d/vertex/Tesselator;)Lcom/mojang/blaze3d/vertex/MeshData;", constant = @Constant(intValue = 1500))
    private int injected(int constant) {
        return infinity$options().getNumStars();
    }
    @ModifyConstant(method = "drawStars(Lcom/mojang/blaze3d/vertex/Tesselator;)Lcom/mojang/blaze3d/vertex/MeshData;", constant = @Constant(floatValue = 0.15f))
    private float injected2(float constant) {
        return infinity$options().getStarSizeBase();
    }
    @ModifyConstant(method = "drawStars(Lcom/mojang/blaze3d/vertex/Tesselator;)Lcom/mojang/blaze3d/vertex/MeshData;", constant = @Constant(floatValue = 0.1f))
    private float injected3(float constant) {
        return infinity$options().getStarSizeModifier();
    }

    @Unique
    private void infinity$renderEntireSky(Matrix4f matrix4f, Matrix4f projectionMatrix, float tickDelta, Camera camera, boolean thickFog, Runnable fogCallback) {
        fogCallback.run();
        if (thickFog || doesMobEffectBlockSky(camera) || SkyRenderer.testCameraCancels(camera)) return;
        PoseStack matrices = new PoseStack();
        matrices.mulPose(matrix4f);
        SkyRenderer renderer = new SkyRenderer(infinity$options(), minecraft, level,
                matrices, tickDelta, projectionMatrix,
                skyBuffer, starBuffer);
        renderer.render(fogCallback);
    }
    @Unique
    private InfinityOptions infinity$options() {
        return InfinityOptions.ofClient(minecraft);
    }
}
