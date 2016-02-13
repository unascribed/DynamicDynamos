package com.unascribed.dyndyn;

import org.apache.logging.log4j.Logger;

import cofh.thermalexpansion.ThermalExpansion;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;

@Mod(
		modid="dyndyn",
		name="Dynamic Dynamos",
		version="0.1",
		dependencies="required-after:ThermalExpansion"
	)
public class DynamicDynamos {
	@Instance("ThermalExpansion")
	public static ThermalExpansion te;
	@Instance("dyndyn")
	public static DynamicDynamos inst;
	
	@SidedProxy(clientSide="com.unascribed.dyndyn.ClientProxy", serverSide="com.unascribed.dyndyn.ServerProxy")
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
