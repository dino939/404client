package com.denger.client.another.settings.sett;

import com.denger.client.another.settings.Setting;

public class BoolSetting extends Setting {
    private Runnable on;
    private Runnable off;
    private boolean state = false;

    public BoolSetting setBol(boolean state) {
        if (state == this.state) return this;
        this.state = state;
        if (state) {
            if (on == null ) return this;
            on.run();
        } else {
            if (off == null ) return this;
            off.run();
        }
        return this;
    }
    public BoolSetting setOn(Runnable runnable){
        on = runnable;
        return this;
    }
    public BoolSetting setOff(Runnable runnable){
        off = runnable;
        return this;
    }
    public boolean getState() {
        return state;
    }

    public void toggle(){
        setBol(!state);
    }
}
