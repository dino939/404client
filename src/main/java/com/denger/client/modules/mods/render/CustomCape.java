package com.denger.client.modules.mods.render;

import com.denger.client.another.settings.SettingTarget;
import com.denger.client.another.settings.sett.ModSetting;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.ModuleTarget;
import com.denger.client.modules.another.Category;

@ModuleTarget(ModName = "DvtupnDbqf", category = Category.RENDER,enable = true)
public class CustomCape extends Module {
    public static boolean enable;
    @SettingTarget(name = "Мод")
    public static ModSetting mode = new ModSetting().setMods("клиент", "аниме","гиф").setCurent("гиф");

    @Override
    public void onEnable() {
        enable = true;
    }

    @Override
    public void onDisable() {
        enable = false;
    }
}
