package net.lerariemann.infinity.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.lerariemann.infinity.entity.custom.ChaosCreeper;
import net.lerariemann.infinity.util.InfinityMethods;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.model.geom.ModelLayers;
//? if >1.21.4 {
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
//?} else {
/*import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.client.model.CreeperModel;
*///?}
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class ChaosCreeperRenderer extends
        //? if >1.21.2 {
        MobRenderer<ChaosCreeper, TintedRenderState.Creeper, ChaosCreeperRenderer.Model>
        //?} else {
        /*CreeperRenderer
         *///?}
{
    public static final ResourceLocation TEXTURE = InfinityMethods.getId("textures/entity/creeper.png");

    @Override
    public ResourceLocation getTextureLocation(
            //? if >1.21.2 {
            TintedRenderState.Creeper
            //?} else {
            /*Creeper
            *///?}
                    creeperEntity) {
        return TEXTURE;
    }

    public ChaosCreeperRenderer(EntityRendererProvider.Context context) {
        //? if <1.21.2 {
        /*super(context);
        this.addLayer(new TintedLayerRenderer<>(this, new CreeperModel(context.bakeLayer(ModelLayers.CREEPER))));
         *///?} else {
        super(context, new ChaosCreeperRenderer.Model(context.bakeLayer(ModelLayers.CREEPER)), 0.5F);
        this.addLayer(new PowerLayer(this, context.getModelSet()));
        this.addLayer(new TintedLayerRenderer<>(this, new ChaosCreeperRenderer.Model(context.bakeLayer(ModelLayers.CREEPER))));
        //?}
    }

    //? if >1.21.2 {
    public TintedRenderState.Creeper createRenderState() {
        return new TintedRenderState.Creeper();
    }

    public void extractRenderState(ChaosCreeper creeper, TintedRenderState.Creeper state, float f) {
        super.extractRenderState(creeper, state, f);
        state.swelling = creeper.getSwelling(f);
        state.isPowered = creeper.isPowered();
        state.colour = creeper.getColorForRender();
    }

    //mojang decided abstract creepers should not exist and now i have to suffer
    protected void scale(TintedRenderState.Creeper state, PoseStack poseStack) {
        float f = state.swelling;
        float f1 = 1.0F + Mth.sin(f * 100.0F) * f * 0.01F;
        f = Mth.clamp(f, 0.0F, 1.0F);
        f *= f;
        f *= f;
        float f2 = (1.0F + f * 0.4F) * f1;
        float f3 = (1.0F + f * 0.1F) / f1;
        poseStack.scale(f2, f3, f2);
    }

    public float getWhiteOverlayProgress(TintedRenderState.Creeper state) {
        float f = state.swelling;
        return (int)(f * 10.0F) % 2 == 0 ? 0.0F : Mth.clamp(f, 0.5F, 1.0F);
    }

    public class PowerLayer extends EnergySwirlLayer<TintedRenderState.Creeper, ChaosCreeperRenderer.Model> {
        private static final ResourceLocation POWER_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/creeper/creeper_armor.png");
        private final ChaosCreeperRenderer.Model model;

        public PowerLayer(RenderLayerParent<TintedRenderState.Creeper, ChaosCreeperRenderer.Model> renderer, EntityModelSet modelSet) {
            super(renderer);
            this.model = new ChaosCreeperRenderer.Model(modelSet.bakeLayer(ModelLayers.CREEPER_ARMOR));
        }

        protected boolean isPowered(TintedRenderState.Creeper renderState) {
            return renderState.isPowered;
        }

        @Override
        protected float xOffset(float tickCount) {
            return tickCount * 0.01F;
        }

        @Override
        protected ResourceLocation getTextureLocation() {
            return POWER_LOCATION;
        }

        protected ChaosCreeperRenderer.Model model() {
            return this.model;
        }
    }

    public static class Model extends EntityModel<TintedRenderState.Creeper> {
        private final ModelPart head;
        private final ModelPart rightHindLeg;
        private final ModelPart leftHindLeg;
        private final ModelPart rightFrontLeg;
        private final ModelPart leftFrontLeg;
        private static final int Y_OFFSET = 6;

        public Model(ModelPart root) {
            super(root);
            this.head = root.getChild("head");
            this.leftHindLeg = root.getChild("right_hind_leg");
            this.rightHindLeg = root.getChild("left_hind_leg");
            this.leftFrontLeg = root.getChild("right_front_leg");
            this.rightFrontLeg = root.getChild("left_front_leg");
        }

        public static LayerDefinition createBodyLayer(CubeDeformation cubeDeformation) {
            MeshDefinition meshdefinition = new MeshDefinition();
            PartDefinition partdefinition = meshdefinition.getRoot();
            partdefinition.addOrReplaceChild(
                    "head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, cubeDeformation), PartPose.offset(0.0F, 6.0F, 0.0F)
            );
            partdefinition.addOrReplaceChild(
                    "body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, cubeDeformation), PartPose.offset(0.0F, 6.0F, 0.0F)
            );
            CubeListBuilder cubelistbuilder = CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, cubeDeformation);
            partdefinition.addOrReplaceChild("right_hind_leg", cubelistbuilder, PartPose.offset(-2.0F, 18.0F, 4.0F));
            partdefinition.addOrReplaceChild("left_hind_leg", cubelistbuilder, PartPose.offset(2.0F, 18.0F, 4.0F));
            partdefinition.addOrReplaceChild("right_front_leg", cubelistbuilder, PartPose.offset(-2.0F, 18.0F, -4.0F));
            partdefinition.addOrReplaceChild("left_front_leg", cubelistbuilder, PartPose.offset(2.0F, 18.0F, -4.0F));
            return LayerDefinition.create(meshdefinition, 64, 32);
        }

        public void setupAnim(TintedRenderState.Creeper renderState) {
            super.setupAnim(renderState);
            this.head.yRot = renderState.yRot * (float) (Math.PI / 180.0);
            this.head.xRot = renderState.xRot * (float) (Math.PI / 180.0);
            float f = renderState.walkAnimationSpeed;
            float f1 = renderState.walkAnimationPos;
            this.rightHindLeg.xRot = Mth.cos(f1 * 0.6662F) * 1.4F * f;
            this.leftHindLeg.xRot = Mth.cos(f1 * 0.6662F + (float) Math.PI) * 1.4F * f;
            this.rightFrontLeg.xRot = Mth.cos(f1 * 0.6662F + (float) Math.PI) * 1.4F * f;
            this.leftFrontLeg.xRot = Mth.cos(f1 * 0.6662F) * 1.4F * f;
        }
    }
    //?}
}
