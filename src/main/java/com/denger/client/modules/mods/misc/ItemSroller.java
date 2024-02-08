package com.denger.client.modules.mods.misc;


import com.denger.client.another.hooks.forge.even.addevents.WorldUpdate;
import com.denger.client.another.settings.SettingTarget;
import com.denger.client.another.settings.sett.FloatSetting;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import com.denger.client.utils.TimerUtil;
import net.minecraft.client.gui.screen.inventory.ChestScreen;
import net.minecraft.client.util.InputMappings;
import net.minecraft.inventory.container.ClickType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

import static com.denger.client.Main.mc;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;
@ModuleTarget(ModName = "JufnTspmmfs",category = Category.MISC)
public class ItemSroller extends Module {
@SettingTarget(name = "Задержка")
    FloatSetting zaderzka = new FloatSetting().setMin(0).setMax(200).setVal(30).seType("ms");
    TimerUtil timerUtil = new TimerUtil();

    @SubscribeEvent
    public void onUpdate(WorldUpdate e) {
        if (mc.screen instanceof ChestScreen && ((ChestScreen) mc.screen).getSlotUnderMouse() != null && InputMappings.isKeyDown(mc.getWindow().getWindow(), mc.options.keyShift.getKey().getValue()) && GLFW.glfwGetMouseButton(mc.getWindow().getWindow(), GLFW_MOUSE_BUTTON_1) == 1 && timerUtil.hasReached((int) zaderzka.getVal())) {
            assert mc.gameMode != null;
            assert mc.player != null;
            mc.gameMode.handleInventoryMouseClick(mc.player.containerMenu.containerId, ((ChestScreen) mc.screen).getSlotUnderMouse().index, 0, ClickType.QUICK_MOVE, mc.player);
            timerUtil.reset();
        }
    }
}
