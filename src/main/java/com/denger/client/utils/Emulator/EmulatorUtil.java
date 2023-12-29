package com.denger.client.utils.Emulator;

import com.denger.client.utils.MathUtils;
import com.denger.client.utils.Utils;
import com.denger.client.utils.WebUtilsNative;
import com.denger.client.utils.rect.RectUtil;
import com.denger.client.utils.rect.ShaderUtil;
import com.denger.client.utils.rect.StencilUtil;
import com.google.common.collect.Queues;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.IRenderCall;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import static com.denger.client.MainNative.mc;
import static com.denger.client.utils.TextureUtil.uploadTextureImageAllocate;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;

public class EmulatorUtil {
    private static MainWindow window = mc.getWindow();

    private static final ShaderUtil blur = new ShaderUtil("blur");
    private static final ConcurrentLinkedQueue<IRenderCall> renderQueue = Queues.newConcurrentLinkedQueue();
    private static final Framebuffer inFrameBuffer = new Framebuffer((int) (window.getWidth() / 2d), (int) (window.getHeight() / 2d), true, Minecraft.ON_OSX);
    private static final Framebuffer outFrameBuffer = new Framebuffer((int) (window.getWidth() / 2d), (int) (window.getHeight() / 2d), true, Minecraft.ON_OSX);

    public static HashMap<Integer, Integer> shadowCache2 = new HashMap();
    public static void drawBlur(int radius,Runnable runnable){
        MainWindow mainWindow = mc.getWindow();
        float width = (float) MathUtils.calc(mainWindow.getGuiScaledWidth());
        float height = (float) MathUtils.calc(mainWindow.getGuiScaledHeight());
        StencilUtil.renderInStencil(runnable,()->{
            registerRenderCall(()-> RectUtil.drawRect(0, 0, width, height, -1));
            draw((int) MathUtils.clamp(radius,1,999));
        },1);
    }
    public static void drawBlur(int radius,int color,Runnable runnable){
        MainWindow mainWindow = mc.getWindow();
        float width = (float) MathUtils.calc(mainWindow.getGuiScaledWidth());
        float height = (float) MathUtils.calc(mainWindow.getGuiScaledHeight());
        StencilUtil.renderInStencil(runnable,()->{
            RectUtil.drawRect(0, 0, width, height, color);
            registerRenderCall(()->RectUtil.drawRect(0, 0, width, height, color));
            draw((int) MathUtils.clamp(radius,1,999));
        },1);



    }
    public static void registerRenderCall(IRenderCall rc) {
        renderQueue.add(rc);
    }

    public static void draw(int radius) {
        if (renderQueue.isEmpty())
            return;

        setupBuffer(inFrameBuffer);
        setupBuffer(outFrameBuffer);

        inFrameBuffer.bindWrite(true);

        while (!renderQueue.isEmpty()) {
            renderQueue.poll().execute();
        }

        outFrameBuffer.bindWrite(true);

        blur.load();
        blur.setUniformf("radius", radius);
        blur.setUniformi("sampler1", 0);
        blur.setUniformi("sampler2", 20);
        blur.setUniformfb("kernel", Utils.getKernel(radius));
        blur.setUniformf("texelSize", 1.0F / (float) window.getWidth(), 1.0F / (float) window.getHeight());
        blur.setUniformf("direction", 2.0F, 0.0F);

        GlStateManager._disableBlend();
        GlStateManager._blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        mc.getMainRenderTarget().bindRead();
        ShaderUtil.drawQuads();

        mc.getMainRenderTarget().bindWrite(true);

        blur.setUniformf("direction", 0.0F, 2.0F);

        outFrameBuffer.bindRead();
        GL30.glActiveTexture(GL30.GL_TEXTURE20);
        inFrameBuffer.bindRead();
        GL30.glActiveTexture(GL30.GL_TEXTURE0);
        ShaderUtil.drawQuads();

        blur.unload();
        inFrameBuffer.unbindRead();
        GlStateManager._disableBlend();
    }

    private static Framebuffer setupBuffer(Framebuffer frameBuffer) {
        if (frameBuffer.width != (int) (window.getWidth() / 2d) || frameBuffer.height != (int) (window.getHeight() / 2d))
            frameBuffer.resize((int) (window.getWidth() / 2d), (int) (window.getHeight() / 2d), Minecraft.ON_OSX);
        else
            frameBuffer.clear(Minecraft.ON_OSX);

        return frameBuffer;
    }
    public static int getFrameBuffer(){
        return mc.getMainRenderTarget().getColorTextureId();
    }
    public static void drawTex(int textureID, float startX, float startY, float width, float height, int color) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL20.glUseProgram(textureID);

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);

        drawQuads(startX, startY, width, height);

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        GL20.glUseProgram(0);

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }
    public static void drawImageId(int textureID, float x, float y, float width, float height, int color) {
        glPushMatrix();
        glEnable(GL11.GL_TEXTURE_2D);
        glDisable(GL11.GL_CULL_FACE);
        glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBindTexture(3553, textureID);
        GL11.glColor4f(1f, 1f, 1f, 1f);
        ShaderUtil.drawQuads(x, y, width, height);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        glEnable(GL_CULL_FACE);
        GL11.glBindTexture(3553, 0);
        glPopMatrix();
    }
    public static void downloadImage(String url, float x, float y, float width, float height) {
        try {
            glPushMatrix();
            glEnable(GL11.GL_TEXTURE_2D);
            glDisable(GL11.GL_CULL_FACE);
            glEnable(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_BLEND);
            int texId = -1;

            int identifier = (url.hashCode());
            if (shadowCache2.containsKey(identifier)) {
                texId = shadowCache2.get(identifier);
                GL11.glBindTexture(3553, texId);
            } else {
                byte[] bytes = Utils.readAllBytes(WebUtilsNative.visitSiteInput(url));
                BufferedImage img = ImageIO.read(new ByteArrayInputStream(bytes));
                texId = uploadTextureImageAllocate(TextureUtil.generateTextureId(), img, true, false);
                shadowCache2.put(identifier, texId);
            }
            GL11.glColor4f(1f, 1f, 1f, 1f);
            ShaderUtil.drawQuads(x, y, width, height);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_BLEND);
            glEnable(GL_CULL_FACE);
            GL11.glBindTexture(3553, 0);
            glPopMatrix();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void drawQuads(float startX, float startY, float width, float height) {
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(startX, startY);
        GL11.glVertex2f(startX + width, startY);
        GL11.glVertex2f(startX + width, startY + height);
        GL11.glVertex2f(startX, startY + height);
        GL11.glEnd();
    }
}
