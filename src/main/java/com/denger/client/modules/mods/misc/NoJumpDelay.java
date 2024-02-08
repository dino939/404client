package com.denger.client.modules.mods.misc;


import com.denger.client.another.hooks.forge.even.addevents.WorldUpdate;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import static com.denger.client.Main.mc;

@ModuleTarget(ModName = "OpKvnqEfmbz", category = Category.MISC)
public class NoJumpDelay extends Module {
    @SubscribeEvent
    public void onUpdate(WorldUpdate e) {
        ObfuscationReflectionHelper.setPrivateValue(LivingEntity.class, mc.player, 0, "field_70773_bE");
    }
}
