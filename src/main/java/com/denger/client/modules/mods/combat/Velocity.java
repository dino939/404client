package com.denger.client.modules.mods.combat;

import com.denger.client.another.hooks.forge.even.addevents.WorldUpdate;
import com.denger.client.another.networkutills.ConnectionUtil;
import com.denger.client.another.settings.SettingTarget;
import com.denger.client.another.settings.sett.FloatSetting;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import com.denger.client.utils.ReflectFileld;
import net.minecraft.network.play.server.SEntityVelocityPacket;

import java.util.SplittableRandom;

import static com.denger.client.MainNative.mc;

@ModuleTarget(ModName = "Wfmpdjuz", category = Category.COMBAT)
public class Velocity extends Module {
    @SettingTarget(name = "Шанс")
    FloatSetting shans = new FloatSetting().seType("%").setMax(100).setMin(0).setVal(100);
    @SettingTarget(name = "Процент")
    FloatSetting procent = new FloatSetting().seType("%").setMax(100).setMin(0).setVal(100);


    @Override
    public boolean onPacket(Object packet, ConnectionUtil.Side side) {
        if (packet instanceof SEntityVelocityPacket) {
            if (new SplittableRandom().nextInt(1, 101) <= shans.getVal()) {
                if (procent.getVal() > 95) {
                    return ((SEntityVelocityPacket) packet).getId() != mc.player.getId();
                } else {
                    new ReflectFileld(packet, SEntityVelocityPacket.class, 1).setValue(((SEntityVelocityPacket) packet).getXa() / (int) procent.getVal());
                    new ReflectFileld(packet, SEntityVelocityPacket.class, 2).setValue(((SEntityVelocityPacket) packet).getYa() / (int) procent.getVal());
                    new ReflectFileld(packet, SEntityVelocityPacket.class, 3).setValue(((SEntityVelocityPacket) packet).getZa() / (int) procent.getVal());
                }
            }
        }
        return super.onPacket(packet, side);
    }

    public void onUpdate(WorldUpdate e) {
        mc.player.pushthrough = 1;
    }

    @Override
    public void onDisable() {
        mc.player.pushthrough = 0;
    }
}
