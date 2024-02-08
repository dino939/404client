package com.denger.client.modules.mods.combat;


import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.denger.client.Main.mc;

@ModuleTarget(ModName = "OpQvti", category = Category.COMBAT)
public class NoPush extends Module {
    @SubscribeEvent
    public void onPreRenderEntity(RenderLivingEvent.Pre e) {
        if (e.getEntity() == mc.player) {
            e.getEntity().pushthrough = 1;
        }
    }

    @Override
    public void onDisable() {
        mc.player.pushthrough = 0;
    }
}
