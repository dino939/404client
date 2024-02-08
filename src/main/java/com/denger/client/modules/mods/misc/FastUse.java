package com.denger.client.modules.mods.misc;


import com.denger.client.another.networkutills.ConnectionUtil;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPlayerTryUseItemPacket;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import static com.denger.client.Main.mc;

@ModuleTarget(ModName = "GbtuVtf", category = Category.MISC)
public class FastUse extends Module {


    @Override
    public boolean onPacket(Object packet, ConnectionUtil.Side side) {
        if (packet instanceof CPlayerTryUseItemPacket) {
            ObfuscationReflectionHelper.setPrivateValue(Minecraft.class, mc, 0, "field_71467_ac");
        }
        return super.onPacket(packet, side);
    }
}
