package com.denger.client.modules.mods.misc;


import com.denger.client.another.hooks.forge.even.addevents.WorldUpdate;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import net.minecraft.network.play.client.CClientStatusPacket;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.denger.client.Main.mc;

@ModuleTarget(ModName = "BvupSftqbxo",category = Category.MISC)
public class AutoRespawn extends Module {
    @SubscribeEvent
    public void onUpdate(WorldUpdate e){
        assert mc.player != null;
        if (mc.player.isDeadOrDying()){
            mc.player.connection.send(new CClientStatusPacket(CClientStatusPacket.State.PERFORM_RESPAWN));
        }
    }
}
