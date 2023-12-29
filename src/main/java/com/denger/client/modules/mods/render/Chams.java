package com.denger.client.modules.mods.render;

import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@ModuleTarget(ModName = "Dibnt", category = Category.RENDER)
public class Chams extends Module {
    @SubscribeEvent
    public void onPreRenderEntity(RenderLivingEvent.Pre e) {
    }

    @SubscribeEvent
    public void onPostRenderEntity(RenderLivingEvent.Post e) {
    }
}
