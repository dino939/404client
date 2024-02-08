package com.denger.client.another.networkutills;

import com.denger.client.another.hooks.GameRendererHook;
import com.denger.client.another.hooks.IngameGuiHook;
import com.denger.client.modules.Module;
import com.denger.client.utils.ReflectFileld;
import com.denger.client.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IngameGui;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.denger.client.Main.getInstance;
import static com.denger.client.Main.mc;


public class EventsHandlerUtil {
    private boolean initialized = false;
    private boolean hooked = false;


    public boolean onPacket(Object packet, ConnectionUtil.Side side) {
        boolean suc = true;
        for (Module module : getInstance.getRegisterModule().getModules()) {
            if (mc.player != null && mc.level != null && module.getState()) {
                suc &= module.onPacket(packet, side);
            }
        }
        return suc;
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent eventUpdate) {
        if (mc.player != null && mc.level != null) {
            if (!initialized) {
                new ConnectionUtil(this);
                if (!hooked) {
                    hooked = !hooked;
                    try {
                        Utils.sleepVoid(() -> getInstance.getConfigManager().loadConfig("32423r23febfbfjhbsmfb32"), 200);
                    } catch (Exception e) {
                        System.out.println(e.getCause().getMessage());
                        getInstance.getConfigManager().loadConfig("32423r23febfbfjhbsmfb32");
                    }
                    new ReflectFileld(mc, Minecraft.class, IngameGui.class).setValueFinal(new IngameGuiHook(mc));
                    new ReflectFileld(mc, Minecraft.class, 25).setValueFinal(new GameRendererHook(mc, mc.getResourceManager(), mc.renderBuffers()));
                    //ObfuscationReflectionHelper.setPrivateValue(Minecraft.class, mc, new FontRendererHook(ObfuscationReflectionHelper.getPrivateValue(FontRenderer.class, mc.font, "field_211127_e")), "field_71466_p");
                }
                initialized = true;
            }
        } else {
            initialized = false;
        }
    }


}