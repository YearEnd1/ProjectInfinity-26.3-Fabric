package net.codexarchonic.infinity.entity.client;

import net.codexarchonic.infinity.entity.custom.ChaosPawn;
import net.codexarchonic.infinity.util.InfinityMethods;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jspecify.annotations.NonNull;

public class ChaosPawnRenderer
        //? if >1.21.4 {
        extends HumanoidMobRenderer<ChaosPawn, ChaosPawnRenderState, HumanoidModel<ChaosPawnRenderState>>
        //?} else {
        /*extends HumanoidMobRenderer<ChaosPawn, HumanoidModel<ChaosPawn>>
        *///?}
        {
    private static final ResourceLocation TEXTURE = InfinityMethods.getId("textures/entity/empty.png");
    public ChaosPawnRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)), 0.25f);
        this.addLayer(new ChaosPawnTint(this, new HumanoidModel<>(context.getModelSet().bakeLayer(ModelLayers.PLAYER))));
    }

    //? if >1.21.2 {
    @Override
    public ChaosPawnRenderState createRenderState() {
        return new ChaosPawnRenderState();
    }

    @Override
    public void extractRenderState(ChaosPawn entity, ChaosPawnRenderState reusedState, float partialTick) {
        super.extractRenderState(entity, reusedState, partialTick);
        reusedState.colors = entity.getColors();
        reusedState.id = entity.getId();
        if (entity.hasCustomName())
            reusedState.customName = entity.getCustomName().getString();
    }

    @Override
    public ResourceLocation getTextureLocation(ChaosPawnRenderState renderState) {
        return TEXTURE;
    }
    //?} else {

    /*@Override
    public @NonNull ResourceLocation getTextureLocation(ChaosPawn e) {
        return TEXTURE;
    }
    *///?}

}
