package com.denger.client.another;

import com.denger.client.Main;
import com.denger.client.another.hooks.MainMenuHook;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class OptifineMenuLoad {
    public OptifineMenuLoad(){
        Main.optifine = true;
        Main.eventManager.register(this);

    }
    @SubscribeEvent
    public void onGui(GuiOpenEvent event){
        if (event.getGui() instanceof MainMenuScreen){
            event.setGui(new MainMenuHook());
        }
    }
}
