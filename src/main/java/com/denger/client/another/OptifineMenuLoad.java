package com.denger.client.another;

import com.denger.client.MainNative;
import com.denger.client.another.hooks.MainMenuHook;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.denger.client.MainNative.mc;

public class OptifineMenuLoad {
    public OptifineMenuLoad(){
        MainNative.optifine = true;
        MainNative.eventManager.register(this);

    }
    @SubscribeEvent
    public void onGui(GuiOpenEvent event){
        if (event.getGui() instanceof MainMenuScreen){
            event.setGui(new MainMenuHook());
        }
    }
}
