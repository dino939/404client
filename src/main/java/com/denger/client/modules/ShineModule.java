package com.denger.client.modules;

import net.minecraftforge.eventbus.api.Event;

public abstract class ShineModule extends Module{

    public abstract void onEventContinuous(Event e);
}
