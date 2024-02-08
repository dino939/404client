package com.denger.client.screens.main.comp;


import com.denger.client.utils.ColorUtil;

import java.awt.*;

import static com.denger.client.Main.getInstance;

public enum GuiColors {

    White(Color.WHITE),
    Black(Color.BLACK);

    public final Color color;

    GuiColors(Color color) {
        this.color = color;
    }

    public int getColor() {
        return ColorUtil.swapAlpha(color.getRGB(), getInstance.getMainScreen().getScale() * 255);
    }

    public int getColor(int alpha) {
        return ColorUtil.swapAlpha(color.getRGB(),getInstance.getMainScreen().getScale() * alpha);
    }

    public static int getThemeColor() {
        return ColorUtil.swapAlpha(getInstance.theme.getC().getRGB(), getInstance.getMainScreen().getScale() * 255);
    }

    public static int getThemeColor(int alpha) {
        return ColorUtil.swapAlpha(getInstance.theme.getC().getRGB(),getInstance.getMainScreen().getScale() * alpha);
    }
    public static int getThemeColor2() {
        return ColorUtil.swapAlpha(getInstance.theme.getC2().getRGB(), getInstance.getMainScreen().getScale() * 255);
    }
    public static int transform(int color){
        return ColorUtil.swapAlpha(new Color(color).getRGB(),getInstance.getMainScreen().getScale() * 255);
    }

    public static int getThemeColor2(int alpha) {
        return ColorUtil.swapAlpha(getInstance.theme.getC2().getRGB(),getInstance.getMainScreen().getScale() * alpha);
    }
    public static float getScale(){
        return getInstance.getMainScreen().getScale();
    }
}
