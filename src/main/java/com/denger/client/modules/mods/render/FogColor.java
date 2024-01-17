package com.denger.client.modules.mods.render;

import com.denger.client.another.settings.SettingTarget;
import com.denger.client.another.settings.sett.BoolSetting;
import com.denger.client.another.settings.sett.FloatSetting;
import com.denger.client.another.settings.sett.ModSetting;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.ModuleTarget;
import com.denger.client.utils.ColorUtil;
import com.denger.client.modules.another.Category;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.denger.client.MainNative.getInstance;

@ModuleTarget(ModName = "GphDpmps",category = Category.RENDER)
public class FogColor extends Module {
@SettingTarget(name = "Переливание")
BoolSetting transfusion = new BoolSetting();
@SettingTarget(name = "Скорось переливания")
FloatSetting speed = (FloatSetting) new FloatSetting().setMin(0).setMax(100).seType("%").setVal(50).setVisible(()->transfusion.getState());
@SettingTarget(name = "Цвет неба")
ModSetting type = (ModSetting) new ModSetting().setMods("Цвет 1","Цвет 2").setCurent("Цвет 1").setVisible(()->!transfusion.getState());

    @SubscribeEvent
    public void onFogColors(EntityViewRenderEvent.FogColors fogColors) {

        int color;
        if (transfusion.getState()){
            color = getInstance.theme.getColor2(0,speed.getVal()/100);
        }else {
            switch (type.getCurent()){
                case "Цвет 1":
                    color = getInstance.theme.getC().getRGB();
                    break;
                case "Цвет 2":
                    color = getInstance.theme.getC2().getRGB();
                    break;
                default:
                    color = -1;
            }
        }
        fogColors.setRed(ColorUtil.r(color));
        fogColors.setGreen(ColorUtil.g(color));
        fogColors.setBlue(ColorUtil.b(color));
    }
}
