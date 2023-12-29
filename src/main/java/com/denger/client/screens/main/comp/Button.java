package com.denger.client.screens.main.comp;

import com.denger.client.utils.AnimationUtil;
import com.denger.client.utils.ColorUtil;
import com.mojang.blaze3d.matrix.MatrixStack;

import static com.denger.client.MainNative.fontManager;
import static com.denger.client.MainNative.getInstance;

public class Button {
    private float posX, posY;
    private final String name;
    private final Scene scene;
    AnimationUtil anim;

    public Button(String name, Scene scene) {
        this.name = name;
        this.scene = scene;
        anim = new AnimationUtil(0.4f, 0.4f, 1000);
    }

    public void render(MatrixStack ms, float posX, float posY) {
        this.posX = posX;
        this.posY = posY;

        fontManager.font24.drawString(ms, name, posX, posY, ColorUtil.TwoColoreffect(GuiColors.getThemeColor(), GuiColors.White.getColor(), anim.getAnim()).getRGB());
    }

    public void onClick(double p_231044_1_, double p_231044_3_, int p_231044_5_) {
        if (ishover(posX, posY, fontManager.font24.getStringWidth(name) + 3, fontManager.font20.getFontHeight(), p_231044_1_, p_231044_3_)) {
            getInstance.getMainScreen().setCuretBut(this);
        }
    }

    public void init() {
        anim.goTo(1);
    }

    public void close() {
        anim.goTo(0.4f);
    }


    public boolean ishover(float xx, float yy, float width, float height, double mouseX, double mouseY) {
        if (mouseX > xx && mouseX < width + xx && mouseY > yy && mouseY < yy + height) {
            return true;
        }
        return false;
    }

    public Scene getScene() {
        return scene;
    }

    public String getName() {
        return name;
    }
}
