package com.denger.client.modules.mods.hud;

import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.world.GameType;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import static com.denger.client.Main.mc;


@ModuleTarget(ModName = "BlaBla", category = Category.HUD)
public class Test extends Module {

    @Override
    public void onEnable() {


        ObfuscationReflectionHelper.setPrivateValue(NetworkPlayerInfo.class, mc.getConnection().getPlayerInfo(mc.player.getUUID()), GameType.SURVIVAL, "field_178866_b");

        toggle();
    }
}
