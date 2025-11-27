package net.lerariemann.infinity.mixin.options;

import com.mojang.blaze3d.vertex.PoseStack;
//? if <1.21.2 {
/*import com.mojang.blaze3d.vertex.VertexBuffer;
import net.lerariemann.infinity.options.SkyRenderer;
*///?}
import net.lerariemann.infinity.access.WorldRendererAccess;
import net.lerariemann.infinity.options.InfinityOptions;
import net.lerariemann.infinity.util.InfinityMethods;
import net.minecraft.client.Camera;
//? if >1.21 {
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
//?}
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
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

@Mixin(LevelRenderer.class)
public abstract class WorldRendererMixin implements WorldRendererAccess {
    @Shadow private ClientLevel level;
    @Shadow private Minecraft minecraft;

    @Override
    public void infinity$setNeedsStars(boolean b) {
        //? if <1.21.2 {
        /*createStars();
        *///?}
    }

    //? if <1.21.2 {

    /*@Final
    @Shadow private VertexBuffer skyBuffer;
    @Shadow private VertexBuffer starBuffer;

    @Shadow protected abstract void createStars();
    @Shadow protected abstract boolean doesMobEffectBlockSky(Camera camera);

    //? if >1.21 {
    @Inject(method = "renderSky(Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;FLnet/minecraft/client/Camera;ZLjava/lang/Runnable;)V",
            at=@At("HEAD"), cancellable=true)
    private void injected4(Matrix4f matrix4f, Matrix4f projectionMatrix, float tickDelta, Camera camera, boolean thickFog, Runnable fogCallback, CallbackInfo ci) {
    //?} else {
    /^@Inject(method = "renderSky(Lcom/mojang/blaze3d/vertex/PoseStack;Lorg/joml/Matrix4f;FLnet/minecraft/client/Camera;ZLjava/lang/Runnable;)V",
                at=@At("HEAD"), cancellable=true)
    private void injected4(PoseStack matrices, Matrix4f projectionMatrix, float tickDelta, Camera camera, boolean thickFog, Runnable fogCallback, CallbackInfo ci) {
    ^///?}
        if (InfinityMethods.isInfinity(level)) {
            //? if >1.21 {
            PoseStack matrices = new PoseStack();
            matrices.mulPose(matrix4f);
            //?}
            infinity$renderEntireSky(matrices, projectionMatrix, tickDelta, camera, thickFog, fogCallback);
            ci.cancel();
        }
    }
    //? if >1.21 {
    @ModifyConstant(method = "drawStars(Lcom/mojang/blaze3d/vertex/Tesselator;)Lcom/mojang/blaze3d/vertex/MeshData;", constant = @Constant(intValue = 1500))
     //?} else {
    /^@ModifyConstant(method = "drawStars(Lcom/mojang/blaze3d/vertex/BufferBuilder;)Lcom/mojang/blaze3d/vertex/BufferBuilder$RenderedBuffer;", constant = @Constant(intValue = 1500))
    ^///?}
    private int injected(int constant) {
        return infinity$options().getNumStars();
    }
    //? if >1.21 {
    @ModifyConstant(method = "drawStars(Lcom/mojang/blaze3d/vertex/Tesselator;)Lcom/mojang/blaze3d/vertex/MeshData;", constant = @Constant(floatValue = 0.15f))
    //?} else {
    /^@ModifyConstant(method = "drawStars(Lcom/mojang/blaze3d/vertex/BufferBuilder;)Lcom/mojang/blaze3d/vertex/BufferBuilder$RenderedBuffer;", constant = @Constant(floatValue = 0.15f))
    ^///?}
    private float injected2(float constant) {
        return infinity$options().getStarSizeBase();
    }
    //? if >1.21 {
    @ModifyConstant(method = "drawStars(Lcom/mojang/blaze3d/vertex/Tesselator;)Lcom/mojang/blaze3d/vertex/MeshData;", constant = @Constant(floatValue = 0.1f))
    //?} else {
    /^@ModifyConstant(method = "drawStars(Lcom/mojang/blaze3d/vertex/BufferBuilder;)Lcom/mojang/blaze3d/vertex/BufferBuilder$RenderedBuffer;", constant = @Constant(floatValue = 0.1f))
    ^///?}
    private float injected3(float constant) {
        return infinity$options().getStarSizeModifier();
    }

    @Unique
    private void infinity$renderEntireSky(PoseStack matrices, Matrix4f projectionMatrix, float tickDelta, Camera camera, boolean thickFog, Runnable fogCallback) {
        fogCallback.run();
        if (thickFog || doesMobEffectBlockSky(camera) || SkyRenderer.testCameraCancels(camera)) return;
        SkyRenderer renderer = new SkyRenderer(infinity$options(), minecraft, level,
                matrices, tickDelta, projectionMatrix,
                skyBuffer, starBuffer);
        renderer.render(fogCallback);
    }

    *///?} else {

    //?}
    //FIXME 1.21.8 - rendering broke

    @Unique
    private InfinityOptions infinity$options() {
        return InfinityOptions.ofClient(minecraft);
    }
}