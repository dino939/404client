package com.denger.client.another.hooks.forge.even.addevents;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.Event;

public class Event2D extends Event {
    private MatrixStack ms = new MatrixStack();

    public MatrixStack getMs() {

        return ms;
    }
}
