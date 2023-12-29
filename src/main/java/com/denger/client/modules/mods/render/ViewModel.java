package com.denger.client.modules.mods.render;

import com.denger.client.another.settings.SettingTarget;
import com.denger.client.another.settings.sett.FloatSetting;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.ModuleTarget;
import com.denger.client.modules.another.Category;

@ModuleTarget(ModName = "WjfxNpefm",category = Category.RENDER)

public class ViewModel extends Module {
    @SettingTarget(name = "Правая рука x")
    public static FloatSetting rightX = new FloatSetting().setMax(3).setMin(-3).setVal(0);
    @SettingTarget(name = "Правая рука y")
    public static FloatSetting rightY = new FloatSetting().setMax(3).setMin(-3).setVal(0);
    @SettingTarget(name = "Правая рука z")
    public static FloatSetting rightZ = new FloatSetting().setMax(3).setMin(-3).setVal(0);
    @SettingTarget(name = "Левая рука x")
    public static FloatSetting leftX = new FloatSetting().setMax(3).setMin(-3).setVal(0);
    @SettingTarget(name = "Левая рука y")
    public static FloatSetting leftY = new FloatSetting().setMax(3).setMin(-3).setVal(0);
    @SettingTarget(name = "Левая рука z")
    public static FloatSetting leftZ = new FloatSetting().setMax(3).setMin(-3).setVal(0);


}
