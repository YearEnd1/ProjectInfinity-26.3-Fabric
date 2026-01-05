package net.lerariemann.infinity.options;

//FIXME 1.21.8 - rendering broke
//? if <1.21.2 {
/*import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.lerariemann.infinity.util.VersionMethods;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.material.FogType;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.awt.Color;

public record SkyRenderer(InfinityOptions options, Minecraft client, ClientLevel world,
                          PoseStack matrices, Tesselator tesselator, float tickDelta, Matrix4f projectionMatrix,
                          VertexBuffer lightSkyBuffer, VertexBuffer starsBuffer) {

    public SkyRenderer(InfinityOptions options, Minecraft client, ClientLevel world,
                       PoseStack matrices, float tickDelta, Matrix4f projectionMatrix,
                       VertexBuffer lightSkyBuffer, VertexBuffer starsBuffer) {
        this(options, client, world, matrices, Tesselator.getInstance(), tickDelta, projectionMatrix, lightSkyBuffer, starsBuffer);
    }

    public static boolean testCameraCancels(Camera camera) {
        FogType cameraSubmersionType = camera.getFluidInCamera();
        return  (cameraSubmersionType == FogType.POWDER_SNOW
                || cameraSubmersionType == FogType.LAVA);
    }

    public void render(Runnable fogCallback) {
        if (testAndRenderNonOverworldySkies()) return;
        setupOverworldySky();
        renderAllCelestialBodies(fogCallback);
        finish();
    }

    public boolean testAndRenderNonOverworldySkies() {
        if (client.level!=null && client.level.effects().skyType() == DimensionSpecialEffects.SkyType.END) {
            renderSkybox(VersionMethods.id("textures/environment/end_sky.png"), 16.0f, 40, 255);
            return true;
        }
        if (options.endSkyLike()) {
            handleSkyBackground();
            return true;
        }
        return client.level != null && client.level.effects().skyType() != DimensionSpecialEffects.SkyType.NORMAL;
    }

    public void setupOverworldySky() {
        FogRenderer.levelFogColor();
        RenderSystem.depthMask(false);
        handleSkyBackground();
        handleFog();
        matrices.pushPose();
    }
    public void handleSkyBackground() {
        String skyType = options.getSkyType();
        if (skyType.equals("rainbow")) {
            renderRainbowBackground();
        }
        else {
            Vec3 vec3d = this.world.getSkyColor(client.gameRenderer.getMainCamera().getPosition(), tickDelta);
            renderSingleColorBackground((float)vec3d.x, (float)vec3d.y, (float)vec3d.z, 1.0f);
        }
    }
    public void renderRainbowBackground() {
        float main = world.getTimeOfDay(tickDelta) * 2;
        int color = Color.getHSBColor(main - (int)main, 1.0f, 1.0f).getRGB();
        float f = Mth.clamp(Mth.cos(world.getTimeOfDay(tickDelta) * ((float)Math.PI * 2)) * 2.0f + 0.5f, 0.0f, 1.0f);
        renderSingleColorBackground(f * (float)(color >> 16 & 0xFF) / 255.0f, f * (float)(color >> 8 & 0xFF) / 255.0f, f * (float)(color & 0xFF) / 255.0f, 1.0f);
    }
    public void renderSingleColorBackground(float f, float g, float h, float a) {
        RenderSystem.setShaderColor(f, g, h, a);
        lightSkyBuffer.bind();
        lightSkyBuffer.drawWithShader(matrices.last().pose(), projectionMatrix, RenderSystem.getShader());
        VertexBuffer.unbind();
    }

    public void handleFog() {
        RenderSystem.enableBlend();
        float[] fs = this.world.effects().getSunriseColor(this.world.getTimeOfDay(tickDelta), tickDelta);
        if (fs != null && options().hasDawn()) handleSunriseFog(fs);
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
    }
    public void handleSunriseFog(float[] fs) {
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

        matrices.pushPose();
        matrices.mulPose(Axis.XP.rotationDegrees(90.0f));
        float i = Mth.sin(this.world.getSunAngle(tickDelta)) < 0.0f ? 180.0f : 0.0f;
        matrices.mulPose(Axis.ZP.rotationDegrees(i - options.getSolarTilt()));

        Matrix4f matrix4f = matrices.last().pose();
        FriendlyBuilder bufferBuilder = new FriendlyBuilder(tesselator, VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
        bufferBuilder.addVertex(matrix4f, 0.0f, 100.0f, 0.0f).setColor(fs[0], fs[1], fs[2], fs[3]).endVertex();
        for (int n = 0; n <= 16; ++n) {
            float o = (float)n * ((float)Math.PI * 2) / 16.0f;
            float p = Mth.sin(o);
            float q = Mth.cos(o);
            bufferBuilder.addVertex(matrix4f, p * 120.0f, q * 120.0f, -q * 40.0f * fs[3]).setColor(fs[0], fs[1], fs[2], 0.0f).endVertex();
        }
        BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
        matrices.popPose();
    }

    public record FriendlyVertexConsumer(VertexConsumer vc) {
        FriendlyVertexConsumer setColor(float r, float g, float b, float a) {
            //? if >1.21 {
            vc.setColor(r, g, b, a);
             //?} else {
            /^vc.color(r, g, b, a);
            ^///?}
            return this;
        }

        FriendlyVertexConsumer setUv(float u, float v) {
            //? if >1.21 {
            vc.setUv(u, v);
             //?} else {
            /^vc.uv(u, v);
            ^///?}
            return this;
        }

        void endVertex() {
            //? if <1.21 {
            /^vc.endVertex();
            ^///?}
        }
    }

    public static class FriendlyBuilder {
        BufferBuilder bufferBuilder;

        FriendlyBuilder(Tesselator tesselator, VertexFormat.Mode mode, VertexFormat format) {
            //? if >1.21 {
            bufferBuilder = tesselator.begin(mode, format);
             //?} else {
            /^bufferBuilder = tesselator.getBuilder();
            bufferBuilder.begin(mode, format);
            ^///?}
        }

        FriendlyVertexConsumer addVertex(Matrix4f m4f, float x, float y, float z) {
            return new FriendlyVertexConsumer(
                    //? if >1.21 {
                    bufferBuilder.addVertex(m4f, x, y, z)
                     //?} else {
                    /^bufferBuilder.vertex(m4f, x, y, z)
                    ^///?}
            );
        }

        //? if >1.21 {
        MeshData buildOrThrow() {
            return bufferBuilder.buildOrThrow();
        }
        //?} else {
        /^BufferBuilder.RenderedBuffer buildOrThrow() {
            return bufferBuilder.end();
        }
        ^///?}
    }

    public void renderAllCelestialBodies(Runnable fogCallback) {
        float rain_alpha = 1.0f - this.world.getRainLevel(tickDelta);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, rain_alpha);

        renderSun();
        for (int i = 0; i < options.getNumMoons(); i++) {
            renderMoon(i);
        }
        renderStars(fogCallback, rain_alpha);
    }

    public void finish() {
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
        matrices.popPose();
        RenderSystem.depthMask(true);
    }

    public void rotate_with_velocity(float v, float offset) {
        matrices.mulPose(Axis.XP.rotationDegrees((world.getTimeOfDay(tickDelta) + offset) * 360.0f * v));
    }

    public void renderSun() {
        renderSingleBody(
                options.getSolarSize(),
                options.getSolarTilt(),
                0,
                1,
                0,
                options.getSolarTint(),
                options.getSolarTexture(),
                true);
    }
    public void renderMoon(int i) {
        renderSingleBody(
                options.getLunarSize(i),
                options.getLunarTiltY(i),
                options.getLunarTiltZ(i),
                options.getLunarVelocity(i),
                options.getLunarOffset(i),
                options.getLunarTint(i),
                options.getLunarTexture(i),
                false);
    }

    public void renderSingleBody(float size, float tilt_y, float tilt_z, float velocity, float offset, Vector3f tint, ResourceLocation texture,
                                 boolean sun) {
        matrices.mulPose(Axis.YP.rotationDegrees(tilt_y));
        matrices.mulPose(Axis.ZP.rotationDegrees(tilt_z));
        rotate_with_velocity(velocity, offset);
        if (sun) renderSun(matrices.last().pose(), texture, size, 100.0f, tint);
        else renderMoon(matrices.last().pose(), texture, size, -100.0f, tint);
        rotate_with_velocity(-1 * velocity, offset);
        matrices.mulPose(Axis.ZP.rotationDegrees(-tilt_z));
        matrices.mulPose(Axis.YP.rotationDegrees(-tilt_y));
    }
    public void renderSun(Matrix4f matrix4f2, ResourceLocation texture, float k, float y, Vector3f tint) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShaderColor(tint.x, tint.y, tint.z, 1.0f);
        FriendlyBuilder bufferBuilder = new FriendlyBuilder(tesselator, VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferBuilder.addVertex(matrix4f2, -k, y, -k).setUv(0.0f, 0.0f).endVertex();
        bufferBuilder.addVertex(matrix4f2, k, y, -k).setUv(1.0f, 0.0f).endVertex();
        bufferBuilder.addVertex(matrix4f2, k, y, k).setUv(1.0f, 1.0f).endVertex();
        bufferBuilder.addVertex(matrix4f2, -k, y, k).setUv(0.0f, 1.0f).endVertex();
        BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
    }
    public void renderMoon(Matrix4f matrix4f2, ResourceLocation texture, float k, float y, Vector3f tint) {
        float t, q, p, o;
        if (!options.isMoonCustom()) {
            int moon_phase = world.getMoonPhase();
            int s = moon_phase % 4;
            int m = moon_phase / 4 % 2;
            t = (float)(s) / 4.0f;
            o = (float)(m) / 2.0f;
            p = (float)(s + 1) / 4.0f;
            q = (float)(m + 1) / 2.0f;
        }
        else {
            t = q = 1.0f;
            p = o = 0.0f;
        }
        renderMoon(matrix4f2, texture, k, y, tint, t, q, p, o);
    }
    public void renderMoon(Matrix4f matrix4f2, ResourceLocation texture, float k, float y, Vector3f tint, float t, float q, float p, float o) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShaderColor(tint.x, tint.y, tint.z, 1.0f);
        FriendlyBuilder bufferBuilder = new FriendlyBuilder(tesselator, VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferBuilder.addVertex(matrix4f2, -k, y, k).setUv(p, q).endVertex();
        bufferBuilder.addVertex(matrix4f2, k, y, k).setUv(t, q).endVertex();
        bufferBuilder.addVertex(matrix4f2, k, y, -k).setUv(t, o).endVertex();
        bufferBuilder.addVertex(matrix4f2, -k, y, -k).setUv(p, o).endVertex();
        BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
    }

    public void renderStars(Runnable fogCallback, float rain_alpha) {
        renderStars(options.getStellarTiltY(),
                options.getStellarTiltZ(),
                options.getStellarVelocity(),
                0,
                fogCallback, rain_alpha);
    }
    public void renderStars(float tilt_y, float tilt_z, float velocity, float offset,
                            Runnable fogCallback, float rain_alpha) {
        matrices.mulPose(Axis.YP.rotationDegrees(tilt_y));
        matrices.mulPose(Axis.ZP.rotationDegrees(tilt_z));
        rotate_with_velocity(velocity, offset);
        renderStars(matrices.last().pose(), projectionMatrix, fogCallback, rain_alpha);
        rotate_with_velocity(-1 * velocity, offset);
        matrices.mulPose(Axis.ZP.rotationDegrees(-tilt_z));
        matrices.mulPose(Axis.YP.rotationDegrees(-tilt_y));
    }
    public void renderStars(Matrix4f matrix4f2, Matrix4f projectionMatrix, Runnable fogCallback, float rain_alpha) {
        float u = getStarBrightness(tickDelta) * rain_alpha;
        Vector3f color = options.getStellarColor();
        if (u > 0.0f) {
            RenderSystem.setShaderColor(u*color.x, u*color.y, u*color.z, u);
            FogRenderer.setupNoFog();
            starsBuffer.bind();
            starsBuffer.drawWithShader(matrix4f2, projectionMatrix, GameRenderer.getPositionShader());
            VertexBuffer.unbind();
            fogCallback.run();
        }
    }
    public float getStarBrightness(float tickDelta) {
        float f = world.getTimeOfDay(tickDelta);
        float g = 1.0F - (Mth.cos(f * (float) (Math.PI * 2)) * 2.0F + 0.25F);
        g = Mth.clamp(g, 0.0f, 1.0f);
        float day = options.getDayStarBrightness();
        float night = options.getNightStarBrightness();
        return day + g*g*(night-day);
    }

    public void renderSkybox(ResourceLocation texture, float copies, int brightness, int alpha) {
        renderSkybox(texture, copies, brightness, brightness, brightness, alpha);
    }
    public void renderSkybox(ResourceLocation texture, float copies, int r, int g, int b, int alpha) {
        RenderSystem.enableBlend();
        RenderSystem.depthMask(false);
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderTexture(0, texture);
        Tesselator tesselator = Tesselator.getInstance();

        for (int i = 0; i < 6; ++i) {
            matrices.pushPose();
            if (i == 1) {
                matrices.mulPose(Axis.XP.rotationDegrees(90.0f));
            }
            if (i == 2) {
                matrices.mulPose(Axis.XP.rotationDegrees(-90.0f));
            }
            if (i == 3) {
                matrices.mulPose(Axis.XP.rotationDegrees(180.0f));
            }
            if (i == 4) {
                matrices.mulPose(Axis.ZP.rotationDegrees(90.0f));
            }
            if (i == 5) {
                matrices.mulPose(Axis.ZP.rotationDegrees(-90.0f));
            }

            Matrix4f matrix4f = matrices.last().pose();
            FriendlyBuilder bufferBuilder = new FriendlyBuilder(tesselator, VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
            bufferBuilder.addVertex(matrix4f, -100.0f, -100.0f, -100.0f).setUv(0.0f, 0.0f).setColor(r, g, b, alpha).endVertex();
            bufferBuilder.addVertex(matrix4f, -100.0f, -100.0f, 100.0f).setUv(0.0f, copies).setColor(r, g, b, alpha).endVertex();
            bufferBuilder.addVertex(matrix4f, 100.0f, -100.0f, 100.0f).setUv(copies, copies).setColor(r, g, b, alpha).endVertex();
            bufferBuilder.addVertex(matrix4f, 100.0f, -100.0f, -100.0f).setUv(copies, 0.0f).setColor(r, g, b, alpha).endVertex();
            BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
            matrices.popPose();
        }
        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
    }
}

*///?} else {
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.joml.*;

