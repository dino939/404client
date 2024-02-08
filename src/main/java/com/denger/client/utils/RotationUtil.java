package com.denger.client.utils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;

import static com.denger.client.Main.mc;


public class RotationUtil {
    public static float[] getRotationsNeeded(PlayerEntity playerEntity, double distance) {
        double diffX = playerEntity.getX() - mc.player.getX(), diffZ = playerEntity.getZ() - mc.player.getZ(), diffY = playerEntity.getY() + playerEntity.getEyeHeight() - (mc.player.getY() + mc.player.getEyeHeight()) - 2 + distance / 2, dist = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float) (Math.atan2(diffZ, diffX) * 180 / Math.PI) - 90, pitch = (float) -(Math.atan2(diffY, dist) * 180 / Math.PI);
        return new float[]{mc.player.yRot + MathHelper.rotWrap((int) (yaw - mc.player.yRot)), mc.player.xRot + MathHelper.rotWrap((int) (pitch - mc.player.xRot))};
    }

    public static float getSensitivity(float rot) {
        return RotationUtil.round(rot) * RotationUtil.lese();
    }

    public static float round(float delta) {
        return Math.round(delta / RotationUtil.lese());
    }

    public static float lese() {
        return (float) ((double) RotationUtil.getLastSensivity() * 0.15);
    }

    public static float getLastSensivity() {
        float sensivity = (float) (mc.options.mouseWheelSensitivity * 0.6 + 0.2);
        return sensivity * sensivity * sensivity * 8.0f;
    }

}
