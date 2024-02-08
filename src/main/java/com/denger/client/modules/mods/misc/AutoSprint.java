package com.denger.client.modules.mods.misc;

import com.denger.client.another.hooks.forge.even.addevents.WorldUpdate;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.denger.client.Main.mc;

@ModuleTarget(ModName = "BvupTqsjou", category = Category.MISC)
public class AutoSprint extends Module {
    @SubscribeEvent
    public void onUpdate(WorldUpdate e) {
        if (!mc.player.isShiftKeyDown() && !mc.player.horizontalCollision && mc.player.input.forwardImpulse > 0) {
            assert mc.player != null;
            mc.player.setSprinting(true);

        }

    }
}
