package com.denger.client.modules.mods.misc;


import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;

import static com.denger.client.MainNative.mc;

@ModuleTarget(ModName = "GvmmCsjhiu",category = Category.MISC)
public class FullBright extends Module {

    @Override
    public void onEnable() {
        mc.options.gamma = 10000;

    }

}
