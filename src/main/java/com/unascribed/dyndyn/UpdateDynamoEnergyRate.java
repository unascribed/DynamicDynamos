package com.unascribed.dyndyn;

import org.apache.commons.lang3.mutable.MutableInt;

import cofh.thermalexpansion.block.dynamo.TileDynamoBase;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;

public final class UpdateDynamoEnergyRate {
	public static class Message implements IMessage {
		public int x;
		public int y;
		public int z;
		public int energyPerTick;
		
		@Override
		public void fromBytes(ByteBuf buf) {
			x = buf.readInt();
			y = buf.readUnsignedByte();
			z = buf.readInt();
			energyPerTick = ByteBufUtils.readVarInt(buf, 5);
		}

		@Override
		public void toBytes(ByteBuf buf) {
			buf.writeInt(x);
			buf.writeByte(y);
			buf.writeInt(z);
			ByteBufUtils.writeVarInt(buf, energyPerTick, 5);
		}
		
	}
	
	public static class Handler implements IMessageHandler<Message, IMessage> {

		@Override
		public IMessage onMessage(Message message, MessageContext ctx) {
			TileEntity te = Minecraft.getMinecraft().theWorld.getTileEntity(message.x, message.y, message.z);
			if (te instanceof TileDynamoBase) {
				TileDynamoBase tdb = (TileDynamoBase)te;
				if (!ClientProxy.energyPerTick.containsKey(te)) {
					ClientProxy.energyPerTick.put(tdb, new MutableInt(message.energyPerTick));
				} else {
					ClientProxy.energyPerTick.get(tdb).setValue(message.energyPerTick);
				}
			}
			return null;
		}
		
	}
	private UpdateDynamoEnergyRate() {}
}
