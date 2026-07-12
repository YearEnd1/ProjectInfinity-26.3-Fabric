package net.codexarchonic.infinity.entity.client;

import net.codexarchonic.infinity.entity.custom.ChaosSkeleton;
import net.codexarchonic.infinity.util.VersionMethods;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.ModelLayers;
//? if >1.21.9 {
/*import net.minecraft.client.renderer.entity.ArmorModelSet;
*///?}
//? if >1.21.1 {
import net.minecraft.client.renderer.entity.AbstractSkeletonRenderer;
//?} else {
/*import net.minecraft.client.renderer.entity.SkeletonRenderer;
*///?}
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class ChaosSkeletonRenderer extends
        //? if <1.21.1 {
        /*SkeletonRenderer
         *///?}
        //? if =1.21.1 {
        /*SkeletonRenderer<ChaosSkeleton>
        *///?}
        //? if >1.21.1 {
        AbstractSkeletonRenderer<ChaosSkeleton, TintedRenderState.Skeleton>
         //?}
{
    public static final ResourceLocation TEXTURE = VersionMethods.id("textures/entity/skeleton/skeleton.png");

    public ChaosSkeletonRenderer(EntityRendererProvider.Context context) {
        //? if >1.21.9 {
        /*super(context, ModelLayers.SKELETON, ModelLayers.SKELETON_ARMOR);
        *///?} else if >1.21.2 {
        super(context, ModelLayers.SKELETON, ModelLayers.SKELETON_INNER_ARMOR, ModelLayers.SKELETON_OUTER_ARMOR);
        //?} else {
        /*super(context);
        *///?}
        this.addLayer(new TintedLayerRenderer<>(this, new SkeletonModel<>(context.bakeLayer(ModelLayers.SKELETON))));
    }

    //? if >1.21.2 {
    @Override
    public TintedRenderState.Skeleton createRenderState() {
        return new TintedRenderState.Skeleton();
    }

    @Override
    public void extractRenderState(ChaosSkeleton skeleton, TintedRenderState.Skeleton state, float f) {
        super.extractRenderState(skeleton, state, f);
        state.colour = skeleton.getColorForRender();
    }

    @Override
    public ResourceLocation getTextureLocation(TintedRenderState.Skeleton renderState) {
        return TEXTURE;
    }
    //?}
}
