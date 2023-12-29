package com.denger.client.another.networkutills;

import com.denger.client.another.hooks.GameRendererHook;
import com.denger.client.modules.Module;
import com.denger.client.utils.ReflectFileld;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.IClientPlayNetHandler;
import net.minecraft.network.IPacket;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.denger.client.MainNative.getInstance;
import static com.denger.client.MainNative.mc;


public class EventsHandlerUtil {
    private boolean initialized = false;
    private boolean hooked = false;
    private ConnectionUtil local;

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
                local = new ConnectionUtil(this);
                if (!hooked) {
                    getInstance.getConfigManager().loadConfig("32423r23febfbfjhbsmfb32");
                    new ReflectFileld(mc, Minecraft.class, 25).setValueFinal(new GameRendererHook(mc, mc.getResourceManager(), mc.renderBuffers()));
                    //ObfuscationReflectionHelper.setPrivateValue(Minecraft.class, mc, new FontRendererHook(ObfuscationReflectionHelper.getPrivateValue(FontRenderer.class, mc.font, "field_211127_e")), "field_71466_p");

                    hooked = !hooked;
                }
                initialized = true;
            }
        } else {
            initialized = false;
        }
    }

    public void sendSelfPacket(IPacket<IClientPlayNetHandler> packet) {
        if (mc.level == null) return;
        try {
            local.channelRead(local.priv, packet);
        } catch (Exception ignore) {
        }
    }

}