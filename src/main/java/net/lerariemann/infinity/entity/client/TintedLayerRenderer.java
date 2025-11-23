package net.lerariemann.infinity.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.lerariemann.infinity.entity.custom.TintableEntity;
import net.lerariemann.infinity.util.InfinityMethods;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
//? if >1.21.9 {
/*import net.minecraft.client.renderer.SubmitNodeCollector;
*///?}
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
//? if <1.21.2 {
/*import net.minecraft.util.FastColor;
*///?} else {
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
//?}
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;

public class TintedLayerRenderer<T extends
        //? if >1.21.2 {
        LivingEntityRenderState
        //?} else {
        /*Mob
        *///?}
        , S extends EntityModel<T>> extends RenderLayer<T, S> {
    private final S model;

    public TintedLayerRenderer(RenderLayerParent<T, S> context, S model) {
        super(context);
        this.model = model;
    }



    //? if >1.21.9 {

    /*@Override
    public void submit(PoseStack matrixStack, SubmitNodeCollector nodeCollector, int packedLight, T renderState, float yRot, float xRot) {
        boolean bl = renderState.appearsGlowing() && renderState.isInvisible;
        if (renderState.isInvisible && !bl) {
            return;
        }

        // TODO 1.21.10 find a better way of extracting a texture from a render state
        ResourceLocation texture;
        if (renderState instanceof ChaosPawnRenderState) {
            texture = InfinityMethods.getId("textures/entity/empty.png");
        } else if (renderState instanceof ChaosSlimeRenderState) {
            texture = InfinityMethods.getId("textures/entity/slime.png");
        } else {
            texture = InfinityMethods.getId("textures/entity/empty.png");
        }

        (this.getParentModel()).copyTransforms(this.model);
        this.model.setupAnim(renderState);
        int color = 16777215;
        if (renderState instanceof TintableEntity ent) {
            color = ent.getColorForRender();
        }
        nodeCollector.submitModel(model, renderState, matrixStack, RenderType.outline(texture), packedLight, LivingEntityRenderer.getOverlayCoords(renderState, 0.0f), color, null, renderState.outlineColor, null);
    }
    *///?} else if >1.21.2 {
    //    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l) {
    @Override
    public void render(PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int light, T livingEntity, float limbAngle, float limbDistance) {
        boolean bl = livingEntity.appearsGlowing && livingEntity.isInvisible;
        if (livingEntity.isInvisible && !bl) {
            return;
        }
        // TODO 1.21.3 find a better way of extracting a texture from a render state
        ResourceLocation texture;
        if (livingEntity instanceof ChaosPawnRenderState) {
            texture = InfinityMethods.getId("textures/entity/empty.png");
        } else if (livingEntity instanceof ChaosSlimeRenderState) {
            texture = InfinityMethods.getId("textures/entity/slime.png");
        } else {
            texture = InfinityMethods.getId("textures/entity/empty.png");
        }
        VertexConsumer vertexConsumer = bl ? vertexConsumerProvider.getBuffer(RenderType.outline(texture)) : vertexConsumerProvider.getBuffer(RenderType.entityTranslucent(texture));
        // TODO 1.21.3 these were probably necessary
//        this.getContextModel().copyStateTo(this.model);
//        this.model.animateModel(livingEntity, f, g, h);
        this.model.setupAnim(livingEntity);
        int color = 16777215;
        if (livingEntity instanceof TintableEntity ent) {
            color = ent.getColorForRender();
        }
        this.model.renderToBuffer(matrixStack, vertexConsumer, light, LivingEntityRenderer.getOverlayCoords(livingEntity, 0.0f), color);
    }

    //?} else {
    /*@Override
    public void render(PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l) {
        boolean bl = Minecraft.getInstance().shouldEntityAppearGlowing(livingEntity) && livingEntity.isInvisible();
        if (livingEntity.isInvisible() && !bl) {
            return;
        }
        VertexConsumer vertexConsumer = bl ? vertexConsumerProvider.getBuffer(RenderType.outline(this.getTextureLocation(livingEntity))) : vertexConsumerProvider.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(livingEntity)));
        (this.getParentModel()).copyPropertiesTo(this.model);
        this.model.prepareMobModel(livingEntity, f, g, h);
        this.model.setupAnim(livingEntity, f, g, j, k, l);
        int color = 16777215;
        if (livingEntity instanceof TintableEntity ent) {
            color = ent.getColorForRender();
        }
        //? if >1.21 {
        this.model.renderToBuffer(matrixStack, vertexConsumer, i, LivingEntityRenderer.getOverlayCoords(livingEntity, 0.0f), color);
        //?} else {
        /^this.model.renderToBuffer(matrixStack, vertexConsumer, i, LivingEntityRenderer.getOverlayCoords(livingEntity, 0.0f), FastColor.ARGB32.red(color), FastColor.ARGB32.green(color), FastColor.ARGB32.blue(color), 1.0f);
         ^///?}
    }
    *///?}
}
