package com.denger.client.modules.mods.misc;


import com.denger.client.another.hooks.MovementInputFromOptionsHook;
import com.denger.client.another.hooks.forge.even.addevents.WorldUpdate;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.denger.client.Main.mc;

@ModuleTarget(ModName = "HvjXbml", category = Category.MISC)
public class GuiWalk extends Module {

    @SubscribeEvent
    public void onRespawn(WorldUpdate e) {
        assert mc.player != null;
         if (!(mc.player.input instanceof MovementInputFromOptionsHook)) {
            mc.player.input = new MovementInputFromOptionsHook(mc.options);

        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        assert mc.player != null;
        mc.player.input = new MovementInputFromOptions(mc.options);
    }
}
