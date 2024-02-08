package com.denger.client.modules.mods.misc;


import com.denger.client.another.hooks.forge.even.addevents.WorldUpdate;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import com.denger.client.utils.TimerUtil;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.denger.client.Main.mc;

@ModuleTarget(ModName = "BoujBgl",category = Category.MISC)
public class AntiAfk extends Module {
    TimerUtil wait;


    @Override
    public void onEnable() {
        super.onEnable();
        wait = new TimerUtil();
    }
    @SubscribeEvent
    public void onUpdate(WorldUpdate e) {
        if (wait.hasReached(1500) && !mc.options.keyJump.isDown()) {
            assert mc.player != null;
            mc.player.jumpFromGround();
            wait.reset();



        }

    }
}
