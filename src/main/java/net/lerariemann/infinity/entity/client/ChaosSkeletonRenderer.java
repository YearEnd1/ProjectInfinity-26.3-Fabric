package net.lerariemann.infinity.entity.client;

import net.lerariemann.infinity.entity.custom.ChaosSkeleton;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SkeletonRenderer;

public class ChaosSkeletonRenderer extends SkeletonRenderer
        //? if >1.21
        <ChaosSkeleton>
{
    public ChaosSkeletonRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.addLayer(new TintedLayerRenderer<>(this, new SkeletonModel<>(context.bakeLayer(ModelLayers.SKELETON))));
    }
}
