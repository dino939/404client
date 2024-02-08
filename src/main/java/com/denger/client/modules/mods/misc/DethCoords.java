package com.denger.client.modules.mods.misc;

import com.denger.client.another.hooks.forge.even.addevents.WorldUpdate;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.denger.client.Main.mc;

@ModuleTarget(ModName = "EfbuiDppset", category = Category.MISC)
public class DethCoords extends Module {
    public static int deley;
    @SubscribeEvent
    public void onPlayerTick(WorldUpdate playerTickEvent) {
        assert mc.player != null;
        if (mc.player.getHealth() < 0.0f || mc.player.isDeadOrDying() || !mc.player.isAlive()) {
            ++deley;
            if (deley == 0) {
                mc.gui.getChat().addMessage(ITextComponent.nullToEmpty(String.valueOf(new StringBuilder().append(" DeathCoords! X:").append((int) mc.player.getX()).append(" Y:").append((int) mc.player.getY()).append(" Z:").append((int) mc.player.getZ()))));
             }
            if (deley >= 10) {

                deley = -1;
            }
        }
    }
}
