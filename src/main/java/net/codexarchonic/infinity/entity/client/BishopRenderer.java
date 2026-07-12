package net.codexarchonic.infinity.entity.client;

import net.codexarchonic.infinity.entity.custom.BishopEntity;
import net.codexarchonic.infinity.util.InfinityMethods;
import net.minecraft.client.model.geom.ModelLayers;
//? if >1.21.9 {
/*import net.minecraft.client.renderer.entity.ArmorModelSet;
*///?}
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import org.jspecify.annotations.NonNull;

public class BishopRenderer extends
        //? if >1.21.2 {
        HumanoidMobRenderer<BishopEntity, BishopEntityRenderState, BishopModel>
        //?} else {
        /*HumanoidMobRenderer<BishopEntity, BishopModel>
        *///?}
         {
    private static final ResourceLocation TEXTURE = InfinityMethods.getId("textures/entity/bishop.png");

    public BishopRenderer(EntityRendererProvider.Context context) {
        super(context, new BishopModel(context.bakeLayer(BishopModel.MODEL_LAYER)), 0.6f);
        //? if >1.21.9 {
        /*this.addLayer(new HumanoidArmorLayer<>(this,
                ArmorModelSet.bake(ModelLayers.SKELETON_ARMOR, context.getModelSet(), BishopModel::new),
                context.getEquipmentRenderer()));
        *///?} else if >1.21.5 {
        this.addLayer(new HumanoidArmorLayer<>(this,
                new BishopModel(context.bakeLayer(ModelLayers.SKELETON_INNER_ARMOR)),
                new BishopModel(context.bakeLayer(ModelLayers.SKELETON_OUTER_ARMOR)),
                context.getEquipmentRenderer()));
        //?} else {
        /*this.addLayer(new HumanoidArmorLayer<>(this,
                new BishopModel(context.bakeLayer(ModelLayers.SKELETON_INNER_ARMOR)),
                new BishopModel(context.bakeLayer(ModelLayers.SKELETON_OUTER_ARMOR)),
                context.getModelManager()));
        *///?}
    }

    @Override
    public @NonNull ResourceLocation getTextureLocation(
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
    //?} else {
    /*@Override
    public void render(BishopEntity mobEntity, float f, float g, PoseStack matrixStack,
                       MultiBufferSource vertexConsumerProvider, int i) {
        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
    *///?}


}
