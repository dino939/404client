
package com.denger.client.utils.rect;


import com.denger.client.utils.ColorUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IngameGui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL46;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import static com.denger.client.Main.mc;
import static com.mojang.blaze3d.platform.GlStateManager.*;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;


public class RenderUtil {

    public static void setupRender() {
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.disableAlphaTest();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.shadeModel(7425);
        RenderSystem.depthMask(false);
    }

    public static void drawImageId(MatrixStack ms, int url, float x, float y, float width, float height, int color) {
        _enableBlend();
        _blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        resetColor();
        RenderSystem.color4f(ColorUtil.r(color), ColorUtil.g(color), ColorUtil.b(color), ColorUtil.a(color));
        _bindTexture(url);
        ShaderUtil.drawQuads(ms, x, y, width, height);
        _bindTexture(0);

        _disableBlend();
    }

    public static void drawLine(double StartX, double StartY, double EndX, double EndY, float thikness, int color) {
        RenderUtil.setupRenderRect();
        GL20.glEnable(2848);
        GL20.glLineWidth(thikness);
        RenderUtil.glColor(color);
        GL20.glBegin(2);
        GL20.glVertex2d(StartX, StartY);
        GL20.glVertex2d(EndX, EndY);
        GL20.glEnd();
        GL20.glDisable(2848);
        RenderUtil.endRenderRect();
    }

    public static void endRender() {
        RenderSystem.depthMask(true);
        RenderSystem.shadeModel(7424);
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        RenderSystem.enableAlphaTest();
        RenderSystem.enableTexture();
        RenderSystem.enableDepthTest();
    }

    public static void setScaleRender(int scale) {
        MainWindow mw = Minecraft.getInstance().getWindow();
        mw.setGuiScale(scale);
        RenderSystem.clear(256, Minecraft.ON_OSX);
        GlStateManager._matrixMode(5889);
        GlStateManager._loadIdentity();
        GlStateManager._ortho(0D, mw.getGuiScaledWidth(), mw.getGuiScaledHeight(),
                0, 1000D, 30000D);
        GlStateManager._matrixMode(5888);
        GlStateManager._loadIdentity();
        GlStateManager._translatef(0, 0, -2000);
    }

    public static void setScaleRenderStandar() {
        MainWindow mw = Minecraft.getInstance().getWindow();
        int scale = mw.calculateScale(mc.options.guiScale, mc.isEnforceUnicode());
        mw.setGuiScale(scale);
        RenderSystem.clear(256, Minecraft.ON_OSX);
        GlStateManager._matrixMode(5889);
        GlStateManager._loadIdentity();
        GlStateManager._ortho(0D, mw.getGuiScaledWidth(), mw.getGuiScaledHeight(),
                0, 1000D, 30000D);
        GlStateManager._matrixMode(5888);
        GlStateManager._loadIdentity();
        GlStateManager._translatef(0, 0, -2000);
    }


    public static void StartScissor(float x, float y, float width, float height) {
        Minecraft mc = Minecraft.getInstance();
        double scale = mc.getWindow().getGuiScale();
        double finalHeight = (double) height * scale;
        double finalY = (double) ((float) mc.getWindow().getGuiScaledHeight() - y) * scale;
        double finalX = (double) x * scale;
        double finalWidth = (double) width * scale;
        RenderSystem.enableScissor(((int) finalX), ((int) (finalY - finalHeight)), ((int) finalWidth), ((int) finalHeight));
    }

    public static void stopScissor() {
        RenderSystem.disableScissor();
    }

    public static int glColor(int color) {
        float alpha = (float) (color >> 24 & 0xFF) / 255.0f;
        float red = (float) (color >> 16 & 0xFF) / 255.0f;
        float green = (float) (color >> 8 & 0xFF) / 255.0f;
        float blue = (float) (color & 0xFF) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
        return color;
    }

    public static void setUp3D(boolean blend) {
        GL46.glDepthMask(false);
        GL46.glDisable(2884);
        GL46.glEnable(3042);
        GL46.glDisable(3008);
        if (blend) {
            GL46.glEnable(GL46.GL_BLEND);
            GL46.glBlendFunc(GL46.GL_SRC_ALPHA, GL46.GL_ONE);
        }


    }

