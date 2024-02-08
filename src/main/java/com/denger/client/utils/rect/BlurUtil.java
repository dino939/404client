package com.denger.client.utils.rect;


import com.denger.client.utils.Emulator.WindowEmulator;
import com.denger.client.utils.MathUtils;
import com.denger.client.utils.Utils;
import com.google.common.collect.Queues;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.IRenderCall;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.util.concurrent.ConcurrentLinkedQueue;

import static com.denger.client.Main.mc;

public class BlurUtil {
    private static MainWindow window = mc.getWindow();

    private static final ShaderUtil blur = new ShaderUtil("blur");
    private static final ConcurrentLinkedQueue<IRenderCall> renderQueue = Queues.newConcurrentLinkedQueue();
    private static final Framebuffer inFrameBuffer = new Framebuffer((window.getWidth()), (window.getHeight()), true, Minecraft.ON_OSX);
    private static final Framebuffer outFrameBuffer = new Framebuffer((window.getWidth() * 2), (window.getHeight() * 2), true, Minecraft.ON_OSX);


    public static void drawBlur(int radius, Runnable runnable) {
        MainWindow mainWindow = mc.getWindow();
        float width = (float) MathUtils.calc(mainWindow.getGuiScaledWidth());
        float height = (float) MathUtils.calc(mainWindow.getGuiScaledHeight());
        StencilUtil.renderInStencil(runnable, () -> {
            registerRenderCall(() -> RectUtil.drawRect(0, 0, width, height, -1));
            draw((int) MathUtils.clamp(radius, 1, 999));
        }, 1);
    }

    public static void drawBlur(int radius, int color, Runnable runnable) {
        MainWindow mainWindow = mc.getWindow();
        float width = (float) MathUtils.calc(mainWindow.getGuiScaledWidth());
        float height = (float) MathUtils.calc(mainWindow.getGuiScaledHeight());
        StencilUtil.renderInStencil(runnable, () -> {
            RectUtil.drawRect(0, 0, width, height, color);
            registerRenderCall(() -> RectUtil.drawRect(0, 0, width, height, color));
            draw((int) MathUtils.clamp(radius, 1, 999));
        }, 1);


    }

    public static void registerRenderCall(IRenderCall rc) {
        renderQueue.add(rc);
    }

    public static void draw(int radius) {
        inFrameBuffer.bindWrite(true);
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
        WindowEmulator.texture = inFrameBuffer.getColorTextureId();
        GlStateManager._disableBlend();
    }

    private static Framebuffer setupBuffer(Framebuffer frameBuffer) {
        if (frameBuffer.width != (window.getWidth() ) || frameBuffer.height !=  (window.getHeight()))
            frameBuffer.resize( (window.getWidth()) , (window.getHeight()), Minecraft.ON_OSX);
        else
            frameBuffer.clear(Minecraft.ON_OSX);

        return frameBuffer;
    }
}
