package com.denger.client.modules.mods.misc;

import com.denger.client.another.settings.SettingTarget;
import com.denger.client.another.settings.sett.BoolSetting;
import com.denger.client.another.settings.sett.FloatSetting;
import com.denger.client.another.settings.sett.ModSetting;
import com.denger.client.another.sound.SoundManager;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import com.denger.client.utils.GuiMusicTuner;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static com.denger.client.Main.getInstance;
import static com.denger.client.Main.mc;

@ModuleTarget(ModName = "TztufnTpvoet", category = Category.MISC)
public class SystemSounds extends Module {
    @SettingTarget(name = "Ударный звук")
    BoolSetting attackSound = new BoolSetting().setBol(false);
    Path path = Paths.get(System.getProperty("java.io.tmpdir"));
    public static GuiMusicTuner guiMusicTuner = new GuiMusicTuner(0.4f);
    @SettingTarget(name = "")
    ModSetting musigs = new ModSetting().setMods(findWavFiles(path)).setRunnable(this::onChageTrack);
    @SettingTarget(name = "Громкость")
    FloatSetting volume = new FloatSetting().setMin(1).setMax(200).setVal(50).setRunnable(this::onChageVolume);
    // @SettingTarget(name = "Перезагрузка")
    BoolSetting refresh = new BoolSetting().setOn(this::setRefresh);

    public SystemSounds() {
        guiMusicTuner.setMaxVolume(200);
        guiMusicTuner.caseInsansive = () -> mc.level != null;
    }

    public void setRefresh() {
        ArrayList<String> temp = findWavFiles(path);
        musigs.setMods(temp);
        musigs.setCurent(temp.get(0));
        refresh.setBol(false);
    }

    public void onChageTrack() {
        if (getState()) guiMusicTuner.setPlaying(false);
        guiMusicTuner.setTrackNameForce(musigs.getCurent());
        if (getState()) guiMusicTuner.setPlaying(true);
    }

    public void onChageVolume() {
        guiMusicTuner.setVolumePC(volume.getVal()/200);
    }

    @SubscribeEvent
    public void onAttack(AttackEntityEvent event) {
        if (event.getPlayer() == mc.player && attackSound.getState()) {
            SoundManager.playSound(getInstance.getSoundManager().hitsound, event.getTarget());
        }
    }

    @Override
    public void onEnable() {
        guiMusicTuner.setPlaying(true);
    }

    @Override
    public void onDisable() {
        guiMusicTuner.setPlaying(false);
    }

    //    @SubscribeEvent
//    public void onModule(ModuleToggleEvent event){
//        if (!guisound.getState() && timerUtil.hasReached(1500))return;
//        timerUtil.reset();
//        SoundManager.playSound(getInstance.getSoundManager().toggle, 10);
//    }
    public static ArrayList<String> findWavFiles(Path directoryPath) {
        ArrayList<String> wavFiles = new ArrayList<>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directoryPath, "*.wav")) {
            for (Path entry : stream) {
                if (Files.isRegularFile(entry)) {
                    wavFiles.add(entry.getFileName().toString().split(".wav")[0]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (wavFiles.isEmpty()){
            wavFiles.add("");
        }
        return wavFiles;
    }
}
