package com.denger.client.another.hooks.forge.even.addevents;

import net.minecraftforge.eventbus.api.Event;

import static com.denger.client.Main.mc;

public class MoveEvent extends Event {
    private float xRot,YRot;
    private double x,y,z;
    boolean isOngraund;
    boolean isPassager;
    public MoveEvent(float xRot,float yRot,double x, double y, double z,boolean isOngraund,boolean isPassager){
        mc.player.setYHeadRot(yRot);
        this.xRot = xRot;
        this.YRot = yRot;
        this.x = x;
        this.y = y;
        this.z = z;
        this.isOngraund = isOngraund;
        this.isPassager = isPassager;
    }
    public MoveEvent(float xRot,float yRot,double x, double y, double z,boolean isOngraund){
        this(xRot,yRot,x,y,z,isOngraund,false);
    }

    public void setxRot(float xRot) {
        this.xRot = xRot;
    }

    public void setyRot(float YRot) {
        this.YRot = YRot;

    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public float getXRot() {
        return xRot;
    }

    public float getYRot() {
        return YRot;
    }

    public boolean isOngraund() {
        return isOngraund;
    }
}
