package com.denger.client.modules.mods.render;

import com.denger.client.another.settings.SettingTarget;
import com.denger.client.another.settings.sett.FloatSetting;
import com.denger.client.another.settings.sett.ModSetting;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.ModuleTarget;
import com.denger.client.modules.another.Category;

@ModuleTarget(ModName = "TxjohBojnbujpo",category = Category.RENDER)
public class SwingAnimation extends Module {

    @SettingTarget(name = "Сила")
    public static FloatSetting power = new FloatSetting().setMax(180).setMin(20).setVal(100);
    @SettingTarget(name = "Плавность")
    public static FloatSetting plavnost = new FloatSetting().setMax(1).setMin(0).setVal(0.5f);
    @SettingTarget(name = "Мод")
    public static ModSetting modeSetting = new ModSetting().setMods("Короткая","Простая","На себя","От себя","Защита","Размер","360");
}
