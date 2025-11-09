package net.lerariemann.infinity.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.lerariemann.infinity.entity.custom.AntEntity;
import net.lerariemann.infinity.util.InfinityMethods;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class AntRenderer extends MobRenderer<AntEntity, AntModel<AntEntity>> {
    private static final ResourceLocation TEXTURE = InfinityMethods.getId("textures/entity/ant.png");

    public AntRenderer(EntityRendererProvider.Context context) {
        super(context, new AntModel<>(context.bakeLayer(AntModel.MODEL_LAYER)), 0.6f);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(AntEntity entity) {
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
}
