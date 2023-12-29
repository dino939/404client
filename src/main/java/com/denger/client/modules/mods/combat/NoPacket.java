package com.denger.client.modules.mods.combat;

import com.denger.client.another.networkutills.ConnectionUtil;
import com.denger.client.another.settings.SettingTarget;
import com.denger.client.another.settings.sett.BoolSetting;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import net.minecraft.network.play.client.CHeldItemChangePacket;
import net.minecraft.network.play.server.SEntityPacket;
import net.minecraft.network.play.server.SHeldItemChangePacket;

import static com.denger.client.MainNative.mc;

@ModuleTarget(ModName = "OpQbdlfu", category = Category.COMBAT)
public class NoPacket extends Module {
    @SettingTarget(name = "Сервер ротации")
    BoolSetting rot = new BoolSetting().setBol(true);
    @SettingTarget(name = "Свап предмета")
    BoolSetting slot = new BoolSetting().setBol(true);

    @Override
    public boolean onPacket(Object packet, ConnectionUtil.Side side) {
        if (side == ConnectionUtil.Side.IN) {
            if (packet instanceof SEntityPacket.LookPacket && rot.getState()) {
                return false;

            }
            if (packet instanceof SHeldItemChangePacket && slot.getState()) {
                mc.player.connection.send(new CHeldItemChangePacket(((SHeldItemChangePacket)(packet)).getSlot()));
                return false;
            }
        }

        return super.onPacket(packet, side);
    }

}
