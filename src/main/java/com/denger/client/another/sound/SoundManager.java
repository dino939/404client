package com.denger.client.another.sound;

import by.radioegor146.nativeobfuscator.Native;
import com.denger.client.utils.NetworkUtil;
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
    public final byte[] toggle;

    public SoundManager() {
        hitsound = (Utils.readAllBytes(NetworkUtil.getStream("https://cdn.discordapp.com/attachments/1030125024666988655/1160143160488505384/bonk.wav?ex=65339683&is=65212183&hm=17f8d740450d1a3cd8476b74ebb83a07f7970ed0cca21514080f64265ca4c8fc&")));
        starting = (Utils.readAllBytes(NetworkUtil.getStream("https://cdn.discordapp.com/attachments/1030125024666988655/1160149661466759168/opening.wav?ex=65339c91&is=65212791&hm=a23bf4239a65ac0e2db2a9fb194683cb9903c752103a6a14a5d4b29af4c3c5a8&")));
        wasted = (Utils.readAllBytes(NetworkUtil.getStream("https://cdn.discordapp.com/attachments/1160265847114317917/1160384137136439497/wasted.wav?ex=653476f1&is=652201f1&hm=850915452ea83f5cc71b2ec7e0f2ecf74a3aef9301eb49dadcf9a57ba3dd742f&")));
        toggle = (Utils.readAllBytes(NetworkUtil.getStream("https://cdn.discordapp.com/attachments/1141336230819266662/1163841148322840576/on.mp3?ex=65410a88&is=652e9588&hm=b00daf13b504a9a9ffb9f9f78f0d19e745711f752fd4106d8f48e442a9b6a5fc&")));

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
