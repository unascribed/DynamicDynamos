package com.unascribed.dyndyn;

import java.util.List;
import java.util.Map;

import cofh.core.network.PacketHandler;
import cofh.thermalexpansion.block.dynamo.TileDynamoBase;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

public abstract class Proxy {
	public void init() {
		DynamicDynamos.inst.log.info("Registering dynamo information syncer");
		FMLCommonHandler.instance().bus().register(this);
	}
	
	private int ticks = 0;
	private Map<TileDynamoBase, Integer> lastTickRate = new WeakIdentityHashMap<>();
	
	@SubscribeEvent
	public void onTick(ServerTickEvent e) {
		if (e.phase == Phase.END) {
			ticks++;
			for (WorldServer w : DimensionManager.getWorlds()) {
				for (TileEntity te : (List<TileEntity>)w.loadedTileEntityList) {
					if (te instanceof TileDynamoBase) {
						TileDynamoBase tdb = (TileDynamoBase)te;
						if (lastTickRate.containsKey(te)) {
							if (lastTickRate.get(te).intValue() != tdb.getInfoEnergyPerTick() || (ticks % 200 == 0)) {
								// FIXME VERY UGLY HAX
								PacketHandler.sendToAllAround(tdb.getGuiPacket(), tdb);
							}
						}
						lastTickRate.put(tdb, tdb.getInfoEnergyPerTick());
					}
				}
			}
		}
	}
}
