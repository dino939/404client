package com.denger.client.utils.anims;

import com.denger.client.utils.MathUtils;

public class CycleAnimation extends AbstractAnimation {
    private long lastUpdateTime;
    private float currentPoint;
    private float startPoint;
    private float endPoint;
    private float selectedPoint;
    private float speed;
    private Type type;
    boolean state;
    public CycleAnimation(float startPoint, float endPoint, float speed) {
        this(startPoint, endPoint, speed, Type.HARP);
    }

    public CycleAnimation(float startPoint, float endPoint, float speed, Type type) {
        this.selectedPoint = endPoint;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.currentPoint = startPoint;
        this.speed = speed;
        this.type = type;
        this.lastUpdateTime = System.currentTimeMillis();
    }

    @Override
    public float getAnim() {
        long currentTime = System.currentTimeMillis();
        int count = (int) ((currentTime - this.lastUpdateTime) / 5L);

        if (count > 0) {
            this.lastUpdateTime = currentTime;
        }
        for (int i = 0; i < count; ++i) {
            this.currentPoint = anim(this.currentPoint, getNextPoint(), this.speed,this.type);

        }

        return MathUtils.roundToDecimal(this.currentPoint, 10);
    }


    private float getNextPoint() {
        if (MathUtils.approximatelyEqual(selectedPoint, currentPoint)) {
            if (state){
                selectedPoint = startPoint;
            }else {
                selectedPoint = endPoint;
            }
            state = !state;
        }
        return selectedPoint;
    }


    public void setStartPoint(float startPoint) {
        this.startPoint = startPoint;
    }

    public void setEndPoint(float endPoint) {
        this.endPoint = endPoint;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public void setSelectedPoint(float selectedPoint) {
        this.selectedPoint = selectedPoint;
    }

    public void setCurrentPoint(float currentPoint) {
        this.currentPoint = currentPoint;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getSpeed() {
        return speed;
    }

    public void reset() {
        this.lastUpdateTime = System.currentTimeMillis();
    }
}
