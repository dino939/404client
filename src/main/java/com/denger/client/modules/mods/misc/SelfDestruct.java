package com.denger.client.modules.mods.misc;

import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;

import static com.denger.client.MainNative.getInstance;
import static com.denger.client.MainNative.mc;

@ModuleTarget(ModName = "TfmgEftusvdu",category = Category.MISC)
public class SelfDestruct extends Module {

    @Override
    public void onEnable() {
        getInstance.panic = true;
        mc.screen = null;
        getInstance.panic();
    }
}
