package com.denger.client.another.hooks.forge.even.addevents;

import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.eventbus.api.Event;

public class EventRayPick extends Event {

   private RayTraceResult result;


    public EventRayPick(RayTraceResult result) {
        this.result = result;
    }

    public void setResult(RayTraceResult result) {
        this.result = result;
    }

    public RayTraceResult getRayTrace() {
        return result;
    }

}
