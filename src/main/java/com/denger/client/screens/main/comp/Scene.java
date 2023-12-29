package com.denger.client.screens.main.comp;

import com.mojang.blaze3d.matrix.MatrixStack;

import static com.denger.client.MainNative.getInstance;

public class Scene {
    public void init(){}
    public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
    }

    public void mouseClicked(double p_231044_1_, double p_231044_3_, int p_231044_5_) {
    }

    public void mouseReleased(double p_231048_1_, double p_231048_3_, int p_231048_5_) {
    }

    public void keyPressed(int p_231046_1_, int p_231046_2_, int p_231046_3_) {
    }

    public void onClose() {
    }

    public float getX() {
        return getInstance.getMainScreen().getSx();
    }

    public float getY() {
        return getInstance.getMainScreen().getSy();
    }
    public void scrole(float num){}
}
