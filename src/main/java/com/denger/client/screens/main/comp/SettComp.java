package com.denger.client.screens.main.comp;


import com.denger.client.another.settings.Setting;
import com.denger.client.modules.Module;
import com.mojang.blaze3d.matrix.MatrixStack;

import java.util.function.Supplier;

public class SettComp {
    private float Xpos, Ypos;
    private static float MouseX,MouseY;
    private Setting setting;
    private Module local;
    public SettComp(Setting s){
        setting = s;
    }
    public SettComp(Module s){
        local = s;
    }
    public void Render(MatrixStack ms, float Xpos, float Ypos) {
        this.Xpos = Xpos;
        this.Ypos = Ypos;
    }

    public void mouseClicked(double mouseX, double mouseY, int button) {

    }

    public void mouseReleased(double mouseX, double mouseY, int button) {

    }

    public void keyPressed(int keyCode, int scanCode, int modifiers) {

    }
    public void close(){}
    public static void UpdateMouse(float MX,float MY){
        MouseX = MX;
        MouseY = MY;
    }
    public float getHeight() {
        return 0;
    }

    public float getXpos() {
        return Xpos;
    }

    public float getYpos() {
        return Ypos;
    }

    public Setting getSetting() {
        return setting;
    }

    public static float getMouseX() {
        return MouseX;
    }

    public static float getMouseY() {
        return MouseY;
    }

    public boolean isVisible(){
        return setting.getVisible();
    }

    public Module getLocal() {
        return local;
    }
}
