package com.denger.client.another;

import com.denger.client.MainNative;
import com.denger.client.another.hooks.PlayerControllerHook;
import com.denger.client.another.hooks.forge.even.addevents.WorldUpdate;
import com.denger.client.modules.Module;
import com.denger.client.utils.Utils;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

import static com.denger.client.MainNative.getInstance;
import static com.denger.client.MainNative.mc;

public class Events {
    MainNative main = getInstance;


    @SubscribeEvent
    public void onEvent(Event e) {
        main.getRegisterModule().getModulesShine().forEach(m -> {
            m.onEventContinuous(e);
        });
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
            MainNative.LShift = true;
        } else if (event.getKey() == GLFW.GLFW_KEY_LEFT_SHIFT && event.getAction() == 0) {
            MainNative.LShift = false;
        }
    }

    @SubscribeEvent
    public void worldUpdate(WorldUpdate update) {

//       if (mc.level == null) return;
//       if (mc.player != null) {
//           if (!(mc.player instanceof ClientPlayerEntityHook)) {
//               boolean temp = mc.player.isCreative();
//               Vector3d posOld = mc.player.position();
//               getInstance.getHandlerUtil().sendSelfPacket(new SRespawnPacket(mc.level.dimensionType(), mc.level.dimension(), -8609512560664653198L, mc.gameMode.getPlayerMode(), mc.gameMode.getPreviousPlayerMode(), mc.level.isDebug(), false, true));
//              try {
//                  if (temp) {
//                      ObfuscationReflectionHelper.setPrivateValue(NetworkPlayerInfo.class, mc.getConnection().getPlayerInfo(mc.player.getUUID()), GameType.CREATIVE, "field_178866_b");
//                  }

//              } catch (Exception ignore   ) {
//              }
//               mc.player.setPos(posOld.x,posOld.y,posOld.z);
//           }

//       }
        if (!(mc.gameMode instanceof PlayerControllerHook)) {
        Utils.sleepVoid(()->{
            assert mc.player != null;
            mc.gameMode = new PlayerControllerHook(mc, mc.player.connection);
        },1000);

       }
 }
}
