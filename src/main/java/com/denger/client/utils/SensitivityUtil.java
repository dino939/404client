package com.denger.client.utils;

import static com.denger.client.Main.mc;

public class SensitivityUtil {
    public static float calculateSensitivity(float rotation) {
        return calculateDeltaMouse(rotation) * calculateGCDValue();
    }

    public static float calculateDeltaMouse(float delta) {
        return Math.round(delta / calculateGCDValue());
    }

    public static float calculateGCD() {
        float temp;
        return (temp = (float) (mc.options.sensitivity * 0.6 + 0.2)) * temp * temp * 8;
    }

    public static float calculateGCDValue() {
        return (float) (calculateGCD() * 0.15);
    }
}

