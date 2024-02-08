package com.denger.client.utils;

import com.denger.client.utils.anims.Animation;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.function.Supplier;

import static com.denger.client.Main.getInstance;


public class GuiMusicTuner {
    String musicName;
    String forceMusicName;
    String tempDirPath = System.getProperty("java.io.tmpdir");
    String format = ".wav";
    Animation volume = new Animation(0.0f, 0.0f, 0.01f);
    float maxVolume;
    boolean wantToChangeTrack;
    Clip clip;
    AudioInputStream stream;
    String temporaryTrackLoc;
    private boolean state;
    public Supplier<Boolean> caseInsansive = () -> true;

    public GuiMusicTuner(float normalVolume) {
        this(null, normalVolume);
    }

    public GuiMusicTuner(String musicName, float normalVolume) {
        this.musicName = musicName;
        this.maxVolume = normalVolume;
    }

    public void setVolumePC(float value) {
        this.volume.setAnim(value * this.getMaxVolumeVal());
        this.volume.to = value * this.getMaxVolumeVal();
    }

    public void setPlaying(boolean playing) {
        state = playing;
        this.setVolumeSmoothPC(playing ? 1.0f : 0.0f);
    }
    public void brootStop(){
        clip.stop();
    }
    public boolean isPlayng() {
        return state;
    }

    public float getMaxVolumeVal() {
        return this.maxVolume;
    }

    public void setMaxVolume(float value) {
        this.maxVolume = value / 4.0f;
    }

    public void multipleVolume(float mul) {
        this.maxVolume *= mul;
        this.maxVolume = this.maxVolume < 0.0f ? 0.0f : (this.maxVolume > 1.0f ? 1.0f : this.maxVolume);
    }

    public void setVolumeSmoothPC(float value) {
        this.volume.speed = (value == 1.0f ? 5.0E-4f + 0.02f * this.volume.getAnim() : 0.004f) * (this.wantToChangeTrack ? 1.25f : 0.75f);
        this.volume.to = value * this.getMaxVolumeVal();
        if (this.volume.to == this.getMaxVolumeVal() && this.getVolumeVal() == 0.0f) {
            this.volume.setAnim(this.getMaxVolumeVal() / 5.0f);
        }
    }

    public void setVolumeChangeSpeed(float value) {
        this.volume.speed = value;
    }

    public void setTrackName(String name) {
        if (this.forceMusicName == null) {
            this.forceMusicName = name;
        }
        this.wantToChangeTrack = !this.forceMusicName.equalsIgnoreCase(this.musicName);
        this.forceMusicName = name;
    }

    public void setTrackNameForce(String name) {
        this.musicName = name;
        this.forceMusicName = name;
    }

    public String getTrackLoc() {
        return new File(tempDirPath, this.musicName + this.format).getAbsolutePath();
    }

    public String getForceTrackLoc() {
        return new File(tempDirPath, this.forceMusicName + this.format).getAbsolutePath();
    }

    public float getVolumeVal() {
        float volume = this.volume.getAnim();
        return (double) volume < 1.0E-4 ? 0.0f : ((double) volume > 0.9999 ? 1.0f : volume);
    }

    float getVolumeForMixer() {
        return (float) (Math.log(this.getVolumeVal()) / Math.log(10.0) * 20.0);
    }

    public boolean canPlayTrack() {
        return this.getVolumeVal() != 0.0f;
    }

    public void controlTrackUpdater() {

        FloatControl volumeControl;
        boolean play;
        if (getInstance.panic) {
            this.setPlaying(false);
        }
        if (this.wantToChangeTrack) {
            this.setVolumeSmoothPC(0.0f);
            if (this.getVolumeVal() == 0.0f || this.musicName == null) {
                this.setTrackNameForce(this.forceMusicName);
                this.wantToChangeTrack = false;
            }
        }
        String trackLoc = this.getTrackLoc();
        float volume = this.getVolumeForMixer();
        play = this.canPlayTrack() || this.clip != null && this.clip.isRunning() && this.clip.isControlSupported(FloatControl.Type.MASTER_GAIN) && ((FloatControl) this.clip.getControl(FloatControl.Type.MASTER_GAIN)).getValue() != ((FloatControl) this.clip.getControl(FloatControl.Type.MASTER_GAIN)).getMinimum();
        if (play) {
            try {
                if (this.stream == null) {
                    InputStream inputStream = Files.newInputStream(new File(trackLoc).toPath());
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                    this.stream = AudioSystem.getAudioInputStream(bufferedInputStream);
                }
            } catch (Exception ignore) {

            }
            if (this.stream != null) {
                try {
                    if (this.clip == null || this.clip != null && !this.clip.isOpen()) {
                        this.clip = AudioSystem.getClip();
                    }
                } catch (Exception ignore) {
                }
            }
            if (this.clip != null) {
                if (!this.clip.isOpen()) {
                    try {
                        this.clip.open(this.stream);
                    } catch (Exception inputStream) {
                    }
                } else if (!this.clip.isRunning() || this.clip.getMicrosecondPosition() == this.clip.getMicrosecondLength()) {
                    if (caseInsansive.get()) {
                        this.clip.setMicrosecondPosition(0L);
                        ((FloatControl) this.clip.getControl(FloatControl.Type.MASTER_GAIN)).setValue((int) volume);

                        this.clip.start();
                    }

                }
            }
        } else if (this.clip != null && this.clip.isRunning()) {
            this.clip.stop();
            this.clip.close();
            this.clip = null;
            this.stream = null;
        }
        volumeControl = this.clip == null ? null : (FloatControl) this.clip.getControl(FloatControl.Type.MASTER_GAIN);
        if (volumeControl != null && volumeControl.getValue() != (float) ((int) volume)) {
            volumeControl.setValue((int) volume);
        }
        if (this.temporaryTrackLoc == null && trackLoc != null) {
            this.temporaryTrackLoc = trackLoc;
        }
        if (trackLoc != null && this.temporaryTrackLoc != null && !this.temporaryTrackLoc.equalsIgnoreCase(trackLoc)) {
            this.temporaryTrackLoc = trackLoc;
            if (this.clip != null) {
                if (this.clip.isRunning()) {
                    this.clip.stop();
                }
                if (this.clip.isOpen()) {
                    this.clip.close();
                }
                this.clip = null;
                this.stream = null;
            }
        }
    }
}

