package com.denger.client.modules.mods.misc;

import com.denger.client.another.hooks.InventoryScreenHook;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.ModuleTarget;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import com.denger.client.modules.another.Category;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@ModuleTarget(ModName = "JowfoupszUispx", category = Category.MISC)
public class InventoryThrow extends Module {
    @SubscribeEvent
    public void onGui(GuiOpenEvent event) {
        if(event.getGui() instanceof InventoryScreen){
          event.setGui(new InventoryScreenHook());
        }
    }
}
