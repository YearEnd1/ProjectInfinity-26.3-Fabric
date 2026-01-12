package net.lerariemann.infinity.entity.client;

import net.lerariemann.infinity.entity.custom.ChaosSkeleton;
import net.lerariemann.infinity.util.VersionMethods;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.ModelLayers;
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
        //? if <1.21.2 {
        /*super(context);
        *///?} else {
        super(context, ModelLayers.SKELETON, ModelLayers.SKELETON_INNER_ARMOR, ModelLayers.SKELETON_OUTER_ARMOR);
        //?}
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
