package com.denger.client.another.sound;

import by.radioegor146.nativeobfuscator.Native;
import com.denger.client.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Native
public class SoundManager {
    private static AudioInputStream stream;
    private static final List<Clip> CLIPS_LIST = new ArrayList();
    public final byte[] hitsound;
    public final byte[] starting;
    public final byte[] wasted;
    //public final byte[] toggle;

    public SoundManager() {
        hitsound = (Utils.readAllBytes(Utils.getResource("sounds/bonk.wav")));
        starting = Utils.readAllBytes(Utils.getResource("sounds/luchiy.wav"));
        wasted = (Utils.readAllBytes(Utils.getResource("sounds/wasted.wav")));
       // toggle = (Utils.readAllBytes(Utils.getResource("sounds/on.mp3")));

    }


    public static void playSound(byte[] audioSrc, Entity player) {
        playSound(audioSrc, Minecraft.getInstance().player.distanceTo(player) * 5);
    }

    public static void playSound(byte[] audioSrc, float volume) {
        CLIPS_LIST.stream().filter(Objects::nonNull).filter(clip -> clip.isOpen()).filter(clip -> !clip.isRunning()).forEach(Clip::close);
        CLIPS_LIST.stream().filter(Objects::nonNull).filter(clip -> !(clip.isOpen() && clip.isRunning())).forEach(Clip::stop);
        CLIPS_LIST.stream().filter(Objects::nonNull).collect(Collectors.toList()).forEach(clip -> {
            if (!clip.isRunning()) CLIPS_LIST.remove(clip);
        });
        try {
            BufferedInputStream inputStream = new BufferedInputStream(new ByteArrayInputStream(audioSrc));
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            stream = AudioSystem.getAudioInputStream(bufferedInputStream);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        if (stream == null) return;
        try {
            CLIPS_LIST.add(AudioSystem.getClip());
        } catch (final Exception e) {
            e.printStackTrace();
        }
        CLIPS_LIST.stream().filter(Objects::nonNull).filter(clip -> !clip.isOpen()).forEach(clip -> {
            try {
                clip.open(stream);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        });
        CLIPS_LIST.stream().filter(Objects::nonNull).filter(Clip::isOpen).forEach(clip -> {
            float volumeVal = volume < 0 ? 0 : volume > 1 ? 1 : volume;
            FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            volumeControl.setValue(20f * (float) Math.log10(volumeVal));
        });
        CLIPS_LIST.stream().filter(Objects::nonNull).filter(Clip::isOpen).filter(clip -> !clip.isRunning()).forEach(Clip::start);
    }

}
