package com.denger.client.modules.mods.render;

import com.denger.client.another.settings.SettingTarget;
import com.denger.client.another.settings.sett.FloatSetting;
import com.denger.client.modules.ShineModule;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import net.minecraft.client.settings.PointOfView;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.Event;

import static com.denger.client.MainNative.mc;
import static com.denger.client.utils.AnimationUtils.fast;


@ModuleTarget(ModName = "OpDbnfsbDmjq",category = Category.RENDER)
public class NoCameraClip extends ShineModule {
    @SettingTarget(name = "Дистанция")
    public static FloatSetting distancia = new FloatSetting().setMin(1).setMax(10).setVal(4);
    public static float animation = 0;

    @Override
    public void onEventContinuous(Event e) {
        if (e instanceof RenderWorldLastEvent){
            if (mc.options.getCameraType() == PointOfView.FIRST_PERSON) animation = fast(animation, 0f, 10);
            else animation = fast(animation, 1f, 10);
        }
    }

    public static double getDistance(double dis) {
        return 1f + ((dis - 1f) * animation);
    }
}