import java.awt.*;
import java.lang.Math;
import java.util.OptionalDouble;
import java.util.OptionalInt;

public class SkyRenderer implements AutoCloseable {
    public static final ResourceLocation END_SKY_LOCATION = ResourceLocation.withDefaultNamespace("textures/environment/end_sky.png");
    private final GpuBuffer starBuffer;
    private final RenderSystem.AutoStorageIndexBuffer starIndices;
    private final GpuBuffer topSkyBuffer;
    private final GpuBuffer bottomSkyBuffer;
    private final GpuBuffer endSkyBuffer;
    private int starIndexCount;
    InfinityOptions options;
    ClientLevel level;
    MultiBufferSource.BufferSource bufferSource;
    PoseStack poseStack;
    float timeOfDay;


    public SkyRenderer(InfinityOptions options, ClientLevel level) {
        this.options = options;
        this.level = level;
        this.starIndices = RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS);
        this.starBuffer = (new SafeBufferBuilder.Stars(this, options)).build();
        this.endSkyBuffer = (new SafeBufferBuilder.End()).build();
        this.topSkyBuffer = (new SafeBufferBuilder.Top()).build();
        this.bottomSkyBuffer = (new SafeBufferBuilder.Bottom()).build();
    }

    public void renderAll(RenderBuffers renderBuffers, float partialTick) {
        if (level.effects().skyType() == DimensionSpecialEffects.SkyType.END || options.endSkyLike()) {
            renderEndSky();
        } else {
            poseStack = new PoseStack();
            float sunAngle = this.level.getSunAngle(partialTick);
            timeOfDay = this.level.getTimeOfDay(partialTick);

            float rainLevel = 1.0F - this.level.getRainLevel(partialTick);
            float starBrightness = getStarBrightness(partialTick) * rainLevel;

            int dawnColor = level.effects().getSunriseOrSunsetColor(timeOfDay);

            int skyColor = (options.getSkyType().equals("rainbow"))
                    ? Color.getHSBColor((2*timeOfDay) - (int)(2*timeOfDay), 1.0f, 1.0f).getRGB()
                    : this.level.getSkyColor(Minecraft.getInstance().gameRenderer.getMainCamera().getPosition(), partialTick);
            renderSkyDisc(skyColor);

            bufferSource = renderBuffers.bufferSource();
            if (level.effects().isSunriseOrSunset(timeOfDay)) {
                renderSunriseAndSunset(sunAngle, dawnColor);
            }

            renderSunMoonAndStars(rainLevel, starBrightness);
            bufferSource.endBatch();
        }
    }

    public float getStarBrightness(float partialTick) {
        float f = level.getTimeOfDay(partialTick);
        float g = 1.0F - (Mth.cos(f * (float) (Math.PI * 2)) * 2.0F + 0.25F);
        g = Mth.clamp(g, 0.0f, 1.0f);
        float day = options.getDayStarBrightness();
        float night = options.getNightStarBrightness();
        return day + g*g*(night-day);
    }

    public void renderSunMoonAndStars(float rainLevel, float starBrightness) {
        poseStack.pushPose();
        this.renderSun(rainLevel);
        for (int i = 0; i < options.getNumMoons(); i++) {
            this.renderMoon(i, rainLevel);
        }
        bufferSource.endBatch();
        if (starBrightness > 0.0F) {
            this.renderStars(starBrightness);
        }
        poseStack.popPose();
    }

    public void renderSun(float alpha) {
        renderSingleBody(
                options.getSolarSize(),
                options.getSolarTilt(),
                0,
                1,
                0,
                ofTintAlpha(options.getSolarTint(), alpha),
                options.getSolarTexture(),
                true);
    }
    public void renderMoon(int i, float alpha) {
        renderSingleBody(
                options.getLunarSize(i),
                options.getLunarTiltY(i),
                options.getLunarTiltZ(i),
                options.getLunarVelocity(i),
                options.getLunarOffset(i),
                ofTintAlpha(options.getLunarTint(i), alpha),
                options.getLunarTexture(i),
                false);
    }

    public void renderSingleBody(float size, float tilt_y, float tilt_z, float velocity, float offset,
                                 int tint, ResourceLocation texture, boolean sun) {
        poseStack.mulPose(Axis.YP.rotationDegrees(tilt_y));
        poseStack.mulPose(Axis.ZP.rotationDegrees(tilt_z));
        rotateWithVelocity(velocity, offset);
        if (sun) renderSun(texture, size, 100.0f, tint);
        else renderMoon(texture, size, -100.0f, tint);
        rotateWithVelocity(-1 * velocity, offset);
        poseStack.mulPose(Axis.ZP.rotationDegrees(-tilt_z));
        poseStack.mulPose(Axis.YP.rotationDegrees(-tilt_y));
    }
    public void rotateWithVelocity(float velocity, float offset) {
        poseStack.mulPose(Axis.XP.rotationDegrees((timeOfDay + offset) * 360.0f * velocity));
    }
    int ofTintAlpha(Vector3f tint, float alpha) {
        return ARGB.colorFromFloat(alpha, tint.x, tint.y, tint.z);
    }

    public void renderSun(ResourceLocation texture, float size, float y, int tint) {
        VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.celestial(texture));
        Matrix4f matrix4f = poseStack.last().pose();
        vertexconsumer.addVertex(matrix4f, -size, y, -size).setUv(0.0F, 0.0F).setColor(tint);
        vertexconsumer.addVertex(matrix4f, size, y, -size).setUv(1.0F, 0.0F).setColor(tint);
        vertexconsumer.addVertex(matrix4f, size, y, size).setUv(1.0F, 1.0F).setColor(tint);
        vertexconsumer.addVertex(matrix4f, -size, y, size).setUv(0.0F, 1.0F).setColor(tint);
    }

    public void renderMoon(ResourceLocation texture, float size, float y, int tint) {
        float t, q, p, o;
        if (isMoonNormal(texture)) {
            int phase = this.level.getMoonPhase();
            int s = phase % 4;
            int m = phase / 4 % 2;
            t = (float)(s) / 4.0f;
            o = (float)(m) / 2.0f;
            p = (float)(s + 1) / 4.0f;
            q = (float)(m + 1) / 2.0f;
        }
        else {
            t = q = 1.0f;
            p = o = 0.0f;
        }

        VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.celestial(texture));
        Matrix4f matrix4f = poseStack.last().pose();
        vertexconsumer.addVertex(matrix4f, -size, y, size).setUv(p, q).setColor(tint);
        vertexconsumer.addVertex(matrix4f, size, y, size).setUv(t, q).setColor(tint);
        vertexconsumer.addVertex(matrix4f, size, y, -size).setUv(t, o).setColor(tint);
        vertexconsumer.addVertex(matrix4f, -size, y, -size).setUv(p, o).setColor(tint);
    }
    boolean isMoonNormal(ResourceLocation texture) {
        return texture.getPath().equals("textures/environment/moon_phases.png");
    }


    public void renderStars(float starBrightness) {
        renderStars(options.getStellarTiltY(),
                options.getStellarTiltZ(),
                options.getStellarVelocity(),
                0,
                starBrightness);
    }
    public void renderStars(float tilt_y, float tilt_z, float velocity, float offset,
                            float starBrightness) {
        poseStack.mulPose(Axis.YP.rotationDegrees(tilt_y));
        poseStack.mulPose(Axis.ZP.rotationDegrees(tilt_z));
        rotateWithVelocity(velocity, offset);
        renderStarsInner(starBrightness);
        rotateWithVelocity(-1 * velocity, offset);
        poseStack.mulPose(Axis.ZP.rotationDegrees(-tilt_z));
        poseStack.mulPose(Axis.YP.rotationDegrees(-tilt_y));
    }

    private void renderStarsInner(float starBrightness) {
        Vector3f color = options.getStellarColor();
        Matrix4fStack matrix4fstack = RenderSystem.getModelViewStack();
        matrix4fstack.pushMatrix();
        matrix4fstack.mul(poseStack.last().pose());
        RenderPipeline renderpipeline = RenderPipelines.STARS;
        GpuTextureView gputextureview = Minecraft.getInstance().getMainRenderTarget().getColorTextureView();
        GpuTextureView gputextureview1 = Minecraft.getInstance().getMainRenderTarget().getDepthTextureView();
        GpuBuffer gpubuffer = this.starIndices.getBuffer(this.starIndexCount);
        GpuBufferSlice gpubufferslice = RenderSystem.getDynamicUniforms().writeTransform(matrix4fstack,
                new Vector4f(starBrightness*color.x, starBrightness*color.y, starBrightness*color.z, starBrightness),
                new Vector3f(), new Matrix4f(), 0.0F);
        RenderPass renderpass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "Stars",
                gputextureview, OptionalInt.empty(), gputextureview1, OptionalDouble.empty());

        try {
            renderpass.setPipeline(renderpipeline);
            RenderSystem.bindDefaultUniforms(renderpass);
            renderpass.setUniform("DynamicTransforms", gpubufferslice);
            renderpass.setVertexBuffer(0, this.starBuffer);
            renderpass.setIndexBuffer(gpubuffer, this.starIndices.type());
            renderpass.drawIndexed(0, 0, this.starIndexCount, 1);
        } catch (Throwable var13) {
            if (renderpass != null) {
                try {
                    renderpass.close();
                } catch (Throwable var12) {
                    var13.addSuppressed(var12);
                }
            }

            throw var13;
        }

        if (renderpass != null) {
            renderpass.close();
        }

        matrix4fstack.popMatrix();
    }

    public void renderSunriseAndSunset(float sunAngle, int color) {
        poseStack.pushPose();
        float tilt = -1.0F * options.getSolarTilt();
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
        float f = Mth.sin(sunAngle) < 0.0F ? 180.0F : 0.0F;
        poseStack.mulPose(Axis.ZP.rotationDegrees(f));
        poseStack.mulPose(Axis.ZP.rotationDegrees(tilt));

        Matrix4f matrix4f = poseStack.last().pose();
        VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.sunriseSunset());
        float f1 = ARGB.alphaFloat(color);
        vertexconsumer.addVertex(matrix4f, 0.0F, 100.0F, 0.0F).setColor(color);
        int i = ARGB.transparent(color);

        for(int k = 0; k <= 16; ++k) {
            float f2 = (float)k * 6.2831855F / 16.0F;
            float f3 = Mth.sin(f2);
            float f4 = Mth.cos(f2);
            vertexconsumer.addVertex(matrix4f, f3 * 120.0F, f4 * 120.0F, -f4 * 40.0F * f1).setColor(i);
        }

        poseStack.popPose();
    }

    public void renderSkyDisc(int skyColor) {
        GpuBufferSlice gpubufferslice = RenderSystem.getDynamicUniforms()
                .writeTransform(RenderSystem.getModelViewMatrix(),
                        new Vector4f(ARGB.redFloat(skyColor), ARGB.greenFloat(skyColor), ARGB.blueFloat(skyColor), 1.0F),
                        new Vector3f(), new Matrix4f(), 0.0F);
        GpuTextureView gputextureview = Minecraft.getInstance().getMainRenderTarget().getColorTextureView();
        GpuTextureView gputextureview1 = Minecraft.getInstance().getMainRenderTarget().getDepthTextureView();
        RenderPass renderpass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "Sky disc",
                gputextureview, OptionalInt.empty(), gputextureview1, OptionalDouble.empty());
        try {
            renderpass.setPipeline(RenderPipelines.SKY);
            RenderSystem.bindDefaultUniforms(renderpass);
            renderpass.setUniform("DynamicTransforms", gpubufferslice);
            renderpass.setVertexBuffer(0, this.topSkyBuffer);
            renderpass.draw(0, 10);
        } catch (Throwable var11) {
            if (renderpass != null) {
                try {
                    renderpass.close();
                } catch (Throwable var10) {
                    var11.addSuppressed(var10);
                }
            }
            throw var11;
        }
        if (renderpass != null) {
            renderpass.close();
        }
    }

    public void renderEndSky() {
        TextureManager texturemanager = Minecraft.getInstance().getTextureManager();
        AbstractTexture abstracttexture = texturemanager.getTexture(END_SKY_LOCATION);
        abstracttexture.setUseMipmaps(false);
        RenderSystem.AutoStorageIndexBuffer rendersystem$autostorageindexbuffer = RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS);
        GpuBuffer gpubuffer = rendersystem$autostorageindexbuffer.getBuffer(36);
        GpuTextureView gputextureview = Minecraft.getInstance().getMainRenderTarget().getColorTextureView();
        GpuTextureView gputextureview1 = Minecraft.getInstance().getMainRenderTarget().getDepthTextureView();
        GpuBufferSlice gpubufferslice = RenderSystem.getDynamicUniforms().writeTransform(RenderSystem.getModelViewMatrix(), new Vector4f(1.0F, 1.0F, 1.0F, 1.0F), new Vector3f(), new Matrix4f(), 0.0F);
        RenderPass renderpass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "End sky",
                gputextureview, OptionalInt.empty(), gputextureview1, OptionalDouble.empty());

        try {
            renderpass.setPipeline(RenderPipelines.END_SKY);
            RenderSystem.bindDefaultUniforms(renderpass);
            renderpass.setUniform("DynamicTransforms", gpubufferslice);
            renderpass.bindSampler("Sampler0", abstracttexture.getTextureView());
            renderpass.setVertexBuffer(0, this.endSkyBuffer);
            renderpass.setIndexBuffer(gpubuffer, rendersystem$autostorageindexbuffer.type());
            renderpass.drawIndexed(0, 0, 36, 1);
        } catch (Throwable var12) {
            if (renderpass != null) {
                try {
                    renderpass.close();
                } catch (Throwable var11) {
                    var12.addSuppressed(var11);
                }
            }

            throw var12;
        }

        if (renderpass != null) {
            renderpass.close();
        }

    }

    @Override
    public void close() {
        this.starBuffer.close();
        this.topSkyBuffer.close();
        this.bottomSkyBuffer.close();
        this.endSkyBuffer.close();
    }

    public static abstract class SafeBufferBuilder {
        ByteBufferBuilder bytebufferbuilder;
        BufferBuilder bufferbuilder;
        MeshData meshdata;
        GpuBuffer gpubuffer;

        abstract ByteBufferBuilder createByteBuffer();
        abstract BufferBuilder createBuffer();
        abstract void populate();
        abstract String manifest();
        abstract int maniint();
        void runSpecial() {
        }

        GpuBuffer build() {
            bytebufferbuilder = createByteBuffer();
            try {
                bufferbuilder = createBuffer();
                populate();
                meshdata = bufferbuilder.buildOrThrow();

                try {
                    runSpecial();
                    gpubuffer = RenderSystem.getDevice().createBuffer(this::manifest, maniint(), meshdata.vertexBuffer());
                } catch (Throwable var8) {
                    if (meshdata != null) {
                        try {
                            meshdata.close();
                        } catch (Throwable var7) {
                            var8.addSuppressed(var7);
                        }
                    }
                    throw var8;
                }
                if (meshdata != null) {
                    meshdata.close();
                }
            } catch (Throwable var9) {
                if (bytebufferbuilder != null) {
                    try {
                        bytebufferbuilder.close();
                    } catch (Throwable var6) {
                        var9.addSuppressed(var6);
                    }
                }
                throw var9;
            }
            if (bytebufferbuilder != null) {
                bytebufferbuilder.close();
            }
            return gpubuffer;
        }

        public static class Stars extends SafeBufferBuilder {
            int numStars;
            float sizeBase;
            float sizeMod;
            SkyRenderer parent;
            public Stars(SkyRenderer parent, InfinityOptions options) {
                this.numStars = options.getNumStars();
                this.sizeBase = options.getStarSizeBase();
                this.sizeMod = options.getStarSizeModifier();
                this.parent = parent;
            }

            @Override
            void runSpecial() {
                parent.starIndexCount = meshdata.drawState().indexCount();
            }
            @Override
            ByteBufferBuilder createByteBuffer() {
                return ByteBufferBuilder.exactlySized(DefaultVertexFormat.POSITION.getVertexSize() * 1500 * 4);
            }
            @Override
            BufferBuilder createBuffer() {
                return new BufferBuilder(bytebufferbuilder, VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
            }
            @Override
            void populate() {
                RandomSource randomsource = RandomSource.create(10842L);
                for(int i = 0; i < numStars; ++i) {
                    float f1 = randomsource.nextFloat() * 2.0F - 1.0F;
                    float f2 = randomsource.nextFloat() * 2.0F - 1.0F;
                    float f3 = randomsource.nextFloat() * 2.0F - 1.0F;
                    float f4 = sizeBase + randomsource.nextFloat() * sizeMod;
                    float f5 = Mth.lengthSquared(f1, f2, f3);
                    if (!(f5 <= 0.010000001F) && !(f5 >= 1.0F)) {
                        Vector3f vector3f = (new Vector3f(f1, f2, f3)).normalize(100.0F);
                        float f6 = (float)(randomsource.nextDouble() * 3.1415927410125732 * 2.0);
                        Matrix3f matrix3f = (new Matrix3f()).rotateTowards((new Vector3f(vector3f)).negate(), new Vector3f(0.0F, 1.0F, 0.0F)).rotateZ(-f6);
                        bufferbuilder.addVertex((new Vector3f(f4, -f4, 0.0F)).mul(matrix3f).add(vector3f));
                        bufferbuilder.addVertex((new Vector3f(f4, f4, 0.0F)).mul(matrix3f).add(vector3f));
                        bufferbuilder.addVertex((new Vector3f(-f4, f4, 0.0F)).mul(matrix3f).add(vector3f));
                        bufferbuilder.addVertex((new Vector3f(-f4, -f4, 0.0F)).mul(matrix3f).add(vector3f));
                    }
                }
            }
            @Override
            String manifest() {
                return "Stars vertex buffer";
            }

            @Override
            int maniint() {
                return 40;
            }
        }

        public static class End extends SafeBufferBuilder {
            @Override
            ByteBufferBuilder createByteBuffer() {
                return ByteBufferBuilder.exactlySized(24 * DefaultVertexFormat.POSITION_TEX_COLOR.getVertexSize());
            }
            @Override
            BufferBuilder createBuffer() {
                return new BufferBuilder(bytebufferbuilder, VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
            }
            @Override
            void populate() {
                for(int i = 0; i < 6; ++i) {
                    Matrix4f matrix4f = new Matrix4f();
                    switch (i) {
                        case 1 -> matrix4f.rotationX(1.5707964F);
                        case 2 -> matrix4f.rotationX(-1.5707964F);
                        case 3 -> matrix4f.rotationX(3.1415927F);
                        case 4 -> matrix4f.rotationZ(1.5707964F);
                        case 5 -> matrix4f.rotationZ(-1.5707964F);
                    }

                    bufferbuilder.addVertex(matrix4f, -100.0F, -100.0F, -100.0F).setUv(0.0F, 0.0F).setColor(-14145496);
                    bufferbuilder.addVertex(matrix4f, -100.0F, -100.0F, 100.0F).setUv(0.0F, 16.0F).setColor(-14145496);
                    bufferbuilder.addVertex(matrix4f, 100.0F, -100.0F, 100.0F).setUv(16.0F, 16.0F).setColor(-14145496);
                    bufferbuilder.addVertex(matrix4f, 100.0F, -100.0F, -100.0F).setUv(16.0F, 0.0F).setColor(-14145496);
                }
            }
            @Override
            String manifest() {
                return "End sky vertex buffer";
            }

            @Override
            int maniint() {
                return 40;
            }
        }

        public abstract static class TopBottom extends SafeBufferBuilder {
            @Override
            ByteBufferBuilder createByteBuffer() {
                return ByteBufferBuilder.exactlySized(10 * DefaultVertexFormat.POSITION.getVertexSize());
            }
            @Override
            BufferBuilder createBuffer() {
                return new BufferBuilder(bytebufferbuilder, VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION);
            }
            abstract float y();
            @Override
            void populate() {
                float y = y();
                float f = Math.signum(y) * 512.0F;
                bufferbuilder.addVertex(0.0F, y, 0.0F);
                for(int i = -180; i <= 180; i += 45) {
                    bufferbuilder.addVertex(f * Mth.cos((float)i * 0.017453292F), y, 512.0F * Mth.sin((float)i * 0.017453292F));
                }
            }
            @Override
            int maniint() {
                return 32;
            }
        }
        public static class Top extends TopBottom {
            @Override
            float y() {
                return 16.0F;
            }
            @Override
            String manifest() {
                return "Top sky vertex buffer";
            }
        }
        public static class Bottom extends TopBottom {
            @Override
            float y() {
                return -16.0F;
            }
            @Override
            String manifest() {
                return "Bottom sky vertex buffer";
            }
        }
    }
}
//?}