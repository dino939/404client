package com.denger.client.another.hooks.forge.even.addevents;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraftforge.eventbus.api.Event;

public class LayerEvent extends Event {
    private MatrixStack ms;
    private final float partialTicks;
    AbstractClientPlayerEntity player;
    public LayerEvent(MatrixStack ms, float p_228426_2_, AbstractClientPlayerEntity p_228426_6_){
        this.ms = ms;
        this.partialTicks = p_228426_2_;
        this.player = p_228426_6_;

    }

    public MatrixStack getMatrixStack() {
        return ms;
    }

    public AbstractClientPlayerEntity getPlayer() {
        return player;
    }

    public float getPartialTicks() {
        return partialTicks;
    }
}