    public static void end3D(boolean blend) {
        if (blend) {
            GL46.glBlendFunc(GL46.GL_SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.value);
            GL46.glDisable(GL46.GL_BLEND);
        }
        GL46.glEnable(3008);
        GL46.glDisable(3042);
        GL46.glEnable(2884);
        GL46.glDepthMask(true);
    }

    public static void drawImage(MatrixStack ms, ResourceLocation rl, float x, float y, float width, float height) {
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.blendFunc(770, 771);
        mc.getTextureManager().bind(rl);
        IngameGui.blit(ms, ((int) x), ((int) y), 0.0f, 0.0f, ((int) width), ((int) height), ((int) width), ((int) height));
        RenderSystem.disableBlend();

        RenderSystem.enableDepthTest();
    }

    public static void drawImage(MatrixStack ms, ResourceLocation rl, float x, float y, float width, float height, float alpha) {
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.color4f((float) 1.0f, (float) 1.0f, (float) 1.0f, alpha / 255);
        RenderSystem.blendFunc((int) 770, (int) 771);
        mc.getTextureManager().bind(rl);
        IngameGui.blit(ms, ((int) x), ((int) y), 0.0f, 0.0f, ((int) width), ((int) height), ((int) width), ((int) height));
        RenderSystem.disableBlend();
        RenderSystem.enableDepthTest();
    }

    public static void drawHead(MatrixStack ms, PlayerEntity player, int x, int y, float width, float height) {
        if (mc.level == null) return;
        ResourceLocation texture = ((mc.getConnection()).getPlayerInfo(player.getUUID())).getSkinLocation();
        if (texture == null) return;
        RenderSystem.clearColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.blendFunc(770, 771);
        mc.getTextureManager().bind(texture);
        IngameGui.blit(ms, x, y, (int) width, (int) height, 72, 72, 8, 8, 64, 64);
        RenderSystem.disableBlend();
        RenderSystem.enableDepthTest();
    }

    public static void drawTexture(MatrixStack ms, ResourceLocation texture, int x, int y, float width, float height) {
        if (mc.level == null) return;
        RenderSystem.clearColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.blendFunc(770, 771);
        mc.getTextureManager().bind(texture);
        IngameGui.blit(ms, x, y, (int) width, (int) height, 72, 72, 8, 8, 64, 64);
        RenderSystem.disableBlend();
        RenderSystem.enableDepthTest();
    }

    public static void RSColor(final int n) {
        RenderSystem.color4f((n >> 16 & 0xFF) / 255.0f, (n >> 8 & 0xFF) / 255.0f, (n & 0xFF) / 255.0f, (n >> 24 & 0xFF) / 255.0f);
    }

    public static void drawShadowCircle(MatrixStack ms, float x, float y, float radius, int slices, int size, int color) {

        RenderUtil.setupRender();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuilder();
        float chlen = 3.0f;
        GL11.glPointSize((float) (size * 2));
        GL11.glEnable(2832);
        bufferbuilder.begin(0, DefaultVertexFormats.POSITION_COLOR);
        int i = 0;
        while (i <= slices) {
            bufferbuilder.vertex(ms.last().pose(), (float) ((double) x + (double) radius * Math.cos((double) i * Math.PI / 180.0)), (float) ((double) y + (double) radius * Math.sin((double) i * Math.PI / 180.0)), 0.0f).color(ColorUtil.r(color), ColorUtil.g(color), ColorUtil.b(color), ColorUtil.a(color)).endVertex();
            i = (int) ((float) i + chlen);
        }
        tessellator.end();
        GL11.glPointSize(1.0f);
        GL11.glDisable(2832);
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderUtil.endRender();
    }

