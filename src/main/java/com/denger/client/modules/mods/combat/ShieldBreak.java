package com.denger.client.modules.mods.combat;

import com.denger.client.another.hooks.forge.even.addevents.WorldUpdate;
import com.denger.client.another.settings.SettingTarget;
import com.denger.client.another.settings.sett.FloatSetting;
import com.denger.client.another.settings.sett.ModSetting;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import com.denger.client.utils.TimerUtil;
import com.denger.client.utils.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Items;
import net.minecraft.network.play.client.CHeldItemChangePacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.denger.client.MainNative.mc;

@ModuleTarget(ModName = "TijfmeCsfbl", category = Category.COMBAT)
public class ShieldBreak extends Module {
    @SettingTarget(name = "Задержка удара:")
    FloatSetting zaderjca = new FloatSetting().setMin(0).setMax(500).setVal(100).seType("ms");
    @SettingTarget(name = "Задержка возврата:")
    FloatSetting zaderjca2 = new FloatSetting().setMin(0).setMax(500).setVal(100).seType("ms");
    @SettingTarget(name = "Мод")
    ModSetting mode = new ModSetting().setMods("Client", "Silent").setCurent("Silent");
    TimerUtil util = new TimerUtil();

    @SubscribeEvent
    public void onUpdate(WorldUpdate e) {
        PlayerEntity entity = getTarget();
        if (entity == null) return;
        if (entity.getUseItem().getItem() == Items.SHIELD && util.hasReached((long) zaderjca.getVal())) {
            util.reset();
            int axeSlot = getAxeSlot();
            if (axeSlot == -1) return;
            int old = mc.player.inventory.selected;
            if (mode.getCurent().equals("Silent")) {
                mc.player.connection.send(new CHeldItemChangePacket(axeSlot));
                attack(entity);
                Utils.sleepVoid(() -> {
                    mc.player.connection.send(new CHeldItemChangePacket(mc.player.inventory.selected));
                }, (int) zaderjca2.getVal());
            } else {
                mc.player.inventory.selected = axeSlot;
                attack(entity);
                Utils.sleepVoid(() -> {
                    mc.player.inventory.selected = old;
                }, (int) zaderjca2.getVal());
            }
        }
    }

    public void attack(PlayerEntity e) {
        assert mc.gameMode != null;
        mc.gameMode.attack(mc.player, e);
        mc.player.swing(Hand.MAIN_HAND);
    }

    public int getAxeSlot() {
        for (int index = 0; index < 9; index++) {
            if (mc.player.inventory.getItem(index).getItem() instanceof AxeItem) {
                return index;
            }
        }
        return -1;
    }

    public PlayerEntity getTarget() {
        if (mc.hitResult.getType() != RayTraceResult.Type.ENTITY) return null;
        Entity target = ((EntityRayTraceResult) mc.hitResult).getEntity();
        if (!(target instanceof PlayerEntity)) return null;
        return (PlayerEntity) target;
    }
}
