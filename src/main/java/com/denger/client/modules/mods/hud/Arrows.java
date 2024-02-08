package com.denger.client.modules.mods.hud;


import com.denger.client.another.hooks.forge.even.addevents.Event2D;
import com.denger.client.another.settings.SettingTarget;
import com.denger.client.another.settings.sett.FloatSetting;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.ModuleTarget;
import com.denger.client.utils.AnimationUtil;
import com.denger.client.utils.ColorUtil;
import com.denger.client.utils.MathUtils;
import com.denger.client.utils.Utils;
import com.denger.client.utils.rect.RenderUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
 import net.minecraft.client.MainWindow;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import com.denger.client.modules.another.Category;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import static com.denger.client.Main.*;

@ModuleTarget(ModName = "Bsspxt", category = Category.HUD, enable = true, bind = GLFW.GLFW_KEY_R, cooldown = 300)
public class Arrows extends Module {
    @SettingTarget(name = "Размер")
    FloatSetting length = new FloatSetting().setMin(0).setMax(2).setVal(1.2f).seType("size");
    AnimationUtil anim = new AnimationUtil(0, 0, 10000);

    @Override
    public void onEnable() {
        anim.setDurationMillis( 350);
        anim.goTo(255);
    }

    @Override
    public void preDisable() {
        anim.goTo(0);
    }


    public void onRender2D(Event2D event) {

           float length2 = length.getVal();
        MainWindow mainWindow = mc.getWindow();
        int noommom = 100;
        double loaddist = 0.2f;
        double pTicks = getInstance.timer.partialTick;
        assert mc.level != null;
        float val = anim.getAnim();
        float width = (float) MathUtils.calc(mainWindow.getGuiScaledWidth());
        float height = (float) MathUtils.calc(mainWindow.getGuiScaledHeight());

            RenderUtil.scale(0, 0, width, height, MathUtils.clamp(val / 255, 0.55f, 1), () -> {
            mc.level.entitiesForRendering().forEach((entity -> {
                if (entity == mc.player) return;
                double xOffset = mainWindow.getGuiScaledWidth() / 2F - (noommom / 2.04);
                double yOffset = mainWindow.getGuiScaledHeight() / 2F - (noommom / 1.983);
                double NoomMomPosX = mc.player.getX();
                double NoomMomPosZ = mc.player.getZ();
                double posX = (((entity.getX() + (entity.getX() - entity.xOld) * pTicks) - NoomMomPosX) * loaddist);
                double posZ = (((entity.getZ() + (entity.getZ() - entity.zOld) * pTicks) - NoomMomPosZ) * loaddist);
                double cos = Math.cos(mc.player.yRot * (Math.PI * 2 / 360));
                double sin = Math.sin(mc.player.yRot * (Math.PI * 2 / 360));
                double rotY = -(posZ * cos - posX * sin);
                double rotX = -(posX * cos + posZ * sin);
                double var7 = 0 - rotX;
                double var9 = 0 - rotY;
                if (MathHelper.sqrt(var7 * var7 + var9 * var9) < noommom / 2F - 4) {
                    double anglenoommom = (Math.atan2(rotY - 0, rotX - 0) * 180 / Math.PI);
                    double x = ((noommom / 2F) * Math.cos(Math.toRadians(anglenoommom))) + xOffset + noommom / 2F;
                    double y = ((noommom / 2F) * Math.sin(Math.toRadians(anglenoommom))) + yOffset + noommom / 2F;
                    MatrixStack ms = new MatrixStack();
                    MatrixStack ms2 = new MatrixStack();
                    GL11.glPushMatrix();
                    Utils.setupRender();

                    GL11.glTranslated(x, y, 0.0);
                    GL11.glRotatef((float) anglenoommom, 0.0f, 0.0f, 1.0f);
                    ms.scale(length2, length2, length2);
                    triangle(ms, 2, getInstance.theme.getC2().getRGB(), getInstance.theme.getC().getRGB(), val);
                    ms2.scale(length2 - 0.75f, length2 - 0.75f, length2 - 0.75f);
                    triangle(ms2, 5, getInstance.theme.getC2().getRGB(), getInstance.theme.getC().getRGB(), val);
                    Utils.endRender();
                    GL11.glPopMatrix();
                }
            }));
        });

    }

    private void triangle(MatrixStack ms, int mode, int color, int color2, float alpha) {
        float cx = 0, cy = 0, r = 3, n = r;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuilder();
        cx = (float) ((double) cx * 2.0);
        cy = (float) ((double) cy * 2.0);
        float b = 6.283185f / n;
        float p = (float) Math.cos(b);
        float s = (float) Math.sin(b);
        float x = r *= 3.0f;
        float y = 0.0f;
        RenderUtil.setupRenderRect();
        ms.pushPose();
        ms.scale(0.5f, 0.5f, 0.5f);
        bufferbuilder.begin(mode, DefaultVertexFormats.POSITION_COLOR);
        RenderSystem.lineWidth(1.5f);
        int ii = 0;
        while ((float) ii < n) {
            int c;
            if (ii == 0) {
                c = ColorUtil.swapAlpha(color, alpha);
            } else {
                c = ColorUtil.swapAlpha(color2, alpha);
            }
            ms.pushPose();
            bufferbuilder.vertex(ms.last().pose(), x + cx, y + cy, 0.0f).color(ColorUtil.r(c), ColorUtil.g(c), ColorUtil.b(c), ColorUtil.a(c)).endVertex();
            ms.popPose();
            float t = x;
            x = p * x - s * y;
            y = s * t + p * y;
            ++ii;

        }
        tessellator.end();
        ms.scale(2.0f, 2.0f, 2.0f);
        ms.popPose();
        RenderUtil.endRenderRect();
    }
}
