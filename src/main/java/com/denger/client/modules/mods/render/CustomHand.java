package com.denger.client.modules.mods.render;

import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@ModuleTarget(ModName = "DvtupnIboe",category = Category.RENDER)
public class CustomHand extends Module {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onHZ(RenderBlockOverlayEvent e){
        if (e.getOverlayType() == RenderBlockOverlayEvent.OverlayType.WATER){
            e.setCanceled(true);

        }
    }
}
