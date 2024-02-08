package com.denger.client.modules.mods.misc;

import com.denger.client.another.hooks.forge.even.addevents.WorldUpdate;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.ModuleTarget;
import net.minecraft.potion.Effects;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.denger.client.Main.mc;

@ModuleTarget(ModName = "OpFggfdu",category = Category.MISC)
public class NoEffect extends Module {


    @SubscribeEvent
    public void onUpdate(WorldUpdate e){
        mc.player.removeEffect(Effects.CONFUSION);
        mc.player.removeEffect(Effects.BLINDNESS);
        mc.player.removeEffect(Effects.WITHER);
        mc.player.removeEffect(Effects.HUNGER);
        mc.player.removeEffect(Effects.POISON);
    }
}
