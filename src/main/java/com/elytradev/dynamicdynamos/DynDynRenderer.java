package com.elytradev.dynamicdynamos;

import com.mojang.blaze3d.matrix.MatrixStack;

import cofh.lib.util.constants.Constants;
import cofh.thermal.lib.block.TileBlockDynamo;
import cofh.thermal.lib.tileentity.DynamoTileBase;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.model.data.EmptyModelData;

public class DynDynRenderer extends TileEntityRenderer<DynamoTileBase> {
	
	public DynDynRenderer(TileEntityRendererDispatcher dispatcher) {
		super(dispatcher);
	}

	private ModelPump pumpModel = new ModelPump();

	@Override
	public void render(DynamoTileBase dyn, float partialTicks, MatrixStack matrices, IRenderTypeBuffer buf, int light, int overlay) {
		if (dyn == null) return;
		try {
			float speed;
			if (DynDynClient.energyPerTick.containsKey(dyn)) {
				speed = DynDynClient.energyPerTick.get(dyn).floatValue()/80f;
			} else {
				speed = 0;
			}
			BlockState bs = dyn.getBlockState();
			if (!(bs.getBlock() instanceof TileBlockDynamo)) return;
			Direction dir = bs.get(Constants.FACING_ALL);
			
			TextureAtlasSprite tas = null;
			BlockState fakeState = bs.with(Constants.FACING_ALL, Direction.UP);
			IBakedModel model = Minecraft.getInstance().getModelManager().getBlockModelShapes().getModel(fakeState);
			for (BakedQuad bq : model.getQuads(bs, null, dyn.world().rand, EmptyModelData.INSTANCE)) {
				if (bq.getFace() == Direction.UP) {
					tas = bq.getSprite();
					break;
				}
			}
			if (tas == null) return;


			float pumpX = 0;
			float pumpY = -0.825f;
			float pumpZ = 0;

			float stroke = DynDynClient.stroke.containsKey(dyn) ? (MathHelper.sin((DynDynClient.stroke.get(dyn).floatValue()+(partialTicks*speed))/10f)+1f)/2f : 0;

			pumpY -= (stroke)*(0.245f);

			matrices.push();
				matrices.translate(0.5, 0.5, 0.5);
				switch (dir) {
					default:
					case UP:
						break;
					case DOWN:
						matrices.rotate(Vector3f.XP.rotationDegrees(180));
						break;
					case NORTH:
						matrices.rotate(Vector3f.XN.rotationDegrees(90));
						break;
					case EAST:
						matrices.rotate(Vector3f.ZN.rotationDegrees(90));
						break;
					case SOUTH:
						matrices.rotate(Vector3f.XP.rotationDegrees(90));
						break;
					case WEST:
						matrices.rotate(Vector3f.ZP.rotationDegrees(90));
						break;
				}
				matrices.translate(pumpX, pumpY, pumpZ);

				pumpModel.render(matrices, buf.getBuffer(RenderType.getEntityCutout(new ResourceLocation(tas.getName().getNamespace(), "textures/"+tas.getName().getPath()+".png"))),
						light, overlay, 1, 1, 1, 1);
			matrices.pop();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

}
