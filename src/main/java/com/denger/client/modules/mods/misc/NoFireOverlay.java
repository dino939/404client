package com.denger.client.modules.mods.misc;

import com.denger.client.modules.Module;
import com.denger.client.modules.another.ModuleTarget;
import com.denger.client.modules.another.Category;
import com.denger.client.utils.ReflectFileld;
import net.minecraft.entity.EntityType;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.denger.client.MainNative.mc;

@ModuleTarget(ModName = "OpGjsfPwfsmbz",category = Category.MISC)
public class NoFireOverlay extends Module {
    @SubscribeEvent
    public void onRenderBlockOverlay(RenderBlockOverlayEvent renderBlockOverlayEvent) {
          if (renderBlockOverlayEvent.getOverlayType().name().equals("FIRE")) {
              new ReflectFileld(mc.player.getType(), EntityType.class,114).setValueFinal(true);

          }
    }
}
