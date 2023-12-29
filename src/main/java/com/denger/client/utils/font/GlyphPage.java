package com.denger.client.utils.font;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;

public class GlyphPage {

    private int imgSize;
    private float maxFontHeight = -1;
    private Font font;
    private boolean antiAliasing;
    private boolean fractionalMetrics;
    private HashMap<Character, Glyph> glyphCharacterMap = new HashMap<>();

    private BufferedImage bufferedImage;
    private DynamicTexture loadedTexture;

    public GlyphPage(Font font, boolean antiAliasing, boolean fractionalMetrics) {
        this.font = font;
        this.antiAliasing = antiAliasing;
        this.fractionalMetrics = fractionalMetrics;
    }

    public void generateGlyphPage(char[] chars) {
        // Calculate glyphPageSize
        double maxWidth = -1;
        double maxHeight = -1;

        AffineTransform affineTransform = new AffineTransform();
        FontRenderContext fontRenderContext = new FontRenderContext(affineTransform, antiAliasing, fractionalMetrics);

        for (char ch : chars) {
            Rectangle2D bounds = font.getStringBounds(Character.toString(ch), fontRenderContext);

            if (maxWidth < bounds.getWidth())
                maxWidth = bounds.getWidth();
            if (maxHeight < bounds.getHeight())
                maxHeight = bounds.getHeight();
        }

        // Leave some additional space

        maxWidth += 2;
        maxHeight += 2;

        imgSize = (int) Math.ceil(Math.max(Math.ceil(Math.sqrt(maxWidth * maxWidth * chars.length) / maxWidth),
                Math.ceil(Math.sqrt(maxHeight * maxHeight * chars.length) / maxHeight)) * Math.max(maxWidth, maxHeight))
                + 1;

        bufferedImage = new BufferedImage(imgSize, imgSize, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = bufferedImage.createGraphics();

        g.setFont(font);
        // Set Color to Transparent
        g.setColor(new Color(255, 255, 255, 0));
        // Set the image background to transparent
        g.fillRect(0, 0, imgSize, imgSize);

        g.setColor(Color.white);

        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                fractionalMetrics ? RenderingHints.VALUE_FRACTIONALMETRICS_ON
                        : RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                antiAliasing ? RenderingHints.VALUE_ANTIALIAS_OFF : RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                antiAliasing ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

        FontMetrics fontMetrics = g.getFontMetrics();

        float currentCharHeight = 0;
        float posX = 0;
        float posY = 0;

        for (char ch : chars) {
            Glyph glyph = new Glyph();

            Rectangle2D bounds = fontMetrics.getStringBounds(Character.toString(ch), g);

            glyph.width = bounds.getBounds().width + 8; // Leave some additional space
            glyph.height = bounds.getBounds().height + 3;

            if (posX + glyph.width >= imgSize) {
                posX = 0;
                posY += currentCharHeight;
                currentCharHeight = 0;
            }

            glyph.x = posX;
            glyph.y = posY - 2;

            if (glyph.height > maxFontHeight)
                maxFontHeight = glyph.height;

            if (glyph.height > currentCharHeight)
                currentCharHeight = glyph.height;

            g.drawString(Character.toString(ch), posX + 2, posY + fontMetrics.getAscent());

            posX += glyph.width;

            glyphCharacterMap.put(ch, glyph);

        }
    }

    public void setupTexture() {
        try {
            // Converting to ByteBuffer
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", baos);
            byte[] bytes = baos.toByteArray();

            ByteBuffer data = BufferUtils.createByteBuffer(bytes.length).put(bytes);
            data.flip();
            loadedTexture = new DynamicTexture(NativeImage.read(data));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void bindTexture() {
        RenderSystem.bindTexture(loadedTexture.getId());
    }

    public void unbindTexture() {
        RenderSystem.bindTexture(0);
    }

    public float drawChar(MatrixStack stack, char ch, float x, float y, float red, float blue, float green, float alpha) {
        Glyph glyph = glyphCharacterMap.get(ch);

        if (glyph == null)
            return 0;

        float pageX = glyph.x / (float) imgSize;
        float pageY = glyph.y / (float) imgSize;

        float pageWidth = glyph.width / (float) imgSize;
        float pageHeight = glyph.height / (float) imgSize;

        float width = glyph.width;
        float height = glyph.height;

        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuilder();

        bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR_TEX);

        bufferBuilder.vertex(stack.last().pose(), x, y + height, 0).color(red, green, blue, alpha)
                .uv(pageX, pageY + pageHeight).endVertex();
        bufferBuilder.vertex(stack.last().pose(), x + width, y + height, 0).color(red, green, blue, alpha)
                .uv(pageX + pageWidth, pageY + pageHeight).endVertex();
        bufferBuilder.vertex(stack.last().pose(), x + width, y, 0).color(red, green, blue, alpha)
                .uv(pageX + pageWidth, pageY).endVertex();
        bufferBuilder.vertex(stack.last().pose(), x, y, 0).color(red, green, blue, alpha).uv(pageX, pageY)
                .endVertex();
        bufferBuilder.end();
        WorldVertexBufferUploader.end(bufferBuilder);

        return width - 8;
    }

    public float getWidth(char ch) {
        Glyph glyph = glyphCharacterMap.get(ch);

        if (glyph == null)
            return 0;

        return glyph.width;
    }

    public float getMaxFontHeight() {
        return maxFontHeight;
    }

    public boolean isAntiAliasingEnabled() {
        return antiAliasing;
    }

    public boolean isFractionalMetricsEnabled() {
        return fractionalMetrics;
    }

    static class Glyph {
        private float x;
        private float y;
        private float width;
        private float height;

        Glyph(float x, float y, float width, float height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        Glyph() {
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }

        public float getWidth() {
            return width;
        }

        public float getHeight() {
            return height;
        }
    }
}
