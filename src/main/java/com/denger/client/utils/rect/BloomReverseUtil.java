package com.denger.client.utils.rect;


import com.denger.client.utils.Utils;
import com.google.common.collect.Queues;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.IRenderCall;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.GL30;

import java.util.concurrent.ConcurrentLinkedQueue;

import static com.denger.client.MainNative.mc;

public class BloomReverseUtil {
    private static final MainWindow window = mc.getWindow();
    private static final ShaderUtil bloom = new ShaderUtil("bloom");
    private static final ConcurrentLinkedQueue<IRenderCall> renderQueue = Queues.newConcurrentLinkedQueue();
    private static final Framebuffer inFrameBuffer = new Framebuffer(window.getWidth(), window.getHeight(), true, Minecraft.ON_OSX);
    private static final Framebuffer outFrameBuffer = new Framebuffer(window.getWidth(), window.getHeight(), true, Minecraft.ON_OSX);
    public static Framebuffer framebufferHashMap;
    public static void registerRenderCall(IRenderCall rc) {
        renderQueue.add(rc);
    }


    public static void draw(int radius) {
        if(renderQueue.isEmpty())
            return;

        setupBuffer(inFrameBuffer);
        setupBuffer(outFrameBuffer);

        inFrameBuffer.bindWrite(true);

        while(!renderQueue.isEmpty()) {
            renderQueue.poll().execute();
        }

        outFrameBuffer.bindWrite(true);

        bloom.load();
        bloom.setUniformf("radius", radius);
        bloom.setUniformi("sampler1", 0);
        bloom.setUniformi("sampler2", 20);
        bloom.setUniformfb("kernel", Utils.getKernel(radius));
        bloom.setUniformf("texelSize", 1.0F / (float)window.getWidth(), 1.0F / (float)window.getHeight());
        bloom.setUniformf("direction", 2.0F, 0.0F);

        GlStateManager._enableBlend();
        GlStateManager._blendFunc(GL30.GL_ONE, GL30.GL_SRC_ALPHA);
        GL30.glAlphaFunc(GL30.GL_GREATER, 0.0001f);

        inFrameBuffer.bindRead();
        ShaderUtil.drawQuadsRevers();

        mc.getMainRenderTarget().bindWrite(false);
        GlStateManager._blendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);

        bloom.setUniformf("direction", 0.0F, 2.0F);

        outFrameBuffer.bindRead();
        GL30.glActiveTexture(GL30.GL_TEXTURE20);
        inFrameBuffer.bindRead();
        GL30.glActiveTexture(GL30.GL_TEXTURE0);
        ShaderUtil.drawQuadsRevers();

        bloom.unload();
        inFrameBuffer.unbindRead();
        GlStateManager._disableBlend();
    }

    private static Framebuffer setupBuffer(Framebuffer frameBuffer) {
        if (frameBuffer.width != (window.getWidth() ) || frameBuffer.height !=  (window.getHeight()))
            frameBuffer.resize( (window.getWidth()) , (window.getHeight()), Minecraft.ON_OSX);
        else
            frameBuffer.clear(Minecraft.ON_OSX);

        return frameBuffer;
    }


    public static void enableBloom() {
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
    }

    public static void disableBloom() {
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
    }

}
