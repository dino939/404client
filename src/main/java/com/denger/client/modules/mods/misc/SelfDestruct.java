package com.denger.client.modules.mods.misc;

import com.denger.client.another.OptifineMenuLoad;
import com.denger.client.another.settings.SettingTarget;
import com.denger.client.another.settings.sett.BoolSetting;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import com.denger.client.utils.Cleaner;
import com.denger.client.utils.ReflectFileld;
import com.denger.client.utils.UnsafeString;
import com.denger.client.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.Init;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static com.denger.client.Main.getInstance;
import static com.denger.client.Main.mc;

@ModuleTarget(ModName = "TfmgEftusvdu",category = Category.MISC)
public class SelfDestruct extends Module {

    @SettingTarget(name = "Оптифайн")
    BoolSetting optifine = new BoolSetting();
    @Override
    public void onEnable() {
        if(Init.UID().equals("2")){
            new ReflectFileld(mc, Minecraft.class,8).setValueFinal(new File("C:\\Users\\Denger\\AppData\\Roaming\\.minecraft\\resourcepacks"));
        }
        if (optifine.getState()){
            new OptifineMenuLoad();
        }

        getInstance.panic = true;
        mc.screen = null;
        getInstance.panic();
        Utils.sleepVoid(()->SystemSounds.guiMusicTuner.brootStop(),5000);

        //List<String> lis = Arrays.asList(404+"client");
       // System.out.println(Utils.getPd());
       //new Thread(()->Cleaner.clean(new UnsafeString(String.valueOf(Utils.getPd())), lis)).start();
    }
}
