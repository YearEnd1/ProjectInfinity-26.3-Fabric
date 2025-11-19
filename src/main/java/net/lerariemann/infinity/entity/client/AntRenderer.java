package net.lerariemann.infinity.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.lerariemann.infinity.entity.custom.AntEntity;
import net.lerariemann.infinity.util.InfinityMethods;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class AntRenderer extends MobRenderer<AntEntity, AntEntityRenderState, AntModel> {
    private static final ResourceLocation TEXTURE = InfinityMethods.getId("textures/entity/ant.png");

    public AntRenderer(EntityRendererProvider.Context context) {
        super(context, new AntModel(context.bakeLayer(AntModel.MODEL_LAYER)), 0.6f);
    }

    //? if >1.21.2 {
    @Override
    public AntEntityRenderState createRenderState() {
        return new AntEntityRenderState();
    }
    @Override
    public @NotNull ResourceLocation getTextureLocation(AntEntityRenderState entity) {
        return TEXTURE;
    }

    @Override
    public void submit(AntEntityRenderState ant, PoseStack matrixStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        if(ant.isBaby) {
            matrixStack.scale(0.5f, 0.5f, 0.5f);
        } else {
            matrixStack.scale(1f, 1f, 1f);
        }
        super.submit(ant, matrixStack, submitNodeCollector, cameraRenderState);
    }
    //?} else {
    /*@Override
    public ResourceLocation getTextureLocation(AntEntity renderState) {
        return TEXTURE;
    }

    @Override
    public void render(AntEntity mobEntity, float f, float g, PoseStack matrixStack,
                       MultiBufferSource vertexConsumerProvider, int i) {
        if(mobEntity.isBaby()) {
            matrixStack.scale(0.5f, 0.5f, 0.5f);
        } else {
            matrixStack.scale(1f, 1f, 1f);
        }

        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
    *///?}
}
