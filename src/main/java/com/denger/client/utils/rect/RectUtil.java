package com.denger.client.utils.rect;

import com.denger.client.utils.ColorUtil;
import com.denger.client.utils.Utils;
import com.denger.client.utils.WebUtilsNative;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MainWindow;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.HashMap;

import static com.denger.client.MainNative.mc;
import static com.denger.client.utils.TextureUtil.uploadTextureImageAllocate;
import static com.denger.client.utils.rect.RenderUtil.resetColor;
import static com.mojang.blaze3d.platform.GlStateManager.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11C.GL_QUADS;

public class RectUtil {
    public static ShaderUtil roundedShader = new ShaderUtil("roundedRect");
    private static final ShaderUtil roundedGradientShader = new ShaderUtil("roundedRectGradient");
    public static ShaderUtil roundedOutlineShader = new ShaderUtil("roundRectOutline");
    public static ShaderUtil cirlce = new ShaderUtil("jumpcirlce");
    private static final ShaderUtil ROUNDED_TEXTURE = new ShaderUtil("rounded_texture");
    public static final ShaderUtil OUTLINE = new ShaderUtil("outline");
    public static final HashMap<Integer, Integer> glowCache = new HashMap();
    public static HashMap<Integer, Integer> shadowCache2 = new HashMap();

