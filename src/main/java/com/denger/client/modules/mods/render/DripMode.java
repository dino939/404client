package com.denger.client.modules.mods.render;

import com.denger.client.another.settings.SettingTarget;
import com.denger.client.another.settings.sett.FloatSetting;
import com.denger.client.another.settings.sett.ModSetting;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import com.denger.client.utils.anims.AbstractAnimation;
import com.denger.client.utils.anims.Animation;
import com.denger.client.utils.anims.CycleAnimation;

@ModuleTarget(ModName = "EsjqNpef", category = Category.RENDER)
public class DripMode extends Module{
    public CycleAnimation anim = new CycleAnimation(0.5f,1.5f,0.01f);
    public CycleAnimation anim2 = new CycleAnimation(1.5f,0.5f,0.01f);
    @SettingTarget(name = "Тип")
    public ModSetting mod = new ModSetting().setMods("AutoDrip","Custom");
    @SettingTarget(name = "В стороны")
    public FloatSetting xz = (FloatSetting) new FloatSetting().setMin(0.5f).setMax(3).setVal(1).setVisible(()->mod.getCurent().equals("Custom"));

    @SettingTarget(name = "В высоту")
    public FloatSetting y = (FloatSetting) new FloatSetting().setMin(0.5f).setMax(3).setVal(1).setVisible(()->mod.getCurent().equals("Custom"));
}
