package net.lerariemann.infinity.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.lerariemann.infinity.entity.custom.BishopEntity;
import net.lerariemann.infinity.util.InfinityMethods;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class BishopRenderer extends HumanoidMobRenderer<BishopEntity,
        //? if >1.21.2 {
        BishopEntityRenderState,
        //?}
        BishopModel> {
    private static final ResourceLocation TEXTURE = InfinityMethods.getId("textures/entity/bishop.png");

    public BishopRenderer(EntityRendererProvider.Context context) {
        super(context, new BishopModel(context.bakeLayer(BishopModel.MODEL_LAYER)), 0.6f);
        //? if >1.21.9 {
        this.addLayer(new HumanoidArmorLayer<>(this,
                new BishopModel(context.bakeLayer(ModelLayers.SKELETON_ARMOR)),
                new BishopModel(context.bakeLayer(ModelLayers.SKELETON_ARMOR)),
                context.getEquipmentRenderer()));
        //?} else {
        /*this.addLayer(new HumanoidArmorLayer<>(this,
                new BishopModel(context.bakeLayer(ModelLayers.SKELETON_INNER_ARMOR)),
                new BishopModel(context.bakeLayer(ModelLayers.SKELETON_OUTER_ARMOR)),
                context.getModelManager()));
        *///?}

    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(
            //? if >1.21.2 {
            BishopEntityRenderState
            //?} else {
            /*BishopEntity
            *///?}
                    entity) {
        return TEXTURE;
    }

    //? if >1.21.2 {
    @Override
    public BishopEntityRenderState createRenderState() {
        return new BishopEntityRenderState();
    }
    //?}

    @Override
    public void render(BishopEntity mobEntity, float f, float g, PoseStack matrixStack,
                       MultiBufferSource vertexConsumerProvider, int i) {
        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
}
