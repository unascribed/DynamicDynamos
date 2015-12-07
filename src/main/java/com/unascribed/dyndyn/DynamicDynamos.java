package com.unascribed.dyndyn;

import org.apache.logging.log4j.Logger;

import cofh.thermalexpansion.ThermalExpansion;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
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
	
	@EventHandler
	public void onPreInit(FMLPreInitializationEvent e) {
		log = e.getModLog();
	}
	
	@EventHandler
	public void onInit(FMLInitializationEvent e) {
		proxy.init();
	}
}
