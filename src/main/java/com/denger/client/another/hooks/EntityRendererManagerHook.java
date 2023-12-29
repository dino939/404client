package com.denger.client.another.hooks;

import com.denger.client.another.hooks.forge.even.addevents.RenderEntityEvent;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraftforge.common.MinecraftForge;

public class EntityRendererManagerHook extends EntityRendererManager {
    public EntityRendererManagerHook(TextureManager p_i226034_1_, ItemRenderer p_i226034_2_, IReloadableResourceManager p_i226034_3_, FontRenderer p_i226034_4_, GameSettings p_i226034_5_) {
        super(p_i226034_1_, p_i226034_2_, p_i226034_3_, p_i226034_4_, p_i226034_5_);
    }

    @Override
    public <E extends Entity> void render(E p_229084_1_, double p_229084_2_, double p_229084_4_, double p_229084_6_, float p_229084_8_, float p_229084_9_, MatrixStack p_229084_10_, IRenderTypeBuffer p_229084_11_, int p_229084_12_) {
        System.out.println(1);
    }
}
