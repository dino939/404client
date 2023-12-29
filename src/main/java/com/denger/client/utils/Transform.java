package com.denger.client.utils;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.util.math.vector.Quaternion;

public class Transform {

    public static void translate(double x, double y, double z) {
        GlStateManager._translated(x, y, z);
    }

    public static void rotate(float angle, float x, float y, float z) {
        RenderSystem.rotatef(angle, x, y, z);
    }

    public static void scale(double x, double y, double z) {
        RenderSystem.scaled(x, y, z);
    }

    public static void stop() {
        GlStateManager._popMatrix();
    }

    public static void translate(MatrixStack ms, double x, double y, double z) {
        ms.pushPose();
        ms.translate(x, y, z);
    }

    public static void rotate(MatrixStack ms, float angle, float x, float y, float z) {
        ms.mulPose(new Quaternion(angle, x, y, z));
    }

    public static void scale(MatrixStack ms, float x, float y, float z) {
        ms.scale(x, y, z);
    }

    public static void stop(MatrixStack ms) {
        ms.popPose();
    }

}
