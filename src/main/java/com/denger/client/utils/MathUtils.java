
package com.denger.client.utils;

import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.vector.Vector3d;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Random;

import static com.denger.client.Main.mc;

public class MathUtils {
    private static final Random random = new Random();

    public static int scale = 2;
    private static final float EPSILON = 1e-6f;
    private static final long startTime = System.currentTimeMillis();

    public static double deltaTime() {
        float fps = 60;
        try {
            fps = Integer.parseInt(Minecraft.getInstance().fpsString.split(" ")[0]);
        }catch (Exception ignore){}
        return fps > 0 ? (1.0000 / fps) : 1;
    }

    public static float fast(float end, float start, float multiple) {
        return (1 - MathUtils.clamp((float) (deltaTime() * multiple), 0, 1)) * end + MathUtils.clamp((float) (deltaTime() * multiple), 0, 1) * start;
    }

    public static float upgradeClamp(float def, float min, float max) {
        return Math.min(Math.max(def, min), max);
    }
    public static boolean approximatelyEqual(float a, float b) {
        return Math.abs(a - b) < EPSILON;
    }

    public static double sqrt(double var1, double var3) {
        return Math.sqrt(Math.pow(var1, 2.0) + Math.pow(var3, 2.0));
    }
    public static int calc(int value) {
        MainWindow mainWindow = mc.getWindow();
        return (int) (value * mainWindow.getGuiScale() / scale);
    }

    public static double getDifferenceOf(final double num1, final double num2) {
        return (Math.abs(num2 - num1) > Math.abs(num1 - num2)) ? Math.abs(num1 - num2) : Math.abs(num2 - num1);
    }

    public static float interpolate(float a, float a1, float result1, float a2, float result2) {
        return result1 + ((a - a1) * (result2 - result1)) / (a2 - a1);
    }

    public static float interpolate3(float x, float x1, float y1, float x2, float y2, float x3, float y3) {
        float result1 = interpolate(x, x1, y1, x2, y2);
        float result2 = interpolate(x, x2, y2, x3, y3);
        return interpolate(x, x1, result1, x3, result2);
    }

    public static double easeInOutQuad(double value) {
        return value < 0.5 ? 2.0 * value * value : 1.0 - Math.pow(-2.0 * value + 2.0, 2.0) / 2.0;
    }

    public static float roundToDecimal(float value, int decimalPlaces) {
        if (decimalPlaces < 0) {
            throw new IllegalArgumentException("Decimal places must be non-negative");
        }

        double multiplier = Math.pow(10, decimalPlaces);
        return (float) (Math.round(value * multiplier) / multiplier);
    }

    public static float getTimeSpeedValue(float speed, int num) {
        return ((float) (System.currentTimeMillis() - startTime) / num * speed) % num;
    }

    public static double getDistanceXZ(Vector3d vector1, Vector3d vector2) {
        double distanceX = vector2.x - vector1.x;
        double distanceZ = vector2.z - vector1.z;
        return Math.sqrt(distanceX * distanceX + distanceZ * distanceZ);
    }

    public float calc(float value) {
        MainWindow mainWindow = mc.getWindow();
        return value * (int) (value * mainWindow.getGuiScale() / scale);
    }

    public double calc(double value) {
        MainWindow mainWindow = mc.getWindow();
        return value * (int) (value * mainWindow.getGuiScale() / scale);
    }

    public static double getBps(Entity e) {
        double prevZ = e.getZ() - e.zOld;
        double prevX = e.getX() - e.xOld;
        double prevY = e.getY() - e.yOld;
        double lastDist = Math.sqrt(prevX * prevX + prevZ * prevZ + prevY * prevY);
        double currSpeed = lastDist * 15.3571428571;
        return currSpeed;
    }

    public static float wrapDegrees(float value) {
        if ((value = (float) ((double) value % 360.0)) >= 180.0f) {
            value -= 360.0f;
        }
        if (value < -180.0f) {
            value += 360.0f;
        }
        return value;
    }

    public static float calcPercentage(float value, float min, float max) {
        if (value < min || value > max) {
            value = clamp(value, min, max);
        }

        float range = max - min;
        float percentage = (value - min) / range * 100;

        return percentage;
    }

    public static float calculateValue(float percentage, float min, float max) {
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("Процентное соотношение должно быть в пределах от 0 до 100");
        }

        float range = max - min;

        return (percentage / 100) * range + min;
    }


    public static double getRandomInRange(double max, double min) {
        return min + (max - min) * random.nextDouble();
    }

    public static BigDecimal round(float f, int times) {
        BigDecimal bd = new BigDecimal(Float.toString(f));
        bd = bd.setScale(times, 4);
        return bd;
    }

    public static int getRandomInRange(int max, int min) {
        return (int) ((double) min + (double) (max - min) * random.nextDouble());
    }

    public static boolean isEven(int number) {
        return number % 2 == 0;
    }

    public static double roundToPlace(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static double preciseRound(double value, double precision) {
        double scale = Math.pow(10.0, precision);
        return (double) Math.round(value * scale) / scale;
    }

    public static double randomNumber(double max, double min) {
        return Math.random() * (max - min) + min;
    }

    public static float randomFloat(float f2, float f3) {
        if (f2 == f3 || f3 - f2 <= 0.0f) {
            return f2;
        }
        return (float) ((double) f2 + (double) (f3 - f2) * Math.random());
    }

    public static int randomInt(int min, int max) {
        return random.nextInt(max - min) + min;
    }

    public static double getIncremental(double val, double inc) {
        double one = 1.0 / inc;
        return (double) Math.round(val * one) / one;
    }

    public static boolean isInteger(Double variable) {
        return variable == Math.floor(variable) && !Double.isInfinite(variable);
    }

    public static float lerp(float a, float b, float f) {
        return a + f * (b - a);
    }

    public static float harp(float val, float current, float f, float speed) {
        float emi = ((current - val) * (speed / 2)) > 0 ?
                Math.max(speed, Math.min(current - val, ((current - val) * (speed / 2)))) :
                Math.max(current - val, Math.min(-(speed / 2), ((current - val) * (speed / 2))));
        return f + emi;
    }

    public static float harp(float val, float current, float speed) {
        float emi = ((current - val) * (speed / 2)) > 0 ? Math.max((speed), Math.min(current - val, ((current - val) * (speed / 2)))) : Math.max(current - val, Math.min(-(speed / 2), ((current - val) * (speed / 2))));
        return val + emi;
    }

    public static double roundToDecimalPlace(double value, double inc) {
        double halfOfInc = inc / 2.0;
        double floored = Math.floor(value / inc) * inc;
        if (value >= floored + halfOfInc) {
            return new BigDecimal(Math.ceil(value / inc) * inc, MathContext.DECIMAL64).stripTrailingZeros().doubleValue();
        }
        return new BigDecimal(floored, MathContext.DECIMAL64).stripTrailingZeros().doubleValue();
    }

    public static float lerpRandom(float a, float b, float f, float random) {
        float randomFactor = MathUtils.random.nextFloat() * random;
        return a + (f * randomFactor) * (b - a);
    }

    public static float clamp(float val, float min, float max) {
        if (val <= min) {
            val = min;
        }
        if (val >= max) {
            val = max;
        }
        return val;
    }
}

