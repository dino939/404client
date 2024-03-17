package com.denger.client.utils.anims;

import com.denger.client.utils.MathUtils;

public abstract class AbstractAnimation {

    protected float anim(float anim, float to, float speed, Type type) {
        switch (type) {
            case FAST:
                return MathUtils.fast(anim, to, speed * 10);

            case HARP:
                return MathUtils.harp(anim, to, speed);

            case LERP:
                return MathUtils.lerp(anim, to, speed);

            case SIMPLE:
                if (anim > to) {
                    anim -= speed;
                } else if (anim < to) {
                    anim += speed;
                }
                if (!(Math.abs(to - anim) < speed)) return anim;
                anim = to;
                return anim;

            case EaseBackIn:
                double x1 = anim/to;
                float shrink = 2;
                anim = (float) Math.max(0, 1 + shrink * Math.pow(x1 - 1, 3) + Math.pow(x1 - 1, 2));
                return anim;

            default:
                return anim;
        }
    }


    public abstract float getAnim();
}
