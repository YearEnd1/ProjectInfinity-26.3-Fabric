package net.lerariemann.infinity.entity.client;

import net.lerariemann.infinity.entity.custom.ChaosPawn;
import net.lerariemann.infinity.util.InfinityMethods;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

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
    public ResourceLocation getTextureLocation(ChaosPawnRenderState renderState) {
        return TEXTURE;
    }
    //?} else {

    /*@Override
    public @NotNull ResourceLocation getTextureLocation(ChaosPawn e) {
        return TEXTURE;
    }
    *///?}

}
