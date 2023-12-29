package com.denger.client.modules.mods.hud;

import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@ModuleTarget(ModName = "", category = Category.HUD)
public class Crosshair extends Module {

    @SubscribeEvent
    public void onClosshair() {

        //IngameGui
    }
}
