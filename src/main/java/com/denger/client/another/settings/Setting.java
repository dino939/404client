package com.denger.client.another.settings;

import com.denger.client.another.settings.sett.BoolSetting;
import com.denger.client.another.settings.sett.ModSetting;
import com.denger.client.modules.Module;

import java.util.function.Supplier;

public abstract class Setting {
    private String name;
    private Supplier<Boolean> visible = ()-> true;
    private Module module;

    public Setting() {
    }


    public void setModule(Module module) {
        this.module = module;
    }

    public Setting setName(String name) {
        this.name = name;
        return this;
    }

    public Setting setVisible(Supplier<Boolean> visible) {
        this.visible = visible;
        return this;
    }



    public String getName() {
        return name;
    }

    public Module getModule() {
        return module;
    }

    public boolean getVisible() {
        return visible.get();
    }
}
