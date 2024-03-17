package com.denger.client.another;

import com.denger.client.Main;
import com.denger.client.another.hooks.PlayerControllerHook;
import com.denger.client.another.hooks.forge.even.addevents.WorldUpdate;
import com.denger.client.modules.Module;
import com.denger.client.modules.mods.misc.SystemSounds;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

import static com.denger.client.Main.getInstance;
import static com.denger.client.Main.mc;
import static net.minecraftforge.event.TickEvent.Phase.START;

public class Events {
    Main main = getInstance;


    @SubscribeEvent
    public void onEvent(Event e) {
        main.getRegisterModule().getModulesShine().forEach(m -> {
            m.onEventContinuous(e);
        });
    }
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event){
        if (event.phase == START){
            if (SystemSounds.guiMusicTuner != null){
            //    SystemSounds.guiMusicTuner.controlTrackUpdater();;
            }

        }
    }
    @SubscribeEvent
    public void onKey(InputEvent.KeyInputEvent event) {
        if (event.getAction() == 1) {

            if (event.getKey() == GLFW.GLFW_KEY_RIGHT_SHIFT) {
                if (getInstance.getMainScreen() == mc.screen) {
                    getInstance.getMainScreen().onClose();
                } else if (mc.screen == null) {
                    mc.setScreen(getInstance.getMainScreen());
                }

            }
            for (Module module : getInstance.getRegisterModule().getModules()) {
                if (module == null) continue;
                if (module.getKeycode() == event.getKey() && mc.screen == null) {
                    module.toggle();
                }
            }
        }
        if (event.getKey() == GLFW.GLFW_KEY_LEFT_SHIFT && event.getAction() == 1) {
            Main.LShift = true;
        } else if (event.getKey() == GLFW.GLFW_KEY_LEFT_SHIFT && event.getAction() == 0) {
            Main.LShift = false;
        }
    }

    @SubscribeEvent
    public void worldUpdate(WorldUpdate update) {

        if (!(mc.gameMode instanceof PlayerControllerHook)) {
            assert mc.player != null;
        //    mc.gameMode = new PlayerControllerHook(mc, mc.player.connection);
       }
 }
}
