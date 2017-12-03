package com.elytradev.dynamicdynamos;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.google.common.collect.Lists;

import cofh.thermalexpansion.block.dynamo.TileDynamoBase;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ChunkWatchEvent;

public abstract class Proxy {
	public void init() {
		DynamicDynamos.inst.log.info("Registering dynamo information syncer");
		FMLCommonHandler.instance().bus().register(this);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	private Map<TileDynamoBase, Integer> lastTickRate = new WeakIdentityHashMap<>();
	private List<Runnable> doLater = Lists.newArrayList();
	
	@SubscribeEvent
	public void onTick(ServerTickEvent e) {
		if (e.phase == Phase.END) {
			Iterator<Runnable> itr = doLater.iterator();
			while (itr.hasNext()) {
				Runnable r = itr.next();
				r.run();
				itr.remove();
			}
			for (WorldServer w : DimensionManager.getWorlds()) {
				for (TileEntity te : w.loadedTileEntityList) {
					if (te instanceof TileDynamoBase) {
						TileDynamoBase tdb = (TileDynamoBase)te;
						if (lastTickRate.containsKey(te)) {
							if (lastTickRate.get(te).intValue() != tdb.getInfoEnergyPerTick()) {
								UpdateDynamoEnergyRate.Message msg = new UpdateDynamoEnergyRate.Message();
								msg.pos = tdb.getPos();
								msg.energyPerTick = tdb.getInfoEnergyPerTick();
								Chunk c = w.getChunkFromBlockCoords(te.getPos());
								for (EntityPlayer ep : w.playerEntities) {
									if (ep instanceof EntityPlayerMP) {
										EntityPlayerMP mp = (EntityPlayerMP)ep;
										if (w.getPlayerChunkMap().isPlayerWatchingChunk(mp, c.x, c.z)) {
											DynamicDynamos.inst.network.sendTo(msg, mp);
										}
									}
								}
							}
						}
						lastTickRate.put(tdb, tdb.getInfoEnergyPerTick());
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onChunkWatch(ChunkWatchEvent.Watch e) {
		for (TileEntity te : e.getPlayer().world.getChunkFromChunkCoords(e.getChunk().x, e.getChunk().z).getTileEntityMap().values()) {
			if (te instanceof TileDynamoBase) {
				EntityPlayerMP player = e.getPlayer();
				TileDynamoBase tdb = (TileDynamoBase)te;
				doLater.add(() -> {
					UpdateDynamoEnergyRate.Message msg = new UpdateDynamoEnergyRate.Message();
					msg.pos = tdb.getPos();
					msg.energyPerTick = tdb.getInfoEnergyPerTick();
					DynamicDynamos.inst.network.sendTo(msg, player);
				});
			}
		}
	}
}
