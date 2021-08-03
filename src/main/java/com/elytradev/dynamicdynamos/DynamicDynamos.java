package com.elytradev.dynamicdynamos;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.MapMaker;

import cofh.thermal.lib.tileentity.DynamoTileBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.event.world.ChunkWatchEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

@Mod("dynamicdynamos")
public class DynamicDynamos {
	
	public static <K, V> Map<K, V> newWeakIdentityHashMap() {
		return new MapMaker().weakKeys().concurrencyLevel(1).makeMap();
	}
	
	public static Logger log;
	public static SimpleChannel network;
	
	public DynamicDynamos() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onSetup);
		MinecraftForge.EVENT_BUS.addListener(this::onTick);
		MinecraftForge.EVENT_BUS.addListener(this::onWatch);
	}
	
	public void onSetup(FMLCommonSetupEvent e) {
		log = LogManager.getLogger("DynamicDynamos");
		network = NetworkRegistry.newSimpleChannel(new ResourceLocation("dynamicdynamos", "main"),
				() -> "0", (v) -> true, (v) -> true);
		network.messageBuilder(UpdateDynamoEnergyRate.class, 0, NetworkDirection.PLAY_TO_CLIENT)
			.decoder((buf) -> new UpdateDynamoEnergyRate(buf.readBlockPos(), buf.readVarInt()))
			.encoder((uder, buf) -> buf.writeBlockPos(uder.pos).writeVarInt(uder.energyPerTick))
			.consumer((pkt, ctx) -> {
				DynDynClient.processPacket(pkt, ctx);
			})
			.add();
		DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> DynDynClient::setup);
	}
	
	private Map<DynamoTileBase, Integer> lastTickRate = newWeakIdentityHashMap();
	
	public void onTick(WorldTickEvent e) {
		if (e.phase == Phase.END && e.world instanceof ServerWorld) {
			ServerWorld sw = (ServerWorld)e.world;
			for (TileEntity te : sw.loadedTileEntityList) {
				if (te instanceof DynamoTileBase) {
					DynamoTileBase tdb = (DynamoTileBase)te;
					if (lastTickRate.containsKey(te)) {
						if (lastTickRate.get(te).intValue() != tdb.getCurSpeed()) {
							UpdateDynamoEnergyRate msg = new UpdateDynamoEnergyRate(tdb.pos(), tdb.getCurSpeed());
							Chunk c = sw.getChunkAt(tdb.pos());
							sw.getChunkProvider().chunkManager.getTrackingPlayers(c.getPos(), false)
								.forEach(spe -> network.send(PacketDistributor.PLAYER.with(() -> spe), msg));
						}
					}
					lastTickRate.put(tdb, tdb.getCurSpeed());
				}
			}
		}
	}
	
	public void onWatch(ChunkWatchEvent.Watch e) {
		for (TileEntity te : e.getWorld().getChunk(e.getPos().x, e.getPos().z).getTileEntityMap().values()) {
			if (te instanceof DynamoTileBase) {
				DynamoTileBase tdb = (DynamoTileBase)te;
				e.getWorld().getServer().deferTask(() -> {
					UpdateDynamoEnergyRate msg = new UpdateDynamoEnergyRate(tdb.pos(), tdb.getCurSpeed());
					network.send(PacketDistributor.PLAYER.with(e::getPlayer), msg);
				});
			}
		}
	}
	
}
