package net.lerariemann.infinity.mixin.options;

//? if <1.21.2 {
/*import com.mojang.blaze3d.vertex.VertexBuffer;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import net.lerariemann.infinity.util.InfinityMethods;
*///?} else {
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.framegraph.FrameGraphBuilder;
import com.mojang.blaze3d.framegraph.FramePass;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.world.level.material.FogType;
//?}
import com.mojang.blaze3d.vertex.PoseStack;
import net.lerariemann.infinity.access.WorldRendererAccess;
import net.lerariemann.infinity.options.InfinityOptions;
import net.lerariemann.infinity.options.SkyRenderer;
import net.minecraft.client.Camera;
//? if >1.21 {
import net.minecraft.client.renderer.*;
//?} else {
/*import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
*///?}
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
//? if >1.21.2 && neoforge {
/*import net.neoforged.neoforge.client.IRenderableSection;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.common.NeoForge;
*///?}
import net.minecraft.client.renderer.LevelRenderer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(LevelRenderer.class)
public abstract class WorldRendererMixin implements WorldRendererAccess {
    @Shadow private ClientLevel level;
    @Final @Shadow private Minecraft minecraft;

    @Shadow protected abstract boolean doesMobEffectBlockSky(Camera camera);


    @Override
    public void infinity$setNeedsStars(boolean b) {
        //? if <1.21.2 {
        /*createStars();
        *///?} else {
        infinity$sky = new SkyRenderer(infinity$options(), this.level);
        //?}
    }

    //? if <1.21.2 {
    /*@Final
    @Shadow private VertexBuffer skyBuffer;
    @Shadow private VertexBuffer starBuffer;

    @Shadow protected abstract void createStars();

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

    *///?}

    @Unique
    private InfinityOptions infinity$options() {
        return InfinityOptions.ofClient(minecraft);
    }

    //? if >1.21.2 {
    @Shadow @Final private LevelTargetBundle targets;
    @Shadow private int ticks;
    @Shadow @Final private RenderBuffers renderBuffers;
    //?}
    //? if >1.21.2 && neoforge {
    /*@Shadow public abstract Frustum getFrustum();
    @Shadow public abstract Iterable<? extends IRenderableSection> getRenderableSections();
    *///?}
    //? if >1.21.2 {
    @Unique SkyRenderer infinity$sky;
    @Inject(method = "<init>", at = @At("TAIL"))
    void customSky(Minecraft minecraft, EntityRenderDispatcher entityRenderDispatcher, BlockEntityRenderDispatcher blockEntityRenderDispatcher, RenderBuffers renderBuffers, CallbackInfo ci) {
        if (this.level != null) infinity$sky = new SkyRenderer(infinity$options(), this.level);
    }
    //?}
    //? if >1.21.2 && neoforge {
    /*@Inject(method = "addSkyPass(Lcom/mojang/blaze3d/framegraph/FrameGraphBuilder;Lnet/minecraft/client/Camera;FLcom/mojang/blaze3d/buffers/GpuBufferSlice;Lorg/joml/Matrix4f;)V", at = @At("HEAD"), cancellable = true)
    void inj(FrameGraphBuilder frameGraphBuilder, Camera camera, float partialTick, GpuBufferSlice shaderFog, Matrix4f modelViewMatrix, CallbackInfo ci) {
    *///?}
    //? if >1.21.2 && fabric {
    @Inject(method = "Lnet/minecraft/client/renderer/LevelRenderer;addSkyPass(Lcom/mojang/blaze3d/framegraph/FrameGraphBuilder;Lnet/minecraft/client/Camera;FLcom/mojang/blaze3d/buffers/GpuBufferSlice;)V", at = @At("HEAD"), cancellable = true)
    void inj(FrameGraphBuilder frameGraphBuilder, Camera camera, float partialTick, GpuBufferSlice shaderFog, CallbackInfo ci) {
    //?}
    //? if >1.21.2 {
    
        InfinityOptions options = infinity$options();
        if (this.level != null && infinity$sky == null) {
            infinity$sky = new SkyRenderer(options, this.level);
        }
        if (!options.isEmpty() && infinity$sky != null) {
            FogType fogtype = camera.getFluidInCamera();
            if (fogtype != FogType.POWDER_SNOW && fogtype != FogType.LAVA && !doesMobEffectBlockSky(camera)) {
                DimensionSpecialEffects.SkyType type = this.level.effects().skyType();
                if (type != DimensionSpecialEffects.SkyType.NONE) {
                    FramePass framepass = frameGraphBuilder.addPass("sky");
                    targets.main = framepass.readsAndWrites(this.targets.main);
                    framepass.executes(() -> {
    //?}
//? if >1.21.2 && neoforge {
    
                        /*if (!this.level.effects().renderSky(this.level, ticks, partialTick, modelViewMatrix, camera,
                                () -> RenderSystem.setShaderFog(shaderFog))) {
    *///?}
//? if >1.21.2 {
    
                            RenderSystem.setShaderFog(shaderFog);
                            infinity$sky.renderAll(renderBuffers, partialTick);
    //?}
//? if >1.21.2 && neoforge {
    
                            /*Profiler.get().push("neoforge_render_after_sky");
                            NeoForge.EVENT_BUS.post(new RenderLevelStageEvent.AfterSky(this.level, (LevelRenderer)(Object)this, (PoseStack)null, modelViewMatrix, this.ticks, this.minecraft.getDeltaTracker(), camera, this.getFrustum(), this.getRenderableSections()));
                            Profiler.get().pop();
                        }
    *///?}
//? if >1.21.2 {
    
                    });
                }
            }
            ci.cancel();
        }
    }
    //?}

    //jukeboxes begin
    //? if <1.21 {
    /*@Shadow @Final private Map<BlockPos, SoundInstance> playingRecords;
    @Shadow protected abstract void notifyNearbyEntities(Level level, BlockPos pos, boolean playing);

    public void infinity$playMusic(BlockPos pos, SoundEvent sound, String desc) {
        SoundInstance soundinstance = playingRecords.get(pos);
        if (soundinstance != null) {
            this.minecraft.getSoundManager().stop(soundinstance);
            this.playingRecords.remove(pos);
        }

        if (sound != null) {
            this.minecraft.gui.setNowPlaying(Component.translatable(desc));
            SoundInstance simplesoundinstance = SimpleSoundInstance.forRecord(sound, Vec3.atCenterOf(pos));
            this.playingRecords.put(pos, simplesoundinstance);
            this.minecraft.getSoundManager().play(simplesoundinstance);
        }

        this.notifyNearbyEntities(this.level, pos, sound != null);
    };
    *///?}
}