package com.denger.client.modules.mods.combat;

import com.denger.client.another.hooks.forge.even.addevents.WorldUpdate;
import com.denger.client.another.settings.SettingTarget;
import com.denger.client.another.settings.sett.BoolSetting;
import com.denger.client.another.settings.sett.FloatSetting;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import com.denger.client.utils.Utils;
import net.minecraft.item.Items;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.denger.client.Main.mc;

@ModuleTarget(ModName = "BvupUpufn", category = Category.COMBAT)
public class AutoTotem extends Module {
    @SettingTarget(name = "Здоровье")
    BoolSetting zdorovye = new BoolSetting().setBol(true);
    @SettingTarget(name = "Начиная с")
    FloatSetting nachinayaS = (FloatSetting) new FloatSetting().setMin(1).setMax(20).setVal(6).seType(" хп").setVisible(() -> zdorovye.getState());
    @SettingTarget(name = "Вернуть предмен?")
    BoolSetting obratno = new BoolSetting().setBol(true);
    public int save = -1;

    @SubscribeEvent
    public void onTick(WorldUpdate e) {
        assert (mc.player != null);
        assert (mc.gameMode != null);
        if (!zdorovye.getState() || mc.player.getHealth() > nachinayaS.getVal()) {
            return;
        }
        if (mc.player.getOffhandItem().getItem().equals(Items.TOTEM_OF_UNDYING)) {
            return;
        }
        int totem = Utils.findItem(Items.TOTEM_OF_UNDYING);
        if (totem == -1) {
            if (obratno.getState() && save != -1) {
                Utils.PickItemTo(save, 45);
                save = -1;
            }

            return;
        }
        if (obratno.getState() && save == -1) {
            save = totem;
        }
        Utils.PickItemTo(totem, 45);

    }

}
