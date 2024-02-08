package com.denger.client.modules.mods.combat;

import com.denger.client.another.settings.SettingTarget;
import com.denger.client.another.settings.sett.BoolSetting;
import com.denger.client.another.settings.sett.FloatSetting;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import com.denger.client.utils.Utils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.play.client.CHeldItemChangePacket;
import net.minecraft.network.play.client.CPlayerTryUseItemPacket;
import net.minecraft.util.Hand;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.denger.client.Main.mc;

@ModuleTarget(ModName = "NjeemfDmjdlQfbsm", category = Category.COMBAT)
public class MiddleClickPearl extends Module {
    @SettingTarget(name = "Задержка?")
    public BoolSetting zaderjca = new BoolSetting();
    @SettingTarget(name = "Задержка в:")
    public FloatSetting zaderjcav = (FloatSetting) new FloatSetting().setMin(0).setMax(1000).setVal(200).seType("ms").setVisible(()->zaderjca.getState());

    @SubscribeEvent
    public void onMouseEvent(InputEvent.MouseInputEvent event) {
        if (event.getButton() == 2) {
            boolean state = false;
            for (int i = 0; i < 9; i++) {
                ItemStack itemStack = mc.player.inventory.getItem(i);
                if (itemStack.getItem() == Items.ENDER_PEARL && !state) {
                    state = true;
                    mc.player.connection.send(new CHeldItemChangePacket(i));
                    mc.player.connection.send(new CPlayerTryUseItemPacket(Hand.MAIN_HAND));
                    Utils.sleepVoid(()->{mc.player.connection.send(new CHeldItemChangePacket(mc.player.inventory.selected));}, zaderjca.getState()? (int) zaderjcav.getVal() :0);

                }
            }
        }
    }

    @Override
    public void onDisable() {
        mc.player.connection.send(new CHeldItemChangePacket(mc.player.inventory.selected));

    }
}
