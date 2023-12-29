package com.denger.client.modules.mods.misc;


import com.denger.client.another.hooks.forge.even.addevents.WorldUpdate;
import com.denger.client.another.settings.SettingTarget;
import com.denger.client.another.settings.sett.BoolSetting;
import com.denger.client.another.settings.sett.FloatSetting;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import com.denger.client.utils.TimerUtil;
import net.minecraft.client.gui.screen.inventory.ChestScreen;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.denger.client.MainNative.mc;

@ModuleTarget(ModName = "DiftuTufbmfs", category = Category.MISC)
public class ChestStealer extends Module {
    @SettingTarget(name = "АвтоЗакрываение")
    BoolSetting avtoZakritie = new BoolSetting().setBol(true);
    @SettingTarget(name = "Задержка")
    FloatSetting zaderzka = new FloatSetting().setMin(0).setMax(200).setVal(30);
    TimerUtil timerUtil = new TimerUtil();
    @SubscribeEvent
    public void onUpdate(WorldUpdate e) {
        if (mc.player != null) {
            for (int index = 0; index < mc.player.containerMenu.slots.size(); ++index) {
                if (mc.screen instanceof ChestScreen) {
                    if (((ChestContainer) mc.player.containerMenu).getContainer().getItem(index).getItem() != Item.byId(-1) && timerUtil.hasReached((int) (zaderzka.getVal() - 1))) {
                        assert mc.gameMode != null;
                        mc.gameMode.handleInventoryMouseClick(mc.player.containerMenu.containerId, index, 0, ClickType.QUICK_MOVE, mc.player);
                        timerUtil.reset();
                    }
                    if (mc.player.inventory.getFreeSlot() == -1 && avtoZakritie.getState()) {
                        mc.screen.onClose();
                        mc.setScreen(null);
                    }
                }
            }
        }
    }


}
