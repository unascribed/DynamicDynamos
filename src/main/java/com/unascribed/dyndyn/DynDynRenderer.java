package com.unascribed.dyndyn;

import java.util.Map;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Maps;

import cofh.core.render.IconRegistry;
import cofh.thermalexpansion.block.dynamo.TileDynamoBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

public class DynDynRenderer extends TileEntitySpecialRenderer {
	private ModelPump model = new ModelPump();
	
	private Map<String, ResourceLocation> resLocCache = Maps.newHashMap();
	
	@Override
	public void renderTileEntityAt(TileEntity te, double xD, double yD, double zD, float partialTicks) {
		if (te == null) return;
		try {
			TileDynamoBase dyn = (TileDynamoBase)te;
			float speed;
			if (!ClientProxy.stroke.containsKey(dyn)) return;
			if (ClientProxy.energyPerTick.containsKey(dyn)) {
				speed = ClientProxy.energyPerTick.get(dyn).floatValue()/80f;
			} else {
				speed = 0;
			}
			
			float x = (float)xD;
			float y = (float)yD;
			float z = (float)zD;
			
			
			float pumpX = 0f;
			float pumpY = 0f;
			float pumpZ = -0.25f;
			
			float stroke = (MathHelper.sin((ClientProxy.stroke.get(dyn).floatValue()+(partialTicks*speed))/10f)+1f)/2f;
			
			pumpZ -= (stroke)*(0.245f);
			
			GL11.glPushMatrix();
				GL11.glTranslatef(x+0.5f, y+0.5f, z+0.5f);
				switch (ForgeDirection.getOrientation(dyn.getFacing())) {
					default:
					case NORTH:
						break;
					case WEST:
						GL11.glRotatef(90f, 0, 1, 0);
						break;
					case SOUTH:
						GL11.glRotatef(180f, 0, 1, 0);
						break;
					case EAST:
						GL11.glRotatef(270f, 0, 1, 0);
						break;
					case UP:
						GL11.glRotatef(90f, 1, 0, 0);
						break;
					case DOWN:
						GL11.glRotatef(270f, 1, 0, 0);
						break;
				}
				GL11.glTranslatef(pumpX-(5/16f), pumpY-(5/16f), pumpZ);
				String name = IconRegistry.getIcon("Dynamo", dyn.getType()).getIconName();
				if (!resLocCache.containsKey(name)) {
					int idx = name.indexOf(':');
					if (idx == -1) idx = 0;
					String modid = name.substring(0, idx);
					String path = "textures/blocks/"+name.substring(idx+1)+".png";
					resLocCache.put(name, new ResourceLocation(modid, path));
				}
				ResourceLocation loc = resLocCache.get(name);
				Minecraft.getMinecraft().renderEngine.bindTexture(loc);
				model.render(null, 0, 0, 0, 0, 0, partialTicks);
			GL11.glPopMatrix();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

}
