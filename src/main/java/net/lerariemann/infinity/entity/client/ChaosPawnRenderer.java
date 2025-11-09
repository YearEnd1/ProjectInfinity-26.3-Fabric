package net.lerariemann.infinity.entity.client;

import net.lerariemann.infinity.entity.custom.ChaosPawn;
import net.lerariemann.infinity.util.InfinityMethods;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class ChaosPawnRenderer extends HumanoidMobRenderer<ChaosPawn, HumanoidModel<ChaosPawn>> {
    private static final ResourceLocation TEXTURE = InfinityMethods.getId("textures/entity/empty.png");
    public ChaosPawnRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)), 0.25f);
        this.addLayer(new ChaosPawnTint(this, new HumanoidModel<>(context.getModelSet().bakeLayer(ModelLayers.PLAYER))));
    }
    @Override
    public @NotNull ResourceLocation getTextureLocation(ChaosPawn e) {
        return TEXTURE;
    }
}
