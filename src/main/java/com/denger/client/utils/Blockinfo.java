package com.denger.client.utils;

import net.minecraft.util.math.vector.Vector3d;

public class Blockinfo {

    private int x;
    private int y;
    private int z;
    private int Color;
    private String name;
    public Blockinfo(Vector3d pos,int color,String name){
        this((int) pos.x(), (int) pos.y(), (int) pos.z(),color,name);
    }

    public Blockinfo(double x, double y, double z, int color, String name) {
        this.x = (int) x;
        this.y = (int) y;
        this.z = (int) z;
        Color = color;
        this.name = name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public String getName() {
        return name;
    }

    public int getColor() {
        return Color;
    }
}
