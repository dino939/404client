
package com.denger.client.utils;

import net.minecraft.client.MainWindow;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.vector.Vector3d;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Random;

import static com.denger.client.MainNative.mc;

public class MathUtils {
    private static final Random random = new Random();

    public static int scale = 2;

    public static double getNormalDouble(double d, int numberAfterZopyataya) {
        return (new BigDecimal(d)).setScale(numberAfterZopyataya, RoundingMode.HALF_EVEN).doubleValue();
    }

    public static double getNormalDouble(double d) {
        return (new BigDecimal(d)).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
    }
    public static double sq(double a) {
        return a * a;
    }

    public static double cathet(double h, double a) {
        return Math.sqrt(sq(h) - sq(a));
    }

    public static int calc(int value) {
        MainWindow mainWindow = mc.getWindow();
        return (int) (value * mainWindow.getGuiScale() / scale);
    }
    public static double getDifferenceOf(final double num1, final double num2) {
        return (Math.abs(num2 - num1) > Math.abs(num1 - num2)) ? Math.abs(num1 - num2) : Math.abs(num2 - num1);
    }
    public static double easeInOutQuad(double x, int step) {
        return x < 0.5 ? 2.0 * x * x : 1.0 - Math.pow(-2.0 * x + 2.0, step) / 2.0;
    }
    public static float roundToDecimal(float value, int decimalPlaces) {
        if (decimalPlaces < 0) {
            throw new IllegalArgumentException("Decimal places must be non-negative");
        }

        double multiplier = Math.pow(10, decimalPlaces);
        return (float) (Math.round(value * multiplier) / multiplier);
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
            throw new IllegalArgumentException("Значение должно быть в пределах от " + min + " до " + max);
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

    public static int randomize(int max, int min) {
        return -min + (int) (Math.random() * (double) (max - -min + 1));
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

    public static float[] constrainAngle(float[] vector) {
        vector[0] = vector[0] % 360.0f;
        vector[1] = vector[1] % 360.0f;
        while (vector[0] <= -180.0f) {
            vector[0] = vector[0] + 360.0f;
        }
        while (vector[1] <= -180.0f) {
            vector[1] = vector[1] + 360.0f;
        }
        while (vector[0] > 180.0f) {
            vector[0] = vector[0] - 360.0f;
        }
        while (vector[1] > 180.0f) {
            vector[1] = vector[1] - 360.0f;
        }
        return vector;
    }

    public static double randomize(double min, double max) {
        double shifted;
        Random random = new Random();
        double range = max - min;
        double scaled = random.nextDouble() * range;
        if (scaled > max) {
            scaled = max;
        }
        if ((shifted = scaled + min) > max) {
            shifted = max;
        }
        return shifted;
    }


    public static float percentLerp(float initialValue, float finalValue, float speed, float acceleration) {

        float speedloc = MathUtils.calculateValue(acceleration,0,speed);


        return lerp(initialValue, finalValue, speedloc);
    }

    public static float harmonic(float startValue, float endValue, float speed) {

        float to = startValue + speed/2;
        if (to > endValue) {
            to = endValue;
        }

        return to;
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
        Random randomz = new Random();
        float randomFactor = randomz.nextFloat() * random;
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

