package net.lerariemann.infinity.entity.client;

import net.lerariemann.infinity.entity.custom.ChaosPawn;
import net.lerariemann.infinity.entity.custom.TintableEntity;
import net.lerariemann.infinity.util.VersionMethods;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.util.FastColor;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.awt.Color;

public class ChaosPawnTint extends RenderLayer<ChaosPawn, HumanoidModel<ChaosPawn>> {
    private final HumanoidModel<ChaosPawn> model;
    public ChaosPawnTint(ChaosPawnRenderer context, HumanoidModel<ChaosPawn> model) {
        super(context);
        this.model = model;
    }

    public void renderOneLayer(PoseStack matrixStack, VertexConsumer vertexConsumer, int light, int overlay, ChaosPawn livingEntity, ModelPart part, String name) {
        int color;
        color = livingEntity.getColors().getInt(name);
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
        /*part.render(matrixStack, vertexConsumer, light, overlay, color);
        *///?} else {
        part.render(matrixStack, vertexConsumer, light, overlay, FastColor.ARGB32.red(color), FastColor.ARGB32.green(color), FastColor.ARGB32.blue(color), 1.0f);
        //?}
    }

    @Override
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
}
