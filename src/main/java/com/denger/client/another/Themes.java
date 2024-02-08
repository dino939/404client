package com.denger.client.another;

import com.denger.client.utils.ColorUtil;

import java.awt.*;

import static com.denger.client.Main.getInstance;
import static com.denger.client.utils.ColorUtil.TwoColoreffect;

public enum Themes {
    Bloody("Bloody", new Color(163, 0, 0),new Color(56, 0, 0)),
    Toxic("Toxic",new Color(0, 196, 255), new Color(0, 255, 150)),
    Darkness("Darkness",new Color(193, 101, 221), new Color(92, 39, 254)),
    Twillight("Twillight",new Color(190, 177, 232), new Color(132, 79, 175, 255)),
    Harway("Harway",new Color(127, 222, 164), new Color(19, 18, 19, 255)),
    Persoo("Persoo",new Color(189, 30, 81),new Color(241, 184, 20)),
    Weigh("Weigh",new Color(0, 171, 225),new Color(22, 31, 109)),
    Simpatico("Simpatico",new Color(157, 170, 242),new Color(255, 106, 61)),
    Lewerentz("Lewerentz",new Color(160, 174, 205),new Color(0, 0, 0)),
    Festival("Festival",new Color(171, 246, 45),new Color(214, 163, 251)),
    Watts("Watts",new Color(-1941665),new Color(-16733697)),
    Starnight("Starnight",new Color(255, 255, 0, 255), new Color(70, 130, 180, 255));


    public final Color color;
    public final Color color2;
    public final String name;

    Themes(String name,Color color, Color color2) {
        this.name = name;
        this.color = color;
        this.color2 = color2;
    }
     public Color getC(){
        return  getInstance.execute.customColor?getInstance.execute.desing.color1.getColor() : color;
    }
    public Color getC2(){
        return  getInstance.execute.customColor?getInstance.execute.desing.color2.getColor() : color2;
    }
    public int getColor() {
        return TwoColoreffect(getC(), getC2(), Math.abs(System.currentTimeMillis() / 10L) / 100.0 + 6.0F / 60).getRGB();
    }

    public int getColor2() {
        return TwoColoreffect(getC2(), getC(), Math.abs(System.currentTimeMillis() / 10L) / 100.0 + 6.0F / 60).getRGB();
    }
    public Color[] getColors(){
        return new Color[]{getC(),getC2()};
    }
    public int getColor(float offset) {
        return TwoColoreffect(getC(), getC2(), (Math.abs(System.currentTimeMillis() / 10L) / 100.0 + 6.0F * (offset * 0.1) / 60)).getRGB();
    }

    public int getColor2(float offset) {
        return TwoColoreffect(getC2(), getC(), (Math.abs(System.currentTimeMillis() / 10L) / 100.0 + 6.0F * (offset * 0.1) / 60)).getRGB();
    }
    public int getColor(float offset, float speed) {
        double sp = ((double) Math.abs(System.currentTimeMillis() / 4L) / 10.1275 * speed + offset) / 100.0;
        return TwoColoreffect(getC(), getC2(), sp).getRGB();
    }

    public int getColor2(float offset, float speed) {
        double sp = ((double) Math.abs(System.currentTimeMillis() / 4L) / 10.1275 * speed + offset) / 100.0;
        return TwoColoreffect(getC2(), getC(), sp).getRGB();
    }
    public int[] getClientColorsSpeed(float offset, float speed) {
        int[] colors = new int[]{getColor(300.0f + offset, speed), getColor(200.0f + offset, speed), getColor(100.0f + offset, speed), getColor(offset)};
        return colors;
    }
    public int[] getClientColors(float offset, float alpha) {
        return new int[]{ColorUtil.swapAlpha(getColor(300.0f + offset), alpha), ColorUtil.swapAlpha(getColor(200.0f + offset), alpha), ColorUtil.swapAlpha(getColor(100.0f + offset), alpha), ColorUtil.swapAlpha(getColor(offset), alpha)};
    }

}
