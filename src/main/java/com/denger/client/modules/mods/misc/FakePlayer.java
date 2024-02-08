package com.denger.client.modules.mods.misc;


import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import net.minecraft.client.entity.player.RemoteClientPlayerEntity;

import static com.denger.client.Main.mc;

@ModuleTarget(ModName = "GblfQmbzfs",category = Category.MISC)
public class FakePlayer extends Module {

     RemoteClientPlayerEntity player = null;

    @Override
    public void onEnable() {
        super.onEnable();
        player = CreatePlayer();

    }

    @Override
    public void onDisable() {
        player.remove();
    }

    public RemoteClientPlayerEntity CreatePlayer() {
        double d0 = mc.player.getX();
        double d1 = mc.player.getY();
        double d2 = mc.player.getZ();
        float f = mc.cameraEntity.xRot;
        float f1 = mc.cameraEntity.xRot;
        RemoteClientPlayerEntity remoteclientplayerentity = new RemoteClientPlayerEntity(mc.level, mc.player.getGameProfile());
        remoteclientplayerentity.setId(-1);
        remoteclientplayerentity.setPosAndOldPos(d0, d1, d2);
        remoteclientplayerentity.setPacketCoordinates(d0, d1, d2);
        remoteclientplayerentity.absMoveTo(d0, d1, d2, f, f1);
        remoteclientplayerentity.setHealth(20);
        remoteclientplayerentity.setSilent(false);
        mc.level.addPlayer(-1,remoteclientplayerentity);

        return remoteclientplayerentity;
    }
}
