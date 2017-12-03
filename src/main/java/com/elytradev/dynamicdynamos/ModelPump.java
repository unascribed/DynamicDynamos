package com.elytradev.dynamicdynamos;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelPump extends ModelBase {
	
	public ModelRenderer pump = new ModelRenderer(this).setTextureSize(64, 64).setTextureOffset(17, 1).addBox(0.0F, 0.0F, 0.0F, 10, 10, 2);
	
	@Override
	public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale) {
		pump.render(1/16f);
	}
}
