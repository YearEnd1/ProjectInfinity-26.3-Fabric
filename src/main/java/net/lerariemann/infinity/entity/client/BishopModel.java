package net.lerariemann.infinity.entity.client;

import net.lerariemann.infinity.entity.custom.BishopEntity;
import net.lerariemann.infinity.util.InfinityMethods;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class BishopModel extends HumanoidModel<
        //? if >1.21.2 {
        BishopEntityRenderState
        //?} else {
        /*BishopEntity
        *///?}
        > {
    public static final ModelLayerLocation MODEL_LAYER =
            new ModelLayerLocation(InfinityMethods.getId("bishop"), "main");
    public BishopModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition root = modelData.getRoot();

        root.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(16, 16)
                        .addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, CubeDeformation.NONE),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        root.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, CubeDeformation.NONE)
                        .texOffs(40, 0)
                        .addBox(-3.0F, -10.0F, -3.0F, 6.0F, 3.0F, 6.0F, CubeDeformation.NONE)
                        .texOffs(0, 57)
                        .addBox(-2.0F, -13.0F, -2.0F, 4.0F, 3.0F, 4.0F, CubeDeformation.NONE)
                        .texOffs(0, 0)
                        .addBox(-1.0F, -17.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE)
                        .texOffs(54, 59)
                        .addBox(-1.0F, -19.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.5F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        root.addOrReplaceChild("hat", CubeListBuilder.create()
                        .texOffs(32, 35)
                        .addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        root.addOrReplaceChild("right_arm", CubeListBuilder.create()
                        .texOffs(0, 16)
                        .addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, CubeDeformation.NONE),
                PartPose.offset(-5.0F, 2.0F, 0.0F));
        root.addOrReplaceChild("left_arm", CubeListBuilder.create()
                        .texOffs(16, 48)
                        .addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, CubeDeformation.NONE),
                PartPose.offset(5.0F, 2.0F, 0.0F));
        root.addOrReplaceChild("right_leg", CubeListBuilder.create()
                        .texOffs(0, 16)
                        .addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, CubeDeformation.NONE),
                PartPose.offset(-1.9F, 12.0F, 0.0F));
        root.addOrReplaceChild("left_leg", CubeListBuilder.create()
                        .texOffs(16, 48)
                        .addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, CubeDeformation.NONE),
                PartPose.offset(1.9F, 12.0F, 0.0F));

        return LayerDefinition.create(modelData, 64, 64);
    }
}
