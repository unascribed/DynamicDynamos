package com.elytradev.dynamicdynamos;

import net.minecraft.client.model.*;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModelPump extends ModelBase {

	//public ModelRenderer pump = new ModelRenderer(this).setTextureSize(64, 64).setTextureOffset(17, 1).addBox(0.0F, 0.0F, 0.0F, 10, 10, 2);
	public ModelRenderer pump = new ModelRenderer(this).setTextureSize(64, 64);

	public ModelPump()
	{
		pump.cubeList.add(new CustomBox(pump));
	}

	@Override
	public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale) {
		pump.render(1/16f);
	}

	class CustomBox extends ModelBox
	{
		private final PositionTextureVertex[] vertexPositions;
		private final TexturedQuad[] quadList;
		CustomBox(ModelRenderer renderer)
		{
			super(renderer,0,0,0f,0f,0f,0,0,0,0f,false);

			int texU = 17;
			int texV = 1;
			int dx = 10;
			int dy = 10;
			int dz = 2;

			float f = (float)dx;
			float f1 = (float)dy;
			float f2 = (float)dz;

			this.vertexPositions = new PositionTextureVertex[8];
			this.quadList = new TexturedQuad[6];
			PositionTextureVertex positiontexturevertex7 = new PositionTextureVertex(0, 0, 0, 0.0F, 0.0F);
			PositionTextureVertex positiontexturevertex = new PositionTextureVertex(f, 0, 0, 0.0F, 8.0F);
			PositionTextureVertex positiontexturevertex1 = new PositionTextureVertex(f, f1, 0, 8.0F, 8.0F);
			PositionTextureVertex positiontexturevertex2 = new PositionTextureVertex(0, f1, 0, 8.0F, 0.0F);
			PositionTextureVertex positiontexturevertex3 = new PositionTextureVertex(0, 0, f2, 0.0F, 0.0F);
			PositionTextureVertex positiontexturevertex4 = new PositionTextureVertex(f, 0, f2, 0.0F, 8.0F);
			PositionTextureVertex positiontexturevertex5 = new PositionTextureVertex(f, f1, f2, 8.0F, 8.0F);
			PositionTextureVertex positiontexturevertex6 = new PositionTextureVertex(0, f1, f2, 8.0F, 0.0F);
			this.vertexPositions[0] = positiontexturevertex7;
			this.vertexPositions[1] = positiontexturevertex;
			this.vertexPositions[2] = positiontexturevertex1;
			this.vertexPositions[3] = positiontexturevertex2;
			this.vertexPositions[4] = positiontexturevertex3;
			this.vertexPositions[5] = positiontexturevertex4;
			this.vertexPositions[6] = positiontexturevertex5;
			this.vertexPositions[7] = positiontexturevertex6;

			int topU1 = texU + dz;
			int topV1 = texV + dz;
			int topU2 = texU + dz + dx;
			int topV2 = texV + dz + dy;

			int sid1U1 = texU;
			int sid1V1 = topV1;
			int sid1U2 = texU + dz;
			int sid1V2 = topV2;

			int sid2U1 = topU1;
			int sid2V1 = texV;
			int sid2U2 = topU2;
			int sid2V2 = texV + dz;

			this.quadList[0] = new TexturedQuad(new PositionTextureVertex[] {positiontexturevertex4, positiontexturevertex, positiontexturevertex1, positiontexturevertex5},  sid1U1, sid1V1, sid1U2, sid1V2, renderer.textureWidth, renderer.textureHeight);
			this.quadList[1] = new TexturedQuad(new PositionTextureVertex[] {positiontexturevertex7, positiontexturevertex3, positiontexturevertex6, positiontexturevertex2}, sid1U2, sid1V1, sid1U1, sid1V2, renderer.textureWidth, renderer.textureHeight);
			this.quadList[2] = new TexturedQuad(new PositionTextureVertex[] {positiontexturevertex4, positiontexturevertex3, positiontexturevertex7, positiontexturevertex},  sid2U1, sid2V2, sid2U2, sid2V1, renderer.textureWidth, renderer.textureHeight);
			this.quadList[3] = new TexturedQuad(new PositionTextureVertex[] {positiontexturevertex1, positiontexturevertex2, positiontexturevertex6, positiontexturevertex5}, sid2U1, sid2V1, sid2U2, sid2V2, renderer.textureWidth, renderer.textureHeight);
			this.quadList[4] = new TexturedQuad(new PositionTextureVertex[] {positiontexturevertex, positiontexturevertex7, positiontexturevertex2, positiontexturevertex1},  topU1, topV1, topU2, topV2, renderer.textureWidth, renderer.textureHeight);
			this.quadList[5] = new TexturedQuad(new PositionTextureVertex[] {positiontexturevertex3, positiontexturevertex4, positiontexturevertex5, positiontexturevertex6}, topU1, topV1, topU2, topV2, renderer.textureWidth, renderer.textureHeight);

		}

		@SideOnly(Side.CLIENT)
		public void render(BufferBuilder renderer, float scale)
		{
			for (TexturedQuad texturedquad : this.quadList)
			{
				texturedquad.draw(renderer, scale);
			}
		}
	}
}
