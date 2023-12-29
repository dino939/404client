package com.denger.client.utils;

import net.minecraft.client.Minecraft;

public class AnimationUtils {
    long mc;
    float anim;
    public float to;
    public float speed;
    Type type;

    public AnimationUtils(float anim, float to, float speed) {
        this.anim = anim;
        this.to = to;
        this.speed = speed;
        this.mc = System.currentTimeMillis();
        this.type = Type.HARP;
    }

    public AnimationUtils(float anim, float to, float speed, Type type) {
        this.anim = anim;
        this.to = to;
        this.speed = speed;
        this.mc = System.currentTimeMillis();
        this.type = type;
    }

    public static float harp(float val, float current, float speed) {
        float emi = (current - val) * (speed / 2.0f) > 0.0f ? Math.max(speed, Math.min(current - val, (current - val) * (speed / 2.0f))) : Math.max(current - val, Math.min(-(speed / 2.0f), (current - val) * (speed / 2.0f)));
        return val + emi;
    }


    public static double deltaTime() {
        float fps = Integer.parseInt(Minecraft.getInstance().fpsString.split(" ")[0]);
        return fps > 0 ? (1.0000 / fps) : 1;
    }
    public static float fast(float end, float start, float multiple) {
        return (1 - MathUtils.clamp((float) (deltaTime() * multiple), 0, 1)) * end + MathUtils.clamp((float) (deltaTime() * multiple), 0, 1) * start;
    }

    public float getAnim() {
        int count = (int)((System.currentTimeMillis() - this.mc) / 5L);
        if (count > 0) {
            this.mc = System.currentTimeMillis();
        }
        for (int i = 0; i < count; ++i) {
            if (this.type == Type.HARP) {
                this.anim = AnimationUtils.harp(this.anim, this.to, this.speed);
                continue;
            }
            if (this.type == Type.FAST) {
                this.anim = AnimationUtils.fast(this.anim, this.to, speed);
                continue;
            }
            if (this.type != Type.SIMPLE) continue;
            if (this.anim > this.to) {
                this.anim -= this.speed;
            } else if (this.anim < this.to) {
                this.anim += this.speed;
            }
            if (!(Math.abs(this.to - this.anim) < this.speed)) continue;
            this.anim = this.to;
        }
        return MathUtils.roundToDecimal(anim, 10);
    }

    public void reset() {
        this.mc = System.currentTimeMillis();
    }

    public void setAnim(float anim) {
        this.anim = anim;
        this.mc = System.currentTimeMillis();
    }
}