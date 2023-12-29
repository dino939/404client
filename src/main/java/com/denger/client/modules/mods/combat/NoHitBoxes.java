package com.denger.client.modules.mods.combat;

import com.denger.client.another.hooks.forge.even.addevents.WorldUpdate;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.denger.client.MainNative.getInstance;
import static com.denger.client.MainNative.mc;

@ModuleTarget(ModName = "OpIjuCpyft",category = Category.COMBAT)
public class NoHitBoxes extends Module {


    @SubscribeEvent
    public void onUpdate(WorldUpdate e) {
        for (PlayerEntity playerEntity : mc.level.players()) {
            if (playerEntity != mc.player) {
                playerEntity.setBoundingBox(new AxisAlignedBB(playerEntity.getX(), playerEntity.getBoundingBox().minY, playerEntity.getZ(), playerEntity.getX(), playerEntity.getBoundingBox().maxY, playerEntity.getZ()));
            }
        }
    }

    @Override
    public void onDisable() {
        for (PlayerEntity playerEntity : mc.level.players()) {
            if (playerEntity != mc.player && !getInstance.getFriendManager().isFriend(playerEntity) && !getInstance.getRegisterModule().isEnable(HitBox.class)) {
                playerEntity.setBoundingBox(new AxisAlignedBB(playerEntity.getX() - 0.3, playerEntity.getBoundingBox().minY, playerEntity.getZ() - 0.3, playerEntity.getX() + 0.3, playerEntity.getBoundingBox().maxY, playerEntity.getZ() + 0.3));
            }
        }
        super.onDisable();
    }
}
