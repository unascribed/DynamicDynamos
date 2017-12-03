package com.elytradev.dynamicdynamos;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.mutable.MutableFloat;
import org.apache.commons.lang3.mutable.MutableInt;

import cofh.thermalexpansion.block.dynamo.TileDynamoBase;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;

public class ClientProxy extends Proxy {

	public static Map<TileDynamoBase, MutableFloat> stroke = new WeakIdentityHashMap<>();
	public static Map<TileDynamoBase, MutableInt> energyPerTick = new WeakIdentityHashMap<>();
	
	@Override
	public void init() {
		super.init();
		DynamicDynamos.inst.log.info("Registering dynamo renderer");
		ClientRegistry.bindTileEntitySpecialRenderer(TileDynamoBase.class, new DynDynRenderer());
	}
	
	@SubscribeEvent
	public void onTick(ClientTickEvent e) {
		if (e.phase == Phase.START && Minecraft.getMinecraft().world != null && !Minecraft.getMinecraft().isGamePaused()) {
			for (TileEntity te : Minecraft.getMinecraft().world.loadedTileEntityList) {
				if (te instanceof TileDynamoBase) {
					TileDynamoBase tdb = (TileDynamoBase)te;
					float speed = energyPerTick.containsKey(tdb) ? energyPerTick.get(tdb).floatValue()/80f : 0;
					if (!stroke.containsKey(tdb)) {
						stroke.put(tdb, new MutableFloat(((te.getPos().getX()*89)*te.getPos().getY()+(te.getPos().getZ()*67))%(Math.PI*20)));
					}
					stroke.get(tdb).add(speed);
				}
			}
		}
	}

}
