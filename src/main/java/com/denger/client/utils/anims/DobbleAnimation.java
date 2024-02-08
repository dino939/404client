package com.denger.client.utils.anims;

import com.denger.client.utils.MathUtils;

public class DobbleAnimation extends AbstractAnimation {
    long mc;
    float anim;
    public float to;
    public float speed;
    Type type;

    public DobbleAnimation(float anim, float to, float speed) {
        this(anim, to, speed, Type.HARP);
    }

    public DobbleAnimation(float anim, float to, float speed, Type type) {
        this.anim = anim;
        this.to = to;
        this.speed = speed;
        this.mc = System.currentTimeMillis();
        this.type = type;
    }


    @Override
    public float getAnim() {
        int count = (int) ((System.currentTimeMillis() - this.mc) / 5L);
        if (count > 0) {
            this.mc = System.currentTimeMillis();
        }
        for (int i = 0; i < count; ++i) {
            if (i < count / 2) {
                this.anim = anim(this.to, this.anim, this.speed, this.type);
            } else this.anim = anim(this.anim, this.to, this.speed, this.type);

        }
        return MathUtils.roundToDecimal(anim, 10);
    }


    public void reset() {
        this.mc = System.currentTimeMillis();
    }

    public void setAnim(float anim) {
        this.anim = anim;
        this.mc = System.currentTimeMillis();
    }

    public void setType(Type type) {
        this.type = type;
    }
}