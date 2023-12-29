package com.denger.client.modules.mods.misc;


import com.denger.client.another.hooks.forge.even.addevents.WorldUpdate;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import net.minecraft.client.multiplayer.PlayerController;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import static com.denger.client.MainNative.mc;

@ModuleTarget(ModName = "OpCsfblEfmbz",category = Category.MISC)
public class NoBreakDelay extends Module {


    @SubscribeEvent
     public void onUpdate(WorldUpdate e){
        ObfuscationReflectionHelper.setPrivateValue(PlayerController.class, mc.gameMode, 0, "field_78781_i");
    }
}
