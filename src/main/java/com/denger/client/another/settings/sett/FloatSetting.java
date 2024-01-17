package com.denger.client.another.settings.sett;

import com.denger.client.another.settings.Setting;
import com.denger.client.utils.MathUtils;

public class FloatSetting extends Setting{
    private float val = 0;
    private float min = 0;
    private float max = 0;
    private String type = "";
    private Runnable runnable = ()->{};
    public FloatSetting setVal(float val) {
        this.val = MathUtils.clamp(val,min,max);
        runnable.run();
        return this;
    }

    public FloatSetting setMin(float min) {
        this.min = min;
        return this;
    }

    public FloatSetting setMax(float max) {
        this.max = max;
        return this;
    }
    public FloatSetting seType(String type) {
        this.type = type;
        return this;
    }

    public FloatSetting setRunnable(Runnable runnable) {
        this.runnable = runnable;
        return this;
    }

    public float getMin() {
        return min;
    }

    public float getMax() {
        return max;
    }

    public float getVal() {
        return val;
    }

    public String getType() {
        return type;
    }
}
