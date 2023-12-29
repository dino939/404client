package com.denger.client.modules.mods.render;

import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;

@ModuleTarget(ModName = "HmpxFtq", category = Category.RENDER)
public class GlowEsp extends Module {
    ArrayList<Entity> entities = new ArrayList<>();
    @SubscribeEvent
    public void onPreRenderEntity(RenderLivingEvent.Pre e) {
        if (e.getEntity() instanceof AbstractClientPlayerEntity){
            entities.add(e.getEntity());
            e.getEntity().setGlowing(true);
        }

    }

    @Override
    public void onDisable() {
        entities.forEach(e->e.setGlowing(false));
    }
}
