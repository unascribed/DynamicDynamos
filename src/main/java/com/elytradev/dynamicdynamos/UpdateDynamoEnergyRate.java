package com.elytradev.dynamicdynamos;

import org.apache.commons.lang3.mutable.MutableInt;

import cofh.thermalexpansion.block.dynamo.TileDynamoBase;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public final class UpdateDynamoEnergyRate {
	public static class Message implements IMessage {
		public BlockPos pos;
		public int energyPerTick;
		
		@Override
		public void fromBytes(ByteBuf buf) {
			pos = BlockPos.fromLong(buf.readLong());
			energyPerTick = ByteBufUtils.readVarInt(buf, 5);
		}

		@Override
		public void toBytes(ByteBuf buf) {
			buf.writeLong(pos.toLong());
			ByteBufUtils.writeVarInt(buf, energyPerTick, 5);
		}
		
	}
	
	public static class Handler implements IMessageHandler<Message, IMessage> {

		@Override
		public IMessage onMessage(Message message, MessageContext ctx) {
			TileEntity te = Minecraft.getMinecraft().world.getTileEntity(message.pos);
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
