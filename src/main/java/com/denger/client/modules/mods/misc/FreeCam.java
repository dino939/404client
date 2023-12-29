package com.denger.client.modules.mods.misc;

import com.denger.client.another.networkutills.ConnectionUtil;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.network.play.client.CPlayerPacket;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.GameType;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import static com.denger.client.MainNative.mc;

@ModuleTarget(ModName = "GsffDbn",category = Category.MISC)
public class FreeCam extends Module {
    Vector3d oldPos;
    @Override
    public void onEnable() {

        oldPos = mc.player.position();
        ObfuscationReflectionHelper.setPrivateValue(NetworkPlayerInfo.class, mc.getConnection().getPlayerInfo(mc.player.getUUID()), GameType.SPECTATOR, "field_178866_b");
        mc.player.abilities.flying = true;
    }

    @Override
    public boolean onPacket(Object packet, ConnectionUtil.Side side) {
        if (packet instanceof CPlayerPacket){
            return false;
        }
        return super.onPacket(packet, side);
    }

    @Override
    public void onDisable() {
        mc.player.setPos(oldPos.x,oldPos.y,oldPos.z);
        ObfuscationReflectionHelper.setPrivateValue(NetworkPlayerInfo.class, mc.getConnection().getPlayerInfo(mc.player.getUUID()), GameType.SURVIVAL, "field_178866_b");
         mc.player.abilities.flying = false;
    }
}
