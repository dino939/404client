package com.denger.client.modules.mods.misc;


import com.denger.client.another.hooks.forge.even.addevents.WorldUpdate;
import com.denger.client.another.settings.SettingTarget;
import com.denger.client.another.settings.sett.ModSetting;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.denger.client.MainNative.mc;

@ModuleTarget(ModName = "XpsmeUjnf", category = Category.MISC)
public class WorldTime extends Module {
    @SettingTarget(name = "Время суток")
    ModSetting mode = new ModSetting().setMods("Night", "Day", "Noon", "Midnight").setCurent("Night");

    @SubscribeEvent
    public void RenderWorldLastEvent(WorldUpdate e) {
        if (!mc.noRender) {
            switch (mode.getCurent()) {
                case "Day":
                    assert mc.level != null;
                    mc.level.setDayTime((long) 1037.0);
                    break;
                case "Noon":
                    assert mc.level != null;
                    mc.level.setDayTime((long) 6164.0);
                    break;
                case "Night":
                    assert mc.level != null;
                    mc.level.setDayTime((long) 13042.0);
                    break;
                case "Midnight":
                    assert mc.level != null;
                    mc.level.setDayTime((long) 18037.0);
                    break;
            }
        }
    }

}
