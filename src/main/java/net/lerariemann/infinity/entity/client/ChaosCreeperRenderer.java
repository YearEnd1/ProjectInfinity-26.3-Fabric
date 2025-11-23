package net.lerariemann.infinity.entity.client;

import net.lerariemann.infinity.util.InfinityMethods;
import net.minecraft.client.model.CreeperModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.CreeperRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
//? if >1.21.4 {
import net.minecraft.client.renderer.entity.state.CreeperRenderState;
//?}
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Creeper;

public class ChaosCreeperRenderer extends CreeperRenderer {
    private static final ResourceLocation TEXTURE2 = InfinityMethods.getId("textures/entity/creeper.png");

    @Override
    public ResourceLocation getTextureLocation(
            //? if >1.21.2 {
            CreeperRenderState
            //?} else {
            /*Creeper
            *///?}
                    creeperEntity) {
        return TEXTURE2;
    }

    public ChaosCreeperRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.addLayer(new TintedLayerRenderer<>(this, new CreeperModel(context.bakeLayer(ModelLayers.CREEPER))));
    }
}
