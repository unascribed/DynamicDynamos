package com.elytradev.dynamicdynamos;

import java.util.Map;

import com.google.common.collect.Maps;

import cofh.thermalexpansion.block.dynamo.TileDynamoBase;
import cofh.thermalexpansion.init.TETextures;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class DynDynRenderer extends TileEntitySpecialRenderer<TileDynamoBase> {
	private ModelPump model = new ModelPump();
	
	private Map<String, ResourceLocation> resLocCache = Maps.newHashMap();
	
	@Override
	public void render(TileDynamoBase dyn, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		if (dyn == null) return;
		try {
			float speed;
			if (!ClientProxy.stroke.containsKey(dyn)) return;
			if (ClientProxy.energyPerTick.containsKey(dyn)) {
				speed = ClientProxy.energyPerTick.get(dyn).floatValue()/80f;
			} else {
				speed = 0;
			}
			
			float pumpX = 0f;
			float pumpY = 0f;
			float pumpZ = -0.25f;
			
			float stroke = (MathHelper.sin((ClientProxy.stroke.get(dyn).floatValue()+(partialTicks*speed))/10f)+1f)/2f;
			
			pumpZ -= (stroke)*(0.245f);
			
			GlStateManager.pushMatrix();
				GlStateManager.translate(x+0.5, y+0.5, z+0.5);
				switch (EnumFacing.getFront(dyn.getFacing())) {
					default:
					case NORTH:
						break;
					case WEST:
						GlStateManager.rotate(90f, 0, 1, 0);
						break;
					case SOUTH:
						GlStateManager.rotate(180f, 0, 1, 0);
						break;
					case EAST:
						GlStateManager.rotate(270f, 0, 1, 0);
						break;
					case UP:
						GlStateManager.rotate(90f, 1, 0, 0);
						break;
					case DOWN:
						GlStateManager.rotate(270f, 1, 0, 0);
						break;
				}
				GlStateManager.translate(pumpX-(5/16f), pumpY-(5/16f), pumpZ);
				TextureAtlasSprite tas = TETextures.DYNAMO[dyn.getType()];
				String[] split = tas.getIconName().split(":");
				Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(split[0], "textures/"+split[1]+".png"));
				model.render(null, 0, 0, 0, 0, 0, partialTicks);
			GlStateManager.popMatrix();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

}
