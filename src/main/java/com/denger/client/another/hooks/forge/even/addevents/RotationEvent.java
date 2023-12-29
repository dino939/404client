package com.denger.client.another.hooks.forge.even.addevents;

import net.minecraftforge.eventbus.api.Event;

public class RotationEvent extends Event {
    private float xRot, yRot;
    private final float staticXrot, staticYrot;
    private boolean stepCancel = true;
    public RotationEvent(float xRot, float yRot) {
        this.staticXrot = xRot;
        this.staticYrot = yRot;
        this.xRot = xRot;
        this.yRot = yRot;
    }

    public void setStepCancel(boolean stepCancel) {
        this.stepCancel = stepCancel;
    }

    public float getxRot() {
        return xRot;
    }

    public float getyRot() {
        return yRot;
    }

    public void setxRot(float xRot) {
        this.xRot = xRot;
    }

    public void setyRot(float yRot) {
        this.yRot = yRot;
    }

    public boolean isStepCancel() {
        return stepCancel;
    }

    public float getStaticXrot() {
        return staticXrot;
    }

    public float getStaticYrot() {
        return staticYrot;
    }
}
