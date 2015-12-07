package com.unascribed.dyndyn;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.mutable.MutableFloat;

import cofh.thermalexpansion.block.dynamo.TileDynamoBase;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;

public class ClientProxy extends Proxy {

	public static Map<TileDynamoBase, MutableFloat> stroke = new WeakIdentityHashMap<>();
	
	@Override
	public void init() {
		super.init();
		DynamicDynamos.inst.log.info("Registering dynamo renderer");
		ClientRegistry.bindTileEntitySpecialRenderer(TileDynamoBase.class, new DynDynRenderer());
	}
	
	@SubscribeEvent
	public void onTick(ClientTickEvent e) {
		if (e.phase == Phase.START && Minecraft.getMinecraft().theWorld != null && !Minecraft.getMinecraft().isGamePaused()) {
			for (TileEntity te : (List<TileEntity>)Minecraft.getMinecraft().theWorld.loadedTileEntityList) {
				if (te instanceof TileDynamoBase) {
					TileDynamoBase tdb = (TileDynamoBase)te;
					float speed = tdb.getInfoEnergyPerTick()/80f;
					if (!stroke.containsKey(tdb)) {
						stroke.put(tdb, new MutableFloat((te.xCoord*89)*te.yCoord+(te.zCoord*67)));
					}
					stroke.get(tdb).add(speed);
				}
			}
		}
	}

}
