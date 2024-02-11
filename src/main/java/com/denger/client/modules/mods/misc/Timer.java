package com.denger.client.modules.mods.misc;

import com.denger.client.another.hooks.forge.even.addevents.WorldUpdate;
import com.denger.client.another.settings.SettingTarget;
import com.denger.client.another.settings.sett.FloatSetting;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import com.denger.client.utils.Utils;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@ModuleTarget(ModName = "Ujnfs",category = Category.MISC)
public class Timer extends Module {

    @SettingTarget(name = "Speed")
    FloatSetting test = new FloatSetting().setMin(1).setMax(100).setVal(20);

    @SubscribeEvent
    public void onUpdate(WorldUpdate e){
        Utils.SetTimer(test.getVal());
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Utils.ResetTimer();

    }
}