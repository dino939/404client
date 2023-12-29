package com.denger.client.modules.mods.misc;

import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.denger.client.MainNative.mc;

@ModuleTarget(ModName = "",category = Category.MISC)
public class NameProtect extends Module {

    @SubscribeEvent
    public void name(PlayerEvent.NameFormat e){
        if (e.getEntity() == mc.player){
             e.setDisplayname(ITextComponent.nullToEmpty("Sosnul?"));
        }
    }
    @SubscribeEvent
    public void onName2(PlayerEvent.TabListNameFormat e){
        if (e.getEntity() == mc.player){

             e.setDisplayName(ITextComponent.nullToEmpty("Sosnul?"));
        }
    }
}
