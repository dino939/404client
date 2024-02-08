package com.denger.client.utils.rect.notStatic;


import com.denger.client.utils.rect.ShaderUtil;
import com.google.common.collect.Queues;
import com.mojang.blaze3d.systems.IRenderCall;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.Framebuffer;

import java.util.concurrent.ConcurrentLinkedQueue;

import static com.denger.client.Main.mc;
import static com.denger.client.utils.rect.RenderUtil.resetColor;
import static com.mojang.blaze3d.platform.GlStateManager.*;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;

public class Bloom3D {
    private final MainWindow window = mc.getWindow();
    private final ShaderUtil bloom = new ShaderUtil("bloom");
    private final ConcurrentLinkedQueue<IRenderCall> renderQueue = Queues.newConcurrentLinkedQueue();
    private final Framebuffer inFrameBuffer = new Framebuffer(window.getWidth(), window.getHeight(), true, Minecraft.ON_OSX);
    private final Framebuffer outFrameBuffer = new Framebuffer(window.getWidth(), window.getHeight(), true, Minecraft.ON_OSX);

    public void registerRenderCall(IRenderCall rc) {
        inFrameBuffer.clear(true);
        inFrameBuffer.bindWrite(true);
        rc.execute();
        inFrameBuffer.unbindWrite();
        mc.getMainRenderTarget().bindWrite(true);
    }

    public Bloom3D() {

    }

    public void draw(int radius) {
        _enableBlend();
        _blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        resetColor();
        _bindTexture(mc.getMainRenderTarget().getColorTextureId());
        ShaderUtil.drawQuads();
        _bindTexture(0);
        _disableBlend();


    }

    private Framebuffer setupBuffer(Framebuffer frameBuffer) {
        if (frameBuffer.width != window.getWidth() || frameBuffer.height != window.getHeight())
            frameBuffer.resize(window.getWidth(), window.getHeight(), Minecraft.ON_OSX);
        else frameBuffer.clear(Minecraft.ON_OSX);
        frameBuffer.setClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        return frameBuffer;
    }


}
