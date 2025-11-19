// Made with Blockbench 4.11.2
package net.lerariemann.infinity.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.lerariemann.infinity.entity.custom.AntEntity;
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
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class AntModel extends EntityModel<AntEntityRenderState> {
	public static final ModelLayerLocation MODEL_LAYER =
		new ModelLayerLocation(InfinityMethods.getId("ant"), "main");

	private final ModelPart body;
    private final ModelPart head;
	private final ModelPart front_left_leg;
	private final ModelPart center_left_leg;
	private final ModelPart back_left_leg;
	private final ModelPart front_right_leg;
	private final ModelPart center_right_leg;
	private final ModelPart back_right_leg;

	public AntModel(ModelPart root) {
        //? if >1.21.2
        super(root);
        this.body = root.getChild("body");
		this.head = body.getChild("head");
		this.front_left_leg = body.getChild("front_left_leg");
		this.center_left_leg = body.getChild("center_left_leg");
		this.back_left_leg = body.getChild("back_left_leg");
		this.front_right_leg = body.getChild("front_right_leg");
		this.center_right_leg = body.getChild("center_right_leg");
		this.back_right_leg = body.getChild("back_right_leg");
	}
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition root = modelData.getRoot();
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 11).addBox(-2.0F, -5.75F, 3.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-1.0F, -4.0F, -5.0F, 2.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(16, 11).addBox(-1.5F, -1.75F, -3.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0)
				.addBox(2.0F, -3.75F, -1.0F, -1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0)
				.addBox(-1.0F, -3.75F, -1.0F, -1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, -3.0F, -4.0F));

		body.addOrReplaceChild("front_left_leg", CubeListBuilder.create()
				.texOffs(16, 17)
				.addBox(-0.5F, -0.5F, -1.5F, 7.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.5F, -2.5F, -1.5F, 0.0F, 0.2182F, 0.3054F));
		body.addOrReplaceChild("center_left_leg", CubeListBuilder.create()
				.texOffs(0, 19)
				.addBox(-0.5F, -0.5F, -0.5F, 7.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.5F, -2.5F, -0.5F, 0.0F, 0.0F, 0.3054F));
		body.addOrReplaceChild("back_left_leg", CubeListBuilder.create()
				.texOffs(16, 19)
				.addBox(-0.5F, -0.5F, -0.5F, 7.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.5F, -2.5F, 1.5F, 0.0F, -0.2182F, 0.3054F));
		body.addOrReplaceChild("front_right_leg", CubeListBuilder.create()
				.texOffs(0, 21)
				.addBox(-6.5F, -0.5F, -0.5F, 7.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.5F, -2.5F, -2.5F, 0.0F, -0.2182F, -0.3054F));
		body.addOrReplaceChild("center_right_leg", CubeListBuilder.create()
				.texOffs(16, 21)
				.addBox(-6.5F, -0.5F, -0.5F, 7.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.5F, -2.5F, -0.5F, 0.0F, 0.0F, -0.3054F));
		body.addOrReplaceChild("back_right_leg", CubeListBuilder.create()
				.texOffs(22, 0)
				.addBox(-6.5F, -0.5F, -0.5F, 7.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.5F, -2.5F, 1.5F, 0.0F, 0.2182F, -0.3054F));

		return LayerDefinition.create(modelData, 64, 64);
	}
    
    //? if =1.21.1 {
    /*@Override
    public void renderToBuffer(PoseStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
        body.render(matrices, vertices, light, overlay, color);
    }
    *///?} else if =1.20.1 {
    /*public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.renderToBuffer(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
    *///?}


    //? if <1.21.2 {
	/*@Override
	public @NotNull ModelPart root() {
		return body;
	}
    *///?}

	@Override
    public void setupAnim(
    //? if <1.21.2 {
    /*T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch
    *///?} else {
    AntEntityRenderState entity
    //?}
    ) {
        //? if >1.21.2 {
        var headYaw = entity.headYaw;
        var headPitch = entity.headPitch;
        var limbAngle = entity.limbAngle;
        var limbDistance = entity.limbDistance;
        //?}
		this.root().getAllParts().forEach(ModelPart::resetPose);

		this.head.yRot = Mth.clamp(headYaw, -30.0F, 30.0F) * 0.017453292F;
		this.head.xRot = Mth.clamp(headPitch, -25.0F, 45.0F) * 0.017453292F;

		float pi = 3.1415927F;
		float speed = Math.min(0.5f, 3.0f * limbDistance);
		float yawFront = Mth.cos(limbAngle * 0.6662F) * speed;
		float yawCenter = Mth.cos(limbAngle * 0.6662F + pi / 4) * speed;
		float yawBack = Mth.cos(limbAngle * 0.6662F + pi / 2) * speed;
		front_left_leg.yRot += yawFront;
		center_left_leg.yRot += yawCenter;
		back_left_leg.yRot += yawBack;
		front_right_leg.yRot -= yawFront;
		center_right_leg.yRot -= yawCenter;
		back_right_leg.yRot -= yawBack;
	}
}