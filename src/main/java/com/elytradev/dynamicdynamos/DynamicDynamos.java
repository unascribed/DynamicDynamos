package com.elytradev.dynamicdynamos;

import org.apache.logging.log4j.Logger;

import cofh.thermalexpansion.ThermalExpansion;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;

@Mod(
		modid="dyndyn",
		name="Dynamic Dynamos",
		version="@VERSION@",
		dependencies="required-after:thermalexpansion"
	)
public class DynamicDynamos {
	@Instance("thermalexpansion")
	public static ThermalExpansion te;
	@Instance("dyndyn")
	public static DynamicDynamos inst;
	
	@SidedProxy(clientSide="com.elytradev.dynamicdynamos.ClientProxy", serverSide="com.elytradev.dynamicdynamos.ServerProxy")
	public static Proxy proxy;
	public Logger log;
	public SimpleNetworkWrapper network;
	
	@EventHandler
	public void onPreInit(FMLPreInitializationEvent e) {
		log = e.getModLog();
		network = NetworkRegistry.INSTANCE.newSimpleChannel("dyndyn");
		network.registerMessage(UpdateDynamoEnergyRate.Handler.class, UpdateDynamoEnergyRate.Message.class, 0, Side.CLIENT);
	}
	
	@EventHandler
	public void onInit(FMLInitializationEvent e) {
		proxy.init();
	}
}
