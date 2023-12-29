package com.denger.client.modules.mods.combat;

import com.denger.client.another.networkutills.ConnectionUtil;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.play.client.CUseEntityPacket;
import net.minecraft.util.math.EntityRayTraceResult;

import static com.denger.client.MainNative.getInstance;
import static com.denger.client.MainNative.mc;

@ModuleTarget(ModName = "OpBuubdlGsjfoet",category = Category.COMBAT)
public class NoAttackFriends extends Module {


    @Override
    public boolean onPacket(Object packet, ConnectionUtil.Side side) {
        try {
            if (((CUseEntityPacket) packet).getAction().equals(CUseEntityPacket.Action.ATTACK) && getInstance.getFriendManager().isFriend((PlayerEntity) ((EntityRayTraceResult) mc.hitResult).getEntity())) {
                return false;
            }
        } catch (Exception ignored) {
        }
        return super.onPacket(packet, side);
    }
}
