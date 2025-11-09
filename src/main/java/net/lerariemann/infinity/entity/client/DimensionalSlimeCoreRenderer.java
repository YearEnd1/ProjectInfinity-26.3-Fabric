package net.lerariemann.infinity.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.lerariemann.infinity.entity.custom.ChaosSlime;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.SlimeModel;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.state.BlockState;

public class DimensionalSlimeCoreRenderer extends RenderLayer<ChaosSlime, SlimeModel<ChaosSlime>> {
    private final BlockRenderDispatcher blockRenderManager;

    public DimensionalSlimeCoreRenderer(RenderLayerParent<ChaosSlime, SlimeModel<ChaosSlime>> context, BlockRenderDispatcher blockRenderManager) {
        super(context);
        this.blockRenderManager = blockRenderManager;
    }
    @Override
    public void render(PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i, ChaosSlime slime, float f, float g, float h, float j, float k, float l) {
        boolean bl = Minecraft.getInstance().shouldEntityAppearGlowing(slime) && slime.isInvisible();
        if (slime.isInvisible() && !bl) {
            return;
        }
        BlockState blockState = slime.getCore();
        int m = LivingEntityRenderer.getOverlayCoords(slime, 0.0f);
        matrixStack.pushPose();
        matrixStack.scale(0.25f, 0.25f, 0.25f);
        matrixStack.translate(-0.5f, 4.5f, -0.5f);
        this.renderCore(matrixStack, vertexConsumerProvider, i, blockState, m);
        matrixStack.popPose();
    }

    private void renderCore(PoseStack matrices, MultiBufferSource vertexConsumers, int light, BlockState coreState, int overlay) {
        BakedModel coreModel = this.blockRenderManager.getBlockModel(coreState);
        this.blockRenderManager.getModelRenderer().renderModel(matrices.last(), vertexConsumers.getBuffer(ItemBlockRenderTypes.getChunkRenderType(coreState)), coreState, coreModel, 1.0f, 1.0f, 1.0f, light, overlay);
    }
}
