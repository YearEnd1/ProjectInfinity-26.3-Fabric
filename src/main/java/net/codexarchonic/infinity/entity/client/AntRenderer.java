package net.codexarchonic.infinity.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.codexarchonic.infinity.entity.custom.AntEntity;
import net.codexarchonic.infinity.util.InfinityMethods;
import net.minecraft.client.renderer.MultiBufferSource;
//? if >1.21.9 {
/*import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.CameraRenderState;
*///?}
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jspecify.annotations.NonNull;

//~ if >1.21.4 'AntEntity, AntModel'->'AntEntity, AntEntityRenderState, AntModel' {
public class AntRenderer extends MobRenderer<AntEntity, AntEntityRenderState, AntModel> {
//~}

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
    public @NonNull ResourceLocation getTextureLocation(AntEntityRenderState entity) {
        return TEXTURE;
    }

    //? if >1.21.9 {
    /*@Override
    public void submit(AntEntityRenderState ant, PoseStack matrixStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        if(ant.isBaby) {
            matrixStack.scale(0.5f, 0.5f, 0.5f);
        } else {
            matrixStack.scale(1f, 1f, 1f);
        }
        super.submit(ant, matrixStack, submitNodeCollector, cameraRenderState);
    }
    *///?} else {
    public void render(AntEntityRenderState mobEntity, PoseStack matrixStack,
                       MultiBufferSource vertexConsumerProvider, int i) {
        if(mobEntity.isBaby) {
            matrixStack.scale(0.5f, 0.5f, 0.5f);
        } else {
            matrixStack.scale(1f, 1f, 1f);
        }

        super.render(mobEntity, matrixStack, vertexConsumerProvider, i);
    }
    //?}
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
