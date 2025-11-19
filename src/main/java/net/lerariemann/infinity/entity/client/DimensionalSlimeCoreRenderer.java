package net.lerariemann.infinity.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
//? if >1.21.2 {
import net.fabricmc.fabric.api.renderer.v1.render.FabricBlockModelRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
//?} else {
/*import net.minecraft.client.renderer.MultiBufferSource;
*///?}
import net.minecraft.client.model.SlimeModel;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class DimensionalSlimeCoreRenderer extends

        //? if >1.21.2 {
        RenderLayer<ChaosSlimeRenderState, SlimeModel>
        //?} else {
        /*RenderLayer<ChaosSlime, SlimeModel<ChaosSlime>>
        *///?}
         {
    private final BlockRenderDispatcher blockRenderManager;

    public DimensionalSlimeCoreRenderer(
            RenderLayerParent
            //? if >1.21.2 {
            <ChaosSlimeRenderState, SlimeModel>
            //?} else {
            /*<ChaosSlime, SlimeModel<ChaosSlime>>
            *///?}
                    context, BlockRenderDispatcher blockRenderManager) {
        super(context);
        this.blockRenderManager = blockRenderManager;
    }

    @Override
    //? if >1.21.2 {
    public void submit(PoseStack matrixStack, SubmitNodeCollector nodeCollector, int packedLight, ChaosSlimeRenderState slime, float yRot, float xRot) {
        boolean bl = slime.appearsGlowing() && slime.isInvisible;
        if (slime.isInvisible && !bl) {
            return;
        }
        BlockState blockState = slime.core;
        int m = LivingEntityRenderer.getOverlayCoords(slime, 0.0f);
        matrixStack.pushPose();
        matrixStack.scale(0.25f, 0.25f, 0.25f);
        matrixStack.translate(-0.5f, 4.5f, -0.5f);
        this.renderCore(matrixStack, nodeCollector, packedLight, blockState, m);
        matrixStack.popPose();
    //?} else {
    /*public void render(PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i, ChaosSlime slime, float f, float g, float h, float j, float k, float l) {
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
    *///?}
    }

    private void renderCore(PoseStack matrices,
                            //? if >1.21.2 {
                            SubmitNodeCollector
                            //?} else {
                            /*MultiBufferSource
                            *///?}

                                    vertexConsumers, int light, BlockState coreState, int overlay) {
        var coreModel = this.blockRenderManager.getBlockModel(coreState);
        //? if >1.21.9 {
        vertexConsumers.submitBlock(matrices, coreState, light, overlay, 0);
        //?} else if >1.21.8 {
        /*FabricBlockModelRenderer.render(matrices.last(), layer-> vertexConsumers, coreModel, 1.0f, 1.0f, 1.0f, light, overlay, null, BlockPos.ZERO, coreState);
        *///?} else {
        /*this.blockRenderManager.getModelRenderer().renderModel(matrices.last(), vertexConsumers.getBuffer(ItemBlockRenderTypes.getChunkRenderType(coreState)), coreState, coreModel, 1.0f, 1.0f, 1.0f, light, overlay);
        *///?}
    }
}
