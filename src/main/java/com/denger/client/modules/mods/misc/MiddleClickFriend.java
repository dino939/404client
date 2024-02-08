package com.denger.client.modules.mods.misc;

import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.denger.client.Main.getInstance;
import static com.denger.client.Main.mc;

@ModuleTarget(ModName = "NjeemfDmjdlGsjfoe", category = Category.MISC)
public class MiddleClickFriend extends Module {

    @SubscribeEvent
    public void onMouseEvent(InputEvent.MouseInputEvent event) {
        if (mc.level == null)return;
        if (event.getButton() == 2) {
            PlayerEntity e = getTarget();
            if (e != null){
                getInstance.getFriendManager().toggle(e);
            }
        }
    }


    public PlayerEntity getTarget(){
        Entity e = ((EntityRayTraceResult) mc.hitResult).getEntity();
        if (e instanceof PlayerEntity) {
            return (PlayerEntity) e;
        }
        return null;
    }
}
