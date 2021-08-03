package com.elytradev.dynamicdynamos;

import net.minecraft.util.math.BlockPos;

public final class UpdateDynamoEnergyRate {
	public BlockPos pos;
	public int energyPerTick;

	public UpdateDynamoEnergyRate(BlockPos pos, int energyPerTick) {
		this.pos = pos;
		this.energyPerTick = energyPerTick;
	}
}
