package com.elytradev.dynamicdynamos;

import java.util.Map;
import java.util.function.Supplier;

import org.apache.commons.lang3.mutable.MutableFloat;
import org.apache.commons.lang3.mutable.MutableInt;

import cofh.thermal.lib.tileentity.DynamoTileBase;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.registry.Registry;

@OnlyIn(Dist.CLIENT)
public class DynDynClient {
	
	public static Map<DynamoTileBase, MutableInt> energyPerTick = DynamicDynamos.newWeakIdentityHashMap();
	public static Map<DynamoTileBase, MutableFloat> stroke = DynamicDynamos.newWeakIdentityHashMap();

	public static void setup() {
		for (TileEntityType<?> tet : Registry.BLOCK_ENTITY_TYPE) {
			if (tet.getRegistryName().getNamespace().equals("thermal") && tet.getRegistryName().getPath().startsWith("dynamo_")) {
				DynamicDynamos.log.info("Registering dynamo renderer for "+tet.getRegistryName());
				ClientRegistry.bindTileEntityRenderer((TileEntityType)tet, DynDynRenderer::new);
			}
		}
		MinecraftForge.EVENT_BUS.addListener(DynDynClient::onTick);
	}
	
	public static void onTick(ClientTickEvent e) {
		if (e.phase == Phase.START && Minecraft.getInstance().world != null && !Minecraft.getInstance().isGamePaused()) {
			for (Map.Entry<DynamoTileBase, MutableInt> en : energyPerTick.entrySet()) {
				DynamoTileBase tdb = en.getKey();
				float speed = en.getValue().floatValue()/80f;
				if (!stroke.containsKey(tdb)) {
					stroke.put(tdb, new MutableFloat(((tdb.pos().getX()*89)*tdb.pos().getY()+(tdb.pos().getZ()*67))%(Math.PI*20)));
				}
				stroke.get(tdb).add(speed);
			}
		}
	}

	public static void processPacket(UpdateDynamoEnergyRate pkt, Supplier<Context> ctx) {
		ctx.get().setPacketHandled(true);
		TileEntity te = Minecraft.getInstance().world.getTileEntity(pkt.pos);
		if (te instanceof DynamoTileBase) {
			DynamoTileBase tdb = (DynamoTileBase)te;
			if (!energyPerTick.containsKey(te)) {
				energyPerTick.put(tdb, new MutableInt(pkt.energyPerTick));
			} else {
				energyPerTick.get(tdb).setValue(pkt.energyPerTick);
			}
		}
	}

}
