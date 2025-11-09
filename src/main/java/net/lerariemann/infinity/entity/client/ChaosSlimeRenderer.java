package net.lerariemann.infinity.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.lerariemann.infinity.entity.custom.ChaosSlime;
import net.lerariemann.infinity.util.InfinityMethods;
import net.minecraft.client.model.SlimeModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class ChaosSlimeRenderer extends MobRenderer<ChaosSlime, SlimeModel<ChaosSlime>> {
    private static final ResourceLocation TEXTURE = InfinityMethods.getId("textures/entity/slime.png");

    public ChaosSlimeRenderer(EntityRendererProvider.Context context) {
        super(context, new SlimeModel<>(context.bakeLayer(ModelLayers.SLIME)), 0.25f);
        this.addLayer(new DimensionalSlimeCoreRenderer(this, context.getBlockRenderDispatcher()));
        this.addLayer(new TintedLayerRenderer<>(this, new SlimeModel<>(context.getModelSet().bakeLayer(ModelLayers.SLIME_OUTER))));
    }
    @Override
    public @NotNull ResourceLocation getTextureLocation(ChaosSlime slimeEntity) {
        return TEXTURE;
    }
    @Override
    public void render(ChaosSlime slimeEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        this.shadowRadius = 0.25f * (float)slimeEntity.getSize();
        super.render(slimeEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
    @Override
    protected void scale(ChaosSlime slimeEntity, PoseStack matrixStack, float f) {
        float g = 0.999f;
        matrixStack.scale(g, g, g);
        matrixStack.translate(0.0f, 0.001f, 0.0f);
        float h = slimeEntity.getSize();
        float i = Mth.lerp(f, slimeEntity.oSquish, slimeEntity.squish) / (h * 0.5f + 1.0f);
        float j = 1.0f / (i + 1.0f);
        matrixStack.scale(j * h, 1.0f / j * h, j * h);
    }
}
