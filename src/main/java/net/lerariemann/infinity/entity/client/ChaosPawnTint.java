package net.lerariemann.infinity.entity.client;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.lerariemann.infinity.entity.custom.ChaosPawn;
import net.lerariemann.infinity.entity.custom.TintableEntity;
import net.lerariemann.infinity.util.InfinityMethods;
import net.lerariemann.infinity.util.VersionMethods;
import net.lerariemann.infinity.util.core.NbtUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
//? if >1.21.9 {
/*import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
*///?}

import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
//? if <1.21.2 {
/*import net.minecraft.util.FastColor;
*///?}
import com.mojang.blaze3d.vertex.PoseStack;

import java.awt.Color;

public class ChaosPawnTint extends
        //? if <1.21.2 {
        /*RenderLayer<ChaosPawn, HumanoidModel<ChaosPawn>>
        *///?} else {
        RenderLayer<ChaosPawnRenderState, HumanoidModel<ChaosPawnRenderState>>
        //?}
         {
    private final HumanoidModel<
            //? if >1.21.2 {
            ChaosPawnRenderState
            //?} else {
            /*ChaosPawn
            *///?}
            > model;
    public ChaosPawnTint(ChaosPawnRenderer context, HumanoidModel<
            //? if >1.21.2 {
            ChaosPawnRenderState
            //?} else {
            /*ChaosPawn
             *///?}
            > model) {
        super(context);
        this.model = model;
    }

    //? if >1.21.9 {
    /*public void renderOneLayer(PoseStack matrixStack, SubmitNodeCollector vertexConsumer, int light, int overlay, ChaosPawnRenderState livingEntity, ModelPart part, String name) {
        int color;
        color = livingEntity.colors.get(name);
        if (livingEntity.customName != null) {
            String s = livingEntity.customName;
            if ("jeb_".equals(s)) {
                color = TintableEntity.getColorJeb((int) (livingEntity.ageInTicks + (name.equals("hat") ? 200 : 0)), livingEntity.id);            }
            if ("hue".equals(s)) {
                int n = (int) (livingEntity.ageInTicks + (name.equals("hat") ? 200 : 0) + livingEntity.id);
                float hue = n/400.f;
                hue = hue - (int)hue;
                color = Color.getHSBColor(hue, 1.0f, 1.0f).getRGB();
            }
        }
        else {
            color = VersionMethods.opaque(color);
        }
        //FIXME 1.21.10 this probably shouldn't be null
        vertexConsumer.submitModelPart(part, matrixStack, RenderType.cutout(), light, overlay, null, color, null);
    }
    *///?} else if >1.21.5 {
    public void renderOneLayer(PoseStack matrixStack, VertexConsumer vertexConsumer, int light, int overlay, ChaosPawnRenderState livingEntity, ModelPart part, String name) {
        int color;
        color = livingEntity.colors.get(name);
        if (livingEntity.customName != null) {
            String s = livingEntity.customName;
            if ("jeb_".equals(s)) {
                color = TintableEntity.getColorJeb((int) (livingEntity.ageInTicks + (name.equals("hat") ? 200 : 0)), livingEntity.id);
            }
            if ("hue".equals(s)) {
                int n = (int) (livingEntity.ageInTicks + (name.equals("hat") ? 200 : 0) + livingEntity.id);
                float hue = n/400.f;
                hue = hue - (int)hue;
                color = Color.getHSBColor(hue, 1.0f, 1.0f).getRGB();
            }
        }
        else {
            color = VersionMethods.opaque(color);
        }
        part.render(matrixStack, vertexConsumer, light, overlay, color);
    }
    //?} else {
         /*public void renderOneLayer(PoseStack matrixStack, VertexConsumer vertexConsumer, int light, int overlay, ChaosPawn livingEntity, ModelPart part, String name) {
             int color;
             color = livingEntity.getColors().get(name);
             if (livingEntity.hasCustomName()) {
                 String s = livingEntity.getName().getString();
                 if ("jeb_".equals(s)) {
                     color = TintableEntity.getColorJeb(livingEntity.tickCount + (name.equals("hat") ? 200 : 0), livingEntity.getId());
                 }
                 if ("hue".equals(s)) {
                     int n = livingEntity.tickCount + (name.equals("hat") ? 200 : 0) + livingEntity.getId();
                     float hue = n/400.f;
                     hue = hue - (int)hue;
                     color = Color.getHSBColor(hue, 1.0f, 1.0f).getRGB();
                 }
             }
             else {
                 color = VersionMethods.opaque(color);
             }
             //? if >1.21 {
             part.render(matrixStack, vertexConsumer, light, overlay, color);
             //?} else {
             /^part.render(matrixStack, vertexConsumer, light, overlay, FastColor.ARGB32.red(color), FastColor.ARGB32.green(color), FastColor.ARGB32.blue(color), 1.0f);
              ^///?}
         }
             *///?}

    //? if >1.21.9 {
    /*@Override
    public void submit(PoseStack matrixStack, SubmitNodeCollector nodeCollector, int light, ChaosPawnRenderState state, float yRot, float xRot) {
        boolean bl = state.appearsGlowing() && state.isInvisible;
        if (state.isInvisible && !bl) {
            return;
        }
        ResourceLocation texture = InfinityMethods.getId("textures/entity/empty.png");
//        VertexConsumer vertexConsumer = bl ? nodeCollector.getBuffer(RenderType.outline(texture)) : vertexConsumerProvider.getBuffer(RenderType.entityTranslucent(texture));
        // TODO 1.21.3 these were probably necessary
        //        (this.getContextModel()).copyStateTo(this.model);
        //        this.model.animateModel(e, f, g, h);
        this.model.setupAnim(state);
        int o = LivingEntityRenderer.getOverlayCoords(state, 0.0f);
        renderOneLayer(matrixStack, nodeCollector, light, o, state, model.body, "body");
        renderOneLayer(matrixStack, nodeCollector, light, o, state, model.head, "head");
        renderOneLayer(matrixStack, nodeCollector, light, o, state, model.hat, "hat");
        renderOneLayer(matrixStack, nodeCollector, light, o, state, model.leftArm, "left_arm");
        renderOneLayer(matrixStack, nodeCollector, light, o, state, model.rightArm, "right_arm");
        renderOneLayer(matrixStack, nodeCollector, light, o, state, model.leftLeg, "left_leg");
        renderOneLayer(matrixStack, nodeCollector, light, o, state, model.rightLeg, "right_leg");
    }
    *///?} else if >1.21.4 {
         @Override
         public void render(PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int light, ChaosPawnRenderState state, float limbAngle, float limbDistance) {
             boolean bl = state.appearsGlowing && state.isInvisible;
             if (state.isInvisible && !bl) {
                 return;
             }
             ResourceLocation texture = InfinityMethods.getId("textures/entity/empty.png");
             VertexConsumer vertexConsumer = bl ? vertexConsumerProvider.getBuffer(RenderType.outline(texture)) : vertexConsumerProvider.getBuffer(RenderType.entityTranslucent(texture));
             // TODO 1.21.3 these were probably necessary
             //        (this.getContextModel()).copyStateTo(this.model);
             //        this.model.animateModel(e, f, g, h);
             this.model.setupAnim(state);
             int o = LivingEntityRenderer.getOverlayCoords(state, 0.0f);
             renderOneLayer(matrixStack, vertexConsumer, light, o, state, model.body, "body");
             renderOneLayer(matrixStack, vertexConsumer, light, o, state, model.head, "head");
             renderOneLayer(matrixStack, vertexConsumer, light, o, state, model.hat, "hat");
             renderOneLayer(matrixStack, vertexConsumer, light, o, state, model.leftArm, "left_arm");
             renderOneLayer(matrixStack, vertexConsumer, light, o, state, model.rightArm, "right_arm");
             renderOneLayer(matrixStack, vertexConsumer, light, o, state, model.leftLeg, "left_leg");
             renderOneLayer(matrixStack, vertexConsumer, light, o, state, model.rightLeg, "right_leg");
         }
    //?} else {
    /*@Override
    public void render(PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i, ChaosPawn e, float f, float g, float h, float j, float k, float l) {
        boolean bl = Minecraft.getInstance().shouldEntityAppearGlowing(e) && e.isInvisible();
        if (e.isInvisible() && !bl) {
            return;
        }
        VertexConsumer vertexConsumer = bl ? vertexConsumerProvider.getBuffer(RenderType.outline(this.getTextureLocation(e))) : vertexConsumerProvider.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(e)));
        (this.getParentModel()).copyPropertiesTo(this.model);
        this.model.prepareMobModel(e, f, g, h);
        this.model.setupAnim(e, f, g, j, k, l);
        int o = LivingEntityRenderer.getOverlayCoords(e, 0.0f);
        renderOneLayer(matrixStack, vertexConsumer, i, o, e, model.body, "body");
        renderOneLayer(matrixStack, vertexConsumer, i, o, e, model.head, "head");
        renderOneLayer(matrixStack, vertexConsumer, i, o, e, model.hat, "hat");
        renderOneLayer(matrixStack, vertexConsumer, i, o, e, model.leftArm, "left_arm");
        renderOneLayer(matrixStack, vertexConsumer, i, o, e, model.rightArm, "right_arm");
        renderOneLayer(matrixStack, vertexConsumer, i, o, e, model.leftLeg, "left_leg");
        renderOneLayer(matrixStack, vertexConsumer, i, o, e, model.rightLeg, "right_leg");
    }
    *///?}
}
