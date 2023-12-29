package com.denger.client.modules.mods.hud;

import com.denger.client.another.settings.SettingTarget;
import com.denger.client.another.settings.sett.BoolSetting;
import com.denger.client.another.settings.sett.ColorSetting;
import com.denger.client.another.settings.sett.ThemeSetting;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.ModuleTarget;
import com.denger.client.modules.another.Category;
import com.denger.client.another.Themes;

import static com.denger.client.MainNative.getInstance;


@ModuleTarget(ModName = "Eftjho", category = Category.HUD)
public class Design extends Module {
    //@SettingTarget(name = "Бета функции")
    //public BoolSetting beta = new BoolSetting().setOn(() -> getInstance.beta = true).setOff(() -> getInstance.beta = false);
     public BoolSetting customColor = new BoolSetting().setOn(()->{getInstance.executeNative.customColor = true;}).setOff(()->{getInstance.executeNative.customColor = false;});
    @SettingTarget(name = "Цвет 1")
    public ColorSetting color1 = new ColorSetting();
    @SettingTarget(name = "Цвет 2")
    public ColorSetting color2 = new ColorSetting();
    public Design() {
        customColor.setName("Кастомный Цвет");
        customColor.setModule(this);
         this.getSettings().add(customColor);
        getInstance.getSettingManager().addSett(customColor);
        color1.setVisible(()->customColor.getState());
        color2.setVisible(()->customColor.getState());
        for (Themes theme : Themes.values()) {
            ThemeSetting setting = new ThemeSetting().setTheme(theme);
            setting.setName(theme.name);
            setting.setModule(this);
            setting.setVisible(()->!customColor.getState());
            this.getSettings().add(setting);
            getInstance.getSettingManager().addSett(setting);
        }
    }

    @Override
    public void onEnable() {
         toggle();
    }
}
