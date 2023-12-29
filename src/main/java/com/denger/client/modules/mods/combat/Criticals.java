package com.denger.client.modules.mods.combat;

import com.denger.client.another.networkutills.ConnectionUtil;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import com.denger.client.utils.ReflectFileld;
import net.minecraft.network.play.client.CPlayerPacket;
import net.minecraft.network.play.client.CUseEntityPacket;
import net.minecraft.util.math.vector.Vector3d;

import static com.denger.client.MainNative.mc;

@ModuleTarget(ModName = "Dsjujdbmt", category = Category.COMBAT)
public class Criticals extends Module {

    @Override
    public boolean onPacket(Object packet, ConnectionUtil.Side side) {
        if (packet instanceof CUseEntityPacket && ((CUseEntityPacket) packet).getAction() == CUseEntityPacket.Action.ATTACK) {
            assert mc.player != null;
            if (mc.player.isOnGround()) {
                new ReflectFileld(mc.player.getDeltaMovement(), Vector3d.class, 2).setValue(-0.01D);
                mc.getConnection().send(new CPlayerPacket.PositionPacket(mc.player.getX(), mc.player.getY() + 1.28E-9D, mc.player
                        .getZ(), true));
                mc.getConnection().send(new CPlayerPacket.PositionPacket(mc.player
                        .getX(), mc.player.getY(), mc.player.getZ(), false));
            }
        }
        return super.onPacket(packet, side);
    }
}
