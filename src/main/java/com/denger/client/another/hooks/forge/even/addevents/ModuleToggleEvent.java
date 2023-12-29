package com.denger.client.another.hooks.forge.even.addevents;

import com.denger.client.modules.Module;
import net.minecraftforge.eventbus.api.Event;

public class ModuleToggleEvent extends Event {
    private boolean State;
    private Module module;

    public ModuleToggleEvent(boolean state, Module moduleToggle) {
        this.State = state;
        this.module = moduleToggle;
    }

    public Module getModule() {
        return module;
    }

    public boolean getState() {
        return State;
    }

    public static class EnableEventModule extends ModuleToggleEvent {
        public EnableEventModule(Module moduleToggle) {
            super(true, moduleToggle);
        }
    }

    public static class DisableEventModule extends ModuleToggleEvent {
        public DisableEventModule(Module moduleToggle) {
            super(false, moduleToggle);
        }
    }
}
