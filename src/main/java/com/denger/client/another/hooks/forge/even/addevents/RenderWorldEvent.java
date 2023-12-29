package com.denger.client.another.hooks.forge.even.addevents;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraftforge.eventbus.api.Event;

public class RenderWorldEvent extends Event {
    private MatrixStack ms;
    private float field;
    ActiveRenderInfo info;
    public RenderWorldEvent(MatrixStack ms, float p_228426_2_, ActiveRenderInfo p_228426_6_){
        this.ms = ms;
        this.field = p_228426_2_;
        this.info = p_228426_6_;

    }

    public MatrixStack getMs() {
        return ms;
    }

    public ActiveRenderInfo getInfo() {
        return info;
    }

    public float getField() {
        return field;
    }
}
