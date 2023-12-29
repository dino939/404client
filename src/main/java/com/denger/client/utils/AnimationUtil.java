package com.denger.client.utils;

import java.util.ArrayList;

public class AnimationUtil {
    public static ArrayList<AnimationUtil> animationUtils = new ArrayList<>();
    float anim;

    private float to;
    private long startTime;
    private long durationMillis;
    private boolean asList;

    public AnimationUtil() {
        this(0, 0, 200, true);
    }
    public AnimationUtil(float anim, float to, long durationMillis) {
        this(anim, to, durationMillis, true);
    }

    public AnimationUtil(float anim, float to, long durationMillis, boolean IsList) {
        this.anim = anim;
        this.to = to;
        this.durationMillis = durationMillis;
        this.startTime = System.currentTimeMillis();
        asList = IsList;
        if (IsList) {
            animationUtils.add(this);

        }
    }

    public float getAnim() {

        if (!asList) {
            updateVal();
        }
        return MathUtils.roundToDecimal(anim, 3);
    }

    public void updateVal() {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - startTime;

        if (elapsedTime >= durationMillis) {
            anim = to;
        } else {
            float progress = (float) elapsedTime / durationMillis;
            float smoothProgress = smoothStep(progress);
            anim = MathUtils.lerp(anim, to, smoothProgress);
        }
    }

    public static void update() {
        animationUtils.forEach(AnimationUtil::updateVal);
    }

    public void setAnim(float anim) {
        this.anim = anim;
    }

    public void setState(float val) {
        this.anim = val;
        this.to = val;
    }

    public void setTo(float to) {
        this.to = to;
    }

    public void goTo(float to) {
        if (to == this.to) return;
        this.to = to;
        this.startTime = System.currentTimeMillis();
    }

    public void setDurationMillis(long durationMillis) {
        this.durationMillis = durationMillis;
    }

    private float smoothStep(float t) {
        return t * t * (3 - 2 * t);
    }

    public float getTo() {
        return to;
    }
}