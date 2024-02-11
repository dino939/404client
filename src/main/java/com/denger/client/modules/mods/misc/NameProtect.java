package com.denger.client.modules.mods.misc;

import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import com.denger.client.utils.ReflectFileld;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;

import static com.denger.client.Main.mc;

@ModuleTarget(ModName = "ObnfQspufdu", category = Category.MISC)
public class NameProtect extends Module {
    String name = "";
    @Override
    public void onEnable() {
       // new ReflectFileld(mc, Minecraft.class,"isLocalServer").setValue(false);
       // name = (String) new ReflectFileld(mc.player.getGameProfile(),GameProfile.class,1).getFinalValue();
       //// new ReflectFileld(mc.player.getGameProfile(),GameProfile.class,1).setValue("HuiVRot");
    }

    @Override
    public void onDisable() {
        //new ReflectFileld(mc.player.getGameProfile(),GameProfile.class,1).setValue(name);
    }
}
