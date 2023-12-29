package com.denger.client.modules.mods.misc;

import com.denger.client.another.hooks.forge.even.addevents.WorldUpdate;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.denger.client.MainNative.mc;

@ModuleTarget(ModName = "Usbwfm",category = Category.MISC)
public class Travel extends Module {
    Vector3d goTo = null;
    @Override
    public void onEnable() {
         goTo = new Vector3d(867,64,89);

    }
    @SubscribeEvent
    public void onUdate(WorldUpdate e){
        if (goTo != null && mc.player.tickCount % 9 == 0){
            assert mc.player != null;
           mc.player.xxa = 867;
            mc.player.yya = 64;
            mc.player.zza = 89;
        }
    }

    @SubscribeEvent
    public void onLeave(WorldEvent.Unload e){
        goTo = null;
        this.toggle();
    }
}
