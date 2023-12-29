package com.denger.client.another.settings.sett;

import com.denger.client.another.settings.Setting;

import java.awt.*;

public class ColorSetting extends Setting {
    private Color localColor = Color.WHITE;

    public ColorSetting setColor(Color color) {
        localColor = color;
        return this;
    }

    public Color getColor() {
        return localColor;
    }
}
