package com.denger.client.another.hooks.forge.even.addevents;

import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraftforge.eventbus.api.Event;

public class RenderEntityEvent extends Event {

    private final Entity target;
    private final Framebuffer framebuffer;


    public RenderEntityEvent(Framebuffer framebuffer, Entity target) {
        this.framebuffer = framebuffer;
        this.target = target;
    }


    public Entity getTarget() {
        return target;
    }

    public Framebuffer getFramebuffer() {
        return framebuffer;
    }

    public static enum State {
        Pre,
        Post
    }
}