    public static void drawFilledCircle(int xx, int yy, float radius, int color) {
        float f = (float) (color >> 24 & 255) / 255.0F;
        float f1 = (float) (color >> 16 & 255) / 255.0F;
        float f2 = (float) (color >> 8 & 255) / 255.0F;
        float f3 = (float) (color & 255) / 255.0F;
        int sections = 50;
        double dAngle = 6.283185307179586D / (double) sections;
        GL11.glPushAttrib(8192);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glBegin(6);

        for (int i = 0; i < sections; ++i) {
            float x = (float) ((double) radius * Math.sin((double) i * dAngle));
            float y = (float) ((double) radius * Math.cos((double) i * dAngle));
            GL11.glColor4f(f1, f2, f3, f);
            GL11.glVertex2f((float) xx + x, (float) yy + y);
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnd();
        GL11.glPopAttrib();
    }


    public static void drawRound(float x, float y, float width, float height, float radius, int color) {
        RenderUtil.drawSetupShader();
        roundedShader.load();
        ShaderUtil.setupRoundedRectUniforms(x, y, width, height, radius, roundedShader);
        roundedShader.setUniformi("blur", 0);
        roundedShader.setUniformf("color", ColorUtil.r(color), ColorUtil.g(color), ColorUtil.b(color), ColorUtil.a(color));
        ShaderUtil.drawQuads(x, y, width, height);
        roundedShader.unload();
        RenderUtil.drawFinishShader();
    }


    public static void drawJumpCircle(float time) {
        RenderUtil.drawSetupShader();
        cirlce.load();
        MainWindow mainWindow = mc.getWindow();
        GL30.glUniform2f(GL30.glGetUniformLocation(cirlce.getProgramID(), "resolution"), mainWindow.getGuiScaledWidth(), mainWindow.getGuiScaledHeight());
        GL30.glUniform1f(GL30.glGetUniformLocation(cirlce.getProgramID(), "time"), (System.currentTimeMillis() - cirlce.getTime()) / 1000f / time);
        ShaderUtil.drawQuads();
        cirlce.unload();
        RenderUtil.drawFinishShader();
    }

    public static void drawRound(MatrixStack ms, float x, float y, float width, float height, float radius, int color) {
        RenderUtil.drawSetupShader();
        roundedShader.load();
        ShaderUtil.setupRoundedRectUniforms(x, y, width, height, radius, roundedShader);
        roundedShader.setUniformi("blur", 1);
        roundedShader.setUniformf("color", ColorUtil.r(color), ColorUtil.g(color), ColorUtil.b(color), ColorUtil.a(color));
        ShaderUtil.drawQuads(x, y, width, height);
        roundedShader.unload();
        RenderUtil.drawFinishShader();
    }

    public static void drawGradientRound(float x, float y, float width, float height, float radius, int bottomLeft, int topLeft, int bottomRight, int topRight) {
        RenderUtil.drawSetupShader();
        roundedGradientShader.load();
        ShaderUtil.setupRoundedRectUniforms(x, y, width, height, radius, roundedGradientShader);
        // Bottom Left
        roundedGradientShader.setUniformf("color1", ColorUtil.r(bottomLeft), ColorUtil.g(bottomLeft), ColorUtil.b(bottomLeft), ColorUtil.a(bottomLeft));
        //Top left
        roundedGradientShader.setUniformf("color2", ColorUtil.r(topLeft), ColorUtil.g(topLeft), ColorUtil.b(topLeft), ColorUtil.a(topLeft));
        //Bottom Right
        roundedGradientShader.setUniformf("color3", ColorUtil.r(bottomRight), ColorUtil.g(bottomRight), ColorUtil.b(bottomRight), ColorUtil.a(bottomRight));
        //Top Right
        roundedGradientShader.setUniformf("color4", ColorUtil.r(topRight), ColorUtil.g(topRight), ColorUtil.b(topRight), ColorUtil.a(topRight));
        ShaderUtil.drawQuads(x, y, width, height);
        roundedGradientShader.unload();
        RenderUtil.drawFinishShader();
    }

    public static void drawGradientRound(MatrixStack ms, float x, float y, float width, float height, float radius, int bottomLeft, int topLeft, int bottomRight, int topRight) {
        RenderUtil.drawSetupShader();
        roundedGradientShader.load();
        ShaderUtil.setupRoundedRectUniforms(x, y, width, height, radius, roundedGradientShader);
        // Bottom Left
        roundedGradientShader.setUniformf("color1", ColorUtil.r(bottomLeft), ColorUtil.g(bottomLeft), ColorUtil.b(bottomLeft), ColorUtil.a(bottomLeft));
        //Top left
        roundedGradientShader.setUniformf("color2", ColorUtil.r(topLeft), ColorUtil.g(topLeft), ColorUtil.b(topLeft), ColorUtil.a(topLeft));
        //Bottom Right
        roundedGradientShader.setUniformf("color3", ColorUtil.r(bottomRight), ColorUtil.g(bottomRight), ColorUtil.b(bottomRight), ColorUtil.a(bottomRight));
        //Top Right
        roundedGradientShader.setUniformf("color4", ColorUtil.r(topRight), ColorUtil.g(topRight), ColorUtil.b(topRight), ColorUtil.a(topRight));
        ShaderUtil.drawQuads(x, y, width, height);
        roundedGradientShader.unload();
        RenderUtil.drawFinishShader();
    }


    public static void drawRoundOutline(float x, float y, float width, float height, float radius, float outlineThickness, int color, int outlineColor) {
        RenderUtil.drawSetupShader();
        roundedOutlineShader.load();
        MainWindow mainWindow = mc.getWindow();
        ShaderUtil.setupRoundedRectUniforms(x, y, width, height, radius, roundedOutlineShader);
        roundedOutlineShader.setUniformf("outlineThickness", (float) (outlineThickness * mainWindow.getGuiScale()));
        roundedOutlineShader.setUniformf("color", ColorUtil.r(color), ColorUtil.g(color), ColorUtil.b(color), ColorUtil.a(color));
        roundedOutlineShader.setUniformf("outlineColor", ColorUtil.r(outlineColor), ColorUtil.g(outlineColor), ColorUtil.b(outlineColor), ColorUtil.a(outlineColor));
        ShaderUtil.drawQuads(x - (2 + outlineThickness), y - (2 + outlineThickness), width + (4 + outlineThickness * 2), height + (4 + outlineThickness * 2));
        roundedOutlineShader.unload();
        RenderUtil.drawFinishShader();
    }

    public static void drawline(float x, float width, float y, float linewidth, int color) {
        MatrixStack ms = new MatrixStack();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuilder();
        RenderSystem.disableDepthTest();
        RenderUtil.setupRenderRect();
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        RenderSystem.lineWidth(linewidth);
        ms.pushPose();
        bufferbuilder.vertex(ms.last().pose(), x, y, 0).color(ColorUtil.r(color), ColorUtil.g(color), ColorUtil.b(color), ColorUtil.a(color)).endVertex();
        ms.popPose();
        ms.pushPose();
        ms.translate(x + width, y, 0);
        bufferbuilder.vertex(ms.last().pose(), 0.0f, 0.0f, 0.0f).color(ColorUtil.r(color), ColorUtil.g(color), ColorUtil.b(color), ColorUtil.a(color)).endVertex();
        ms.popPose();
        bufferbuilder.color(255, 255, 255, 255);
        tessellator.end();
        RenderSystem.enableDepthTest();
        RenderUtil.endRenderRect();

    }

    public void drawCircle(MatrixStack ms, float x, float y, int color) {
        int i;
        RenderUtil.setupRenderRect();
        // RenderUtil.glColor(color);
        BufferBuilder builder = Tessellator.getInstance().getBuilder();
        builder.begin(6, DefaultVertexFormats.POSITION_COLOR);
        Matrix4f matrix4f = ms.last().pose();
        int c = color;
        float radius = 1;
        for (i = 0; i <= 60; ++i) {
            builder.vertex(matrix4f, (float) (x + radius * Math.sin(0.10471975511965977 * (double) i)), (float) (y + radius * Math.cos(0.10471975511965977 * (double) i)), 1).color(ColorUtil.r(c), ColorUtil.g(c), ColorUtil.b(c), ColorUtil.a(c)).endVertex();

        }
        Tessellator.getInstance().end();
        RenderSystem.lineWidth(1.5f);
        GL20.glEnable(2848);
        builder.begin(2, DefaultVertexFormats.POSITION_COLOR);
        for (i = 0; i <= 60; ++i) {
            builder.vertex(matrix4f, (float) (x + radius * Math.sin(0.10471975511965977 * (double) i)), (float) (y + radius * Math.cos(0.10471975511965977 * (double) i)), 1).color(ColorUtil.r(c), ColorUtil.g(c), ColorUtil.b(c), ColorUtil.a(c)).endVertex();
        }
        Tessellator.getInstance().end();
        GL20.glDisable(2848);
        RenderUtil.endRenderRect();
    }

    public static void drawCircle(double x, double y, double radius, int color) {
        int i;
        RenderUtil.setupRenderRect();
        RenderUtil.glColor(color);
        GL20.glBegin(6);
        for (i = 0; i <= 60; ++i) {
            GL30.glVertex2d((x + radius * Math.sin(0.10471975511965977 * (double) i)), (double) (y + radius * Math.cos(0.10471975511965977 * (double) i)));
        }
        GL20.glEnd();
        GL20.glLineWidth(1.5f);
        GL20.glEnable(2848);
        GL20.glBegin(2);
        for (i = 0; i <= 60; ++i) {
            GL20.glVertex2d((x + radius * Math.sin(0.10471975511965977 * (double) i)), (double) (y + radius * Math.cos(0.10471975511965977 * (double) i)));
        }
        GL20.glEnd();
        GL20.glDisable(2848);
        RenderUtil.endRenderRect();
    }

    public static void ColorPickerTriangle(float xPos, float yPos, float Size, int color) {
        RenderUtil.setupRenderRect();
        GL20.glBegin(4);
        RenderUtil.glColor(Color.WHITE.getRGB());
        GL20.glVertex2d(xPos - 1, yPos - 0.5f);
        GL20.glVertex2d(xPos + 1 + Size, yPos - 0.5);
        GL20.glVertex2d(xPos + Size / 2, yPos + Size + 1.5);
        GL20.glEnd();
        RenderUtil.endRenderRect();
        RenderUtil.setupRenderRect();
        GL20.glBegin(4);
        RenderUtil.glColor(color);
        GL20.glVertex2d(xPos, yPos);
        RenderUtil.glColor(Color.BLACK.getRGB());
        GL20.glVertex2d(xPos + Size, yPos);
        RenderUtil.glColor(Color.WHITE.getRGB());
        GL20.glVertex2d(xPos + Size / 2, yPos + Size);
        GL20.glEnd();
        RenderUtil.endRenderRect();
    }

    public static void drawCircleOutline(double x, double y, double radius, float thikness, int color) {
        RenderUtil.setupRenderRect();
        GL20.glEnable(2848);
        GL20.glLineWidth(thikness);
        RenderUtil.glColor(color);
        GL20.glBegin(2);
        for (int i = 0; i <= 60; ++i) {
            GL20.glVertex2d((x + radius * Math.sin(0.10471975511965977 * (double) i)), (double) (y + radius * Math.cos(0.10471975511965977 * (double) i)));
        }
        GL20.glEnd();
        GL20.glDisable(2848);
        RenderUtil.endRenderRect();
    }

    public static void drawCircleOutline(MatrixStack ms, double x, double y, double radius, float thikness, int color) {
        RenderUtil.setupRenderRect();
        GL20.glEnable(2848);
        ms.pushPose();
        RenderSystem.lineWidth(thikness);
        RenderUtil.glColor(color);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuilder();
        builder.begin(2, DefaultVertexFormats.POSITION_COLOR);
        for (int i = 0; i <= 60; ++i) {
            builder.vertex((x + radius * Math.sin(0.10471975511965977 * (double) i)), (y + radius * Math.cos(0.10471975511965977 * (double) i)), 1).color(ColorUtil.r(color), ColorUtil.g(color), ColorUtil.b(color), ColorUtil.a(color)).endVertex();
        }
        tessellator.end();
        ms.popPose();
        GL20.glEnd();
        GL20.glDisable(2848);
        RenderUtil.endRenderRect();
    }

    public static void drawRainbowCircle(double x, double y, double radius, double blurRadius) {
        int i;
        RenderUtil.setupRenderRect();
        GL20.glEnable(3008);
        GL20.glAlphaFunc(16, 1.0E-4f);
        GL20.glShadeModel(7425);
        RenderUtil.glColor(-1);
        GL20.glBegin(6);
        GL20.glVertex2d(x, y);
        for (i = 0; i <= 120; ++i) {
            RenderUtil.glColor(Color.getHSBColor((float) i / 120.0f, 1.0f, 1.0f).getRGB());
            GL20.glVertex2d((x + radius * Math.sin(0.05235987755982988 * (double) i)), (y + radius * Math.cos(0.05235987755982988 * (double) i)));
        }
        GL20.glEnd();
        GL20.glBegin(5);
        for (i = 0; i <= 121; ++i) {
            if (i % 2 == 1) {
                RenderUtil.glColor(ColorUtil.swapAlpha(Color.getHSBColor((float) i / 120.0f, 1.0f, 1.0f).getRGB(), 0));
                GL20.glVertex2d((x + (radius + blurRadius) * Math.sin(0.05235987755982988 * (double) i)), (y + (radius + blurRadius) * Math.cos(0.05235987755982988 * (double) i)));
                continue;
            }
            RenderUtil.glColor(Color.getHSBColor((float) i / 120.0f, 1.0f, 1.0f).getRGB());
            GL20.glVertex2d((x + radius * Math.sin(0.05235987755982988 * (double) i)), (y + radius * Math.cos(0.05235987755982988 * (double) i)));
        }
        GL20.glEnd();
        GL20.glShadeModel(7424);
        GL20.glDisable(3008);
        RenderUtil.endRenderRect();
    }

    public static void drawGlow(float x, float y, float width, float height, int glowRadius, int color) {
        resetColor();
        _enableBlend();
        _blendFunc(770, 771);
        GL20.glEnable(3008);
        GL20.glAlphaFunc(516, 1.0E-4f);
        _bindTexture(getGlowTexture((int) width, (int) height, glowRadius));
        RenderUtil.glColor(color);
        ShaderUtil.drawQuads(x, y, width, height);
        _bindTexture(0);
        GL20.glDisable(3008);
        GlStateManager._disableBlend();
        RenderUtil.glColor(-1);
        RenderUtil.drawFinishShader();
        resetColor();

    }

    public static int getGlowTexture(int width, int height, int blurRadius) {
        int identifier = (width * 401 + height) * 407 + blurRadius;
        int texId = glowCache.getOrDefault(identifier, -1);
        if (texId == -1) {
            BufferedImage original = new BufferedImage(width + blurRadius * 2, height + blurRadius * 2, 3);
            Graphics g = original.getGraphics();
            g.setColor(Color.WHITE);
            g.fillRect(blurRadius, blurRadius, width, height);
            g.dispose();
            GlowFilter glow = new GlowFilter(blurRadius);
            BufferedImage blurred = glow.filter(original, null);
            try {
                texId = RenderUtil.loadTexture(blurred);
                glowCache.put(identifier, texId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return texId;
    }

    public static void renderByte(byte[] bytes, float x, float y, float width, float height) {
        try {
            glPushMatrix();
            RenderUtil.drawSetupShader();
            int texId = -1;
            int identifier = (new String(bytes).hashCode());
            if (shadowCache2.containsKey(identifier)) {
                texId = shadowCache2.get(identifier);
                _bindTexture(texId);
            } else {
                BufferedImage img = ImageIO.read(new ByteArrayInputStream(bytes));
                texId = uploadTextureImageAllocate(TextureUtil.generateTextureId(), img, true, false);
                shadowCache2.put(identifier, texId);
            }
            ShaderUtil.drawQuads(x, y, width, height);
            RenderUtil.drawFinishShader();
            _bindTexture(0);
            glPopMatrix();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void downloadImage(String url, float x, float y, float width, float height) {
        try {
            glPushMatrix();
            glEnable(GL11.GL_TEXTURE_2D);
            glDisable(GL11.GL_CULL_FACE);
            glEnable(GL11.GL_ALPHA_TEST);
            _enableBlend();
            int texId = -1;

            int identifier = (url.hashCode());
            if (shadowCache2.containsKey(identifier)) {
                texId = shadowCache2.get(identifier);
                _bindTexture(texId);
            } else {
                byte[] bytes = Utils.readAllBytes(WebUtilsNative.visitSiteInput(url));
                BufferedImage img = ImageIO.read(new ByteArrayInputStream(bytes));
                texId = uploadTextureImageAllocate(TextureUtil.generateTextureId(), img, true, false);
                shadowCache2.put(identifier, texId);
            }
            GL11.glColor4f(1f, 1f, 1f, 1f);
            ShaderUtil.drawQuads(x, y, width, height);
            GlStateManager._enableTexture();
            GlStateManager._disableBlend();
            glEnable(GL_CULL_FACE);
            _bindTexture(0);
            glPopMatrix();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void drawRoundedTexture(ResourceLocation rl, float x, float y, float width, float height, float radius) {
        y += height;
        _enableBlend();
        _blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        resetColor();

        ROUNDED_TEXTURE.load();
        ROUNDED_TEXTURE.setUniformf("size", width * 2, height * 2);
        ROUNDED_TEXTURE.setUniformf("round", radius * 2);
        _bindTexture(Utils.getTextureId(rl));
        ShaderUtil.drawQuads(x, y - height, width, height);
        _bindTexture(0);
        ROUNDED_TEXTURE.unload();

        _disableBlend();
    }

    public static void drawRect(final float startX, final float startY, final float width, final float height, final int color) {
        float endX = startX + width;
        float endY = startY + height;
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        GL14.glBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO);
        glColor(color);

        glBegin(GL_QUADS);

        glVertex2f(endX, startY);
        glVertex2f(startX, startY);
        glVertex2f(startX, endY);
        glVertex2f(endX, endY);

        glEnd();

        glDisable(GL_BLEND);
        glEnable(GL_TEXTURE_2D);
    }

    public static void drawTex(int Texture, final float startX, final float startY, final float width, final float height, final int color) {
        // Включаем текстурирование
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        // Привязываем вашу текстуру к текстурному блоку 0
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, Texture);

        // Рисуем квадрат с текстурой
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0, 0);
        GL11.glVertex2f(startX, startY);
        GL11.glTexCoord2f(1, 0);
        GL11.glVertex2f(startX + width, startY);
        GL11.glTexCoord2f(1, 1);
        GL11.glVertex2f(startX + width, startY + height);
        GL11.glTexCoord2f(0, 1);
        GL11.glVertex2f(startX, startY + height);
        GL11.glEnd();

        // Отключаем текстурирование
        GL11.glDisable(GL11.GL_TEXTURE_2D);
    }

    private static void glColor(int color) {
        float alpha = (float) ((color >> 24) & 0xFF) / 255.0f;
        float red = (float) ((color >> 16) & 0xFF) / 255.0f;
        float green = (float) ((color >> 8) & 0xFF) / 255.0f;
        float blue = (float) (color & 0xFF) / 255.0f;

        glColor4f(red, green, blue, alpha);
    }

}
