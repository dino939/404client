package com.denger.client.screens.main.settings;

import com.denger.client.another.settings.Setting;
import com.denger.client.another.settings.sett.ThemeSetting;
import com.denger.client.screens.main.MainScreen;
import com.denger.client.screens.main.comp.SettComp;
import com.denger.client.utils.rect.RectUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.denger.client.screens.main.comp.GuiColors;

import static com.denger.client.MainNative.fontManager;
import static com.denger.client.MainNative.getInstance;

public class ThemeComp extends SettComp {
    ThemeSetting setting;

    public ThemeComp(Setting s) {
        super(s);
        setting = (ThemeSetting) s;
    }

    @Override
    public void Render(MatrixStack ms, float Xpos, float Ypos) {
        super.Render(ms, Xpos, Ypos);
        fontManager.font16.drawString(ms, setting.getName(), getXpos() + 15, getYpos() + 2, getInstance.theme == setting.getTheme() ? GuiColors.getThemeColor() : -1);
        RectUtil.drawRound(getXpos() + 100, getYpos(), 15, 15, 3, setting.getTheme().color2.getRGB());
        RectUtil.drawRound(getXpos() + 117, getYpos(), 15, 15, 3, setting.getTheme().color.getRGB());

    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (MainScreen.ishover(getXpos() + 25, getYpos(), 95, 15, mouseX, mouseY) && button == 0) {
            setting.setThisTheme();
        }
    }

    @Override
    public float getHeight() {
        return 20;
    }
}