    public static void drawSetupShaderGL() {
        GL11.glDisable(GL11.GL_TEXTURE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }

    public static void drawFinishShaderGL() {
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        glColor(-1);
    }

    public static void drawSetupShader() {
        GlStateManager._disableTexture();
        GlStateManager._enableBlend();
        GlStateManager._blendFunc(770, 771);
    }

    public static void drawFinishShader() {
        GlStateManager._enableTexture();
        GlStateManager._disableBlend();
        RSColor(-1);
    }

    public static void setupRenderRect() {
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.disableAlphaTest();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.shadeModel(7425);
        RenderSystem.depthMask(false);
    }

    public static void endRenderRect() {
        RenderSystem.depthMask(true);
        RenderSystem.shadeModel(7424);
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        RenderSystem.enableAlphaTest();
        RenderSystem.enableTexture();
        RenderSystem.enableDepthTest();
    }

    public static int loadTexture(BufferedImage image) {
        int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
        ByteBuffer buffer = BufferUtils.createByteBuffer((int) (pixels.length * 4));
        for (int pixel : pixels) {
            buffer.put((byte) (pixel >> 16 & 0xFF));
            buffer.put((byte) (pixel >> 8 & 0xFF));
            buffer.put((byte) (pixel & 0xFF));
            buffer.put((byte) (pixel >> 24 & 0xFF));
        }
        buffer.flip();
        int textureID = GL30.glGenTextures();
        GL30.glBindTexture(3553, (int) textureID);
        GL30.glTexParameteri(3553, (int) 10242, (int) 33071);
        GL30.glTexParameteri(3553, (int) 10243, (int) 33071);
        GL30.glTexParameteri(3553, (int) 10241, (int) 9729);
        GL30.glTexParameteri(3553, (int) 10240, (int) 9729);
        GL30.glTexImage2D(3553, 0, (int) 32856, (int) image.getWidth(), (int) image.getHeight(), (int) 0, (int) 6408, (int) 5121, (ByteBuffer) buffer);
        GL30.glBindTexture(3553, 0);
        return textureID;
    }

    public static void color(float red, float green, float blue, float alpha) {
        GlStateManager._color4f(red, green, blue, alpha);
    }

    public static void resetColor() {
        GlStateManager._clearCurrentColor();
    }

    public static void scale(float posX, float posY, float width, float height, float scale, Runnable runnable) {
        GlStateManager._pushMatrix();
        GlStateManager._translated(posX + width / 2, posY + height / 2, 0);
        GlStateManager._scaled(scale, scale, 1);
        GlStateManager._translated(-posX - width / 2, -posY - height / 2, 0);
        runnable.run();
        GlStateManager._popMatrix();
    }

    public static void rotate(float posX, float posY, float width, float height, float angleDegrees, Runnable runnable) {
        GlStateManager._pushMatrix();
        GlStateManager._translated(posX + width / 2, posY + height / 2, 0);
        GlStateManager._rotatef(angleDegrees, 0, 0, 1);
        GlStateManager._translated(-posX - width / 2, -posY - height / 2, 0);
        runnable.run();
        GlStateManager._popMatrix();
    }

    public static void scale(MatrixStack ms, float posX, float posY, float width, float height, float scale, Runnable runnable) {
        float centerX = posX + width / 2;
        float centerY = posY + height / 2;

        ms.pushPose();
        ms.translate(centerX, centerY, 0);
        ms.scale(scale, scale, scale);
        ms.translate(-centerX, -centerY, 0);
        runnable.run();
        ms.popPose();
    }

    public static void rotate(MatrixStack ms, float posX, float posY, float width, float height, float angleDegrees, Runnable runnable) {
        float centerX = posX + width / 2;
        float centerY = posY + height / 2;

        ms.translate(centerX, centerY, 0);
        ms.mulPose(Vector3f.ZP.rotationDegrees(angleDegrees));
        ms.translate(-centerX, -centerY, 0);
        runnable.run();
    }


    public static void renderBox(MatrixStack ms, float xx, float yy, float zz, int color) {
        double x = (double) xx - mc.getEntityRenderDispatcher().camera.getPosition().x;
        double y = (double) yy - mc.getEntityRenderDispatcher().camera.getPosition().y;
        double z = (double) zz - mc.getEntityRenderDispatcher().camera.getPosition().z;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuilder();
        ms.pushPose();
        ms.translate(x, y, z);
        RenderSystem.disableDepthTest();
        setupRender();
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.vertex(ms.last().pose(), 0.0f, 0.0f, 0.0f).color(ColorUtil.r(color), ColorUtil.g(color), ColorUtil.b(color), ColorUtil.a(color)).endVertex();
        bufferbuilder.vertex(ms.last().pose(), 1.0f, 0.0f, 0.0f).color(ColorUtil.r(color), ColorUtil.g(color), ColorUtil.b(color), ColorUtil.a(color)).endVertex();
        bufferbuilder.vertex(ms.last().pose(), 1.0f, 1.0f, 0.0f).color(ColorUtil.r(color), ColorUtil.g(color), ColorUtil.b(color), ColorUtil.a(color)).endVertex();
        bufferbuilder.vertex(ms.last().pose(), 0.0f, 1.0f, 0.0f).color(ColorUtil.r(color), ColorUtil.g(color), ColorUtil.b(color), ColorUtil.a(color)).endVertex();
        bufferbuilder.vertex(ms.last().pose(), 0.0f, 0.0f, 0.0f).color(ColorUtil.r(color), ColorUtil.g(color), ColorUtil.b(color), ColorUtil.a(color)).endVertex();
        tessellator.end();
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.vertex(ms.last().pose(), 0.0f, 0.0f, 1.0f).color(ColorUtil.r(color), ColorUtil.g(color), ColorUtil.b(color), ColorUtil.a(color)).endVertex();
        bufferbuilder.vertex(ms.last().pose(), 1.0f, 0.0f, 1.0f).color(ColorUtil.r(color), ColorUtil.g(color), ColorUtil.b(color), ColorUtil.a(color)).endVertex();
        bufferbuilder.vertex(ms.last().pose(), 1.0f, 1.0f, 1.0f).color(ColorUtil.r(color), ColorUtil.g(color), ColorUtil.b(color), ColorUtil.a(color)).endVertex();
        bufferbuilder.vertex(ms.last().pose(), 0.0f, 1.0f, 1.0f).color(ColorUtil.r(color), ColorUtil.g(color), ColorUtil.b(color), ColorUtil.a(color)).endVertex();
        tessellator.end();
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.vertex(ms.last().pose(), 0.0f, 0.0f, 0.0f).color(ColorUtil.r(color), ColorUtil.g(color), ColorUtil.b(color), ColorUtil.a(color)).endVertex();
        bufferbuilder.vertex(ms.last().pose(), 0.0f, 0.0f, 1.0f).color(ColorUtil.r(color), ColorUtil.g(color), ColorUtil.b(color), ColorUtil.a(color)).endVertex();
        bufferbuilder.vertex(ms.last().pose(), 0.0f, 1.0f, 1.0f).color(ColorUtil.r(color), ColorUtil.g(color), ColorUtil.b(color), ColorUtil.a(color)).endVertex();
        bufferbuilder.vertex(ms.last().pose(), 0.0f, 1.0f, 0.0f).color(ColorUtil.r(color), ColorUtil.g(color), ColorUtil.b(color), ColorUtil.a(color)).endVertex();
        tessellator.end();
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.vertex(ms.last().pose(), 1.0f, 0.0f, 0.0f).color(ColorUtil.r(color), ColorUtil.g(color), ColorUtil.b(color), ColorUtil.a(color)).endVertex();
        bufferbuilder.vertex(ms.last().pose(), 1.0f, 0.0f, 1.0f).color(ColorUtil.r(color), ColorUtil.g(color), ColorUtil.b(color), ColorUtil.a(color)).endVertex();
        bufferbuilder.vertex(ms.last().pose(), 1.0f, 1.0f, 1.0f).color(ColorUtil.r(color), ColorUtil.g(color), ColorUtil.b(color), ColorUtil.a(color)).endVertex();
        bufferbuilder.vertex(ms.last().pose(), 1.0f, 1.0f, 0.0f).color(ColorUtil.r(color), ColorUtil.g(color), ColorUtil.b(color), ColorUtil.a(color)).endVertex();
        bufferbuilder.color(255, 255, 255, 255);
        tessellator.end();
        RenderSystem.enableDepthTest();
        endRender();
        ms.popPose();
    }

}

