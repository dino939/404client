package com.denger.client.modules.mods.misc;

import com.denger.client.another.hooks.forge.even.addevents.WorldUpdate;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import com.denger.client.utils.TimerUtil;
import net.minecraft.network.play.client.CChatMessagePacket;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.denger.client.MainNative.mc;

@ModuleTarget(ModName = "LuMfbw",category = Category.MISC)
public class KtLeav extends Module {
    TimerUtil sleep = new TimerUtil();
    boolean chek = false;

    @Override
    public void onEnable() {
        super.onEnable();
        chek = true;

    }
    @SubscribeEvent
    public void onAttack(AttackEntityEvent event){
        if (event.getTarget().getName().getString().contains("KraboviyChlen")){
            sleep = new TimerUtil();
            chek = false;
        }

    }
    @SubscribeEvent
    public void onUpdate(WorldUpdate worldUpdate){
        if (!chek && sleep.hasReached(30100)){
            mc.player.connection.send(new CChatMessagePacket("/spawn"));
            chek = true;
        }
    }
}
