package com.denger.client.modules.mods.misc;

import com.denger.client.another.settings.SettingTarget;
import com.denger.client.another.settings.sett.BoolSetting;
import com.denger.client.another.sound.SoundManager;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.ModuleTarget;
import com.denger.client.modules.another.Category;
import com.denger.client.utils.TimerUtil;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.denger.client.MainNative.getInstance;
import static com.denger.client.MainNative.mc;

@ModuleTarget(ModName = "TztufnTpvoet", category = Category.MISC)
public class SystemSounds extends Module {
    @SettingTarget(name = "Ударный звук")
    BoolSetting attackSound = new BoolSetting().setBol(true);
//    @SettingTarget(name = "Звук гуи")
//    BoolSetting guisound = new BoolSetting().setBol(false   );
    TimerUtil timerUtil = new TimerUtil();

    @SubscribeEvent
    public void onAttack(AttackEntityEvent event) {
        if ( event.getPlayer() == mc.player && attackSound.getState()){
            SoundManager.playSound(getInstance.getSoundManager().hitsound, event.getTarget());
          }}
//    @SubscribeEvent
//    public void onModule(ModuleToggleEvent event){
//        if (!guisound.getState() && timerUtil.hasReached(1500))return;
//        timerUtil.reset();
//        SoundManager.playSound(getInstance.getSoundManager().toggle, 10);
//    }
}
