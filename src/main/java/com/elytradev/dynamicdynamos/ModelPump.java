package com.elytradev.dynamicdynamos;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

//Made with Blockbench 3.8.4
public class ModelPump extends Model {

	private final ModelRenderer hack;
	private final ModelRenderer west_r1;
	private final ModelRenderer north_r1;
	private final ModelRenderer east_r1;
	private final ModelRenderer south_r1;
	private final ModelRenderer bottom_r1;

	public ModelPump() {
		super(RenderType::getEntityCutout);
		textureWidth = 20;
		textureHeight = 20;

		hack = new ModelRenderer(this);
		hack.setRotationPoint(8.0F, 24.0F, -8.0F);
		hack.setTextureOffset(-5, 5).addBox(-13.0F, -5.0F, 3.0F, 10.0F, 0.0F, 10.0F, 0.0F, true);

		west_r1 = new ModelRenderer(this);
		west_r1.setRotationPoint(-8.0F, 0.0F, 8.0F);
		hack.addChild(west_r1);
		setRotationAngle(west_r1, 1.5708F, 0.0F, 1.5708F);
		west_r1.setTextureOffset(3, 5).addBox(-4.0F, -5.0F, 5.0F, 1.0F, 10.0F, 0.0F, 0.0F, false);
		west_r1.setTextureOffset(3, 5).addBox(-5.0F, -5.0F, 5.0F, 1.0F, 10.0F, 0.0F, 0.0F, false);

		north_r1 = new ModelRenderer(this);
		north_r1.setRotationPoint(-8.0F, 0.0F, 8.0F);
		hack.addChild(north_r1);
		setRotationAngle(north_r1, 3.1416F, 0.0F, 1.5708F);
		north_r1.setTextureOffset(3, 5).addBox(-4.0F, -5.0F, 5.0F, 1.0F, 10.0F, 0.0F, 0.0F, false);
		north_r1.setTextureOffset(3, 5).addBox(-5.0F, -5.0F, 5.0F, 1.0F, 10.0F, 0.0F, 0.0F, false);

		east_r1 = new ModelRenderer(this);
		east_r1.setRotationPoint(-8.0F, 0.0F, 8.0F);
		hack.addChild(east_r1);
		setRotationAngle(east_r1, -1.5708F, 0.0F, 1.5708F);
		east_r1.setTextureOffset(3, 5).addBox(-4.0F, -5.0F, 5.0F, 1.0F, 10.0F, 0.0F, 0.0F, false);
		east_r1.setTextureOffset(3, 5).addBox(-5.0F, -5.0F, 5.0F, 1.0F, 10.0F, 0.0F, 0.0F, false);

		south_r1 = new ModelRenderer(this);
		south_r1.setRotationPoint(-8.0F, 0.0F, 8.0F);
		hack.addChild(south_r1);
		setRotationAngle(south_r1, 0.0F, 0.0F, 1.5708F);
		south_r1.setTextureOffset(3, 5).addBox(-4.0F, -5.0F, 5.0F, 1.0F, 10.0F, 0.0F, 0.0F, false);
		south_r1.setTextureOffset(3, 5).addBox(-5.0F, -5.0F, 5.0F, 1.0F, 10.0F, 0.0F, 0.0F, false);

		bottom_r1 = new ModelRenderer(this);
		bottom_r1.setRotationPoint(-8.0F, -3.0F, 8.0F);
		hack.addChild(bottom_r1);
		setRotationAngle(bottom_r1, 3.1416F, 0.0F, 0.0F);
		bottom_r1.setTextureOffset(-5, 5).addBox(-5.0F, 0.0F, -5.0F, 10.0F, 0.0F, 10.0F, 0.0F, true);
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		hack.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

}
