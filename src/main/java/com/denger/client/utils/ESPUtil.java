package com.denger.client.utils;

import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.math.vector.Vector4f;

import static com.denger.client.MainNative.mc;

public class ESPUtil {
    private static Matrix4f projectionMatrix = new Matrix4f();
    private static Matrix4f viewMatrix = new Matrix4f();
    private static Matrix4f projectionViewMatrix = new Matrix4f();

    public static void setProjectionViewMatrix(Matrix4f projection, Matrix4f view) {
        projectionMatrix = projection.copy();
        viewMatrix = view.copy();

        projectionViewMatrix = projectionMatrix.copy();
        projectionViewMatrix.multiply(viewMatrix);
//    projectionViewMatrix.invert();
    }

    public static Vector3d toScreen(double x, double y, double z) {
        // 0.05 = near plane, which i found in GameRenderer::getProjectionMatrix
        final float NEAR_PLANE = 0.05f;

        final double screenWidth = mc.getWindow().getGuiScaledWidth();
        final double screenHeight = mc.getWindow().getGuiScaledHeight();

        Vector3d camera = mc.getEntityRenderDispatcher().camera.getPosition();
        Vector3d dir = camera.subtract(x, y, z);

        Vector4f pos = new Vector4f((float) dir.x, (float) dir.y, (float) dir.z, 1.f);
        pos.transform(projectionViewMatrix);

        float w = pos.w();
        if (w < NEAR_PLANE && w != 0) {
            pos.perspectiveDivide();
        } else {
            float scale = (float) Math.max(screenWidth, screenHeight);
            pos.set((pos.x() * -1 * scale),(pos.y() * -1 * scale),pos.z(),pos.w());
         }

        double hw = screenWidth / 2.d;
        double hh = screenHeight / 2.d;
        double pointX = (hw * pos.x()) + (pos.x() + hw);
        double pointY = -(hh * pos.y()) + (pos.y() + hh);

        return new Vector3d(pointX, pointY,
                (pointX >= 0
                        && pointX < screenWidth
                        && pointY >= 0
                        && pointY < screenHeight ? 1d : 0d));
    }

    public static Vector3d toScreen(Vector3d vec) {
        return toScreen(vec.x, vec.y, vec.z);
    }

    public static Vector3d toScreen(Vector3i vec) {
        return toScreen(vec.getX(), vec.getY(), vec.getZ());
    }

    public static Vector3d multiplyBy(Vector3d vec1, Vector3d vec2) {
        return new Vector3d(vec1.x * vec2.x, vec1.y * vec2.y, vec1.z * vec2.z);
    }

    public static Vector3d copy(Vector3d toCopy) {
        return new Vector3d(toCopy.x, toCopy.y, toCopy.z);
    }

    public static double getCrosshairDistance(Vector3d eyes, Vector3d directionVec, Vector3d pos) {
        return pos.subtract(eyes).normalize().subtract(directionVec).length();
    }

    public static Vector3d toFPIVector(Vector3i vec) {
        return new Vector3d(vec.getX(), vec.getY(), vec.getZ());
    }
}
