package net.lerariemann.infinity.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.lerariemann.infinity.entity.custom.TintableEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.Mob;

public class TintedLayerRenderer<T extends Mob, S extends EntityModel<T>> extends RenderLayer<T, S> {
    private final S model;

    public TintedLayerRenderer(RenderLayerParent<T, S> context, S model) {
        super(context);
        this.model = model;
    }

    @Override
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
        this.model.renderToBuffer(matrixStack, vertexConsumer, i, LivingEntityRenderer.getOverlayCoords(livingEntity, 0.0f), color);
    }
}
