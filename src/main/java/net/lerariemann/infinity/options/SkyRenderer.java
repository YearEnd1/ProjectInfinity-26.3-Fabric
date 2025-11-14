package net.lerariemann.infinity.options;

import com.mojang.blaze3d.platform.GlStateManager;
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
        bufferBuilder.addVertex(matrix4f, 0.0f, 100.0f, 0.0f).setColor(fs[0], fs[1], fs[2], fs[3]);
        for (int n = 0; n <= 16; ++n) {
            float o = (float)n * ((float)Math.PI * 2) / 16.0f;
            float p = Mth.sin(o);
            float q = Mth.cos(o);
            bufferBuilder.addVertex(matrix4f, p * 120.0f, q * 120.0f, -q * 40.0f * fs[3]).setColor(fs[0], fs[1], fs[2], 0.0f);
        }
        BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
        matrices.popPose();
    }

    public record FriendlyVertexConsumer(VertexConsumer vc) {
        void setColor(float r, float g, float b, float a) {
            //? if >1.21 {
            /*vc.setColor(r, g, b, a);
             *///?} else {
            vc.color(r, g, b, a);
            //?}
        }

        FriendlyVertexConsumer setUv(float u, float v) {
            //? if >1.21 {
            /*vc.setUv(u, v);
             *///?} else {
            vc.uv(u, v);
            //?}
            return this;
        }
    }

    public static class FriendlyBuilder {
        BufferBuilder bufferBuilder;

        FriendlyBuilder(Tesselator tesselator, VertexFormat.Mode mode, VertexFormat format) {
            //? if >1.21 {
            /*bufferBuilder = tesselator.begin(mode, format);
             *///?} else {
            bufferBuilder = tesselator.getBuilder();
            bufferBuilder.begin(mode, format);
            //?}
        }

        FriendlyVertexConsumer addVertex(Matrix4f m4f, float x, float y, float z) {
            return new FriendlyVertexConsumer(
            //? if >1.21 {
            /*bufferBuilder.addVertex(m4f, x, y, z)
             *///?} else {
            bufferBuilder.vertex(m4f, x, y, z)
            //?}
            );
        }

        //? if >1.21 {
        /*MeshData buildOrThrow() {
            return bufferBuilder.buildOrThrow();
        }
        *///?} else {
        BufferBuilder.RenderedBuffer buildOrThrow() {
            return bufferBuilder.end();
        }
        //?}
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
        bufferBuilder.addVertex(matrix4f2, -k, y, -k).setUv(0.0f, 0.0f);
        bufferBuilder.addVertex(matrix4f2, k, y, -k).setUv(1.0f, 0.0f);
        bufferBuilder.addVertex(matrix4f2, k, y, k).setUv(1.0f, 1.0f);
        bufferBuilder.addVertex(matrix4f2, -k, y, k).setUv(0.0f, 1.0f);
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
        bufferBuilder.addVertex(matrix4f2, -k, y, k).setUv(p, q);
        bufferBuilder.addVertex(matrix4f2, k, y, k).setUv(t, q);
        bufferBuilder.addVertex(matrix4f2, k, y, -k).setUv(t, o);
        bufferBuilder.addVertex(matrix4f2, -k, y, -k).setUv(p, o);
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
            bufferBuilder.addVertex(matrix4f, -100.0f, -100.0f, -100.0f).setUv(0.0f, 0.0f).setColor(r, g, b, alpha);
            bufferBuilder.addVertex(matrix4f, -100.0f, -100.0f, 100.0f).setUv(0.0f, copies).setColor(r, g, b, alpha);
            bufferBuilder.addVertex(matrix4f, 100.0f, -100.0f, 100.0f).setUv(copies, copies).setColor(r, g, b, alpha);
            bufferBuilder.addVertex(matrix4f, 100.0f, -100.0f, -100.0f).setUv(copies, 0.0f).setColor(r, g, b, alpha);
            BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
            matrices.popPose();
        }
        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
    }
}
