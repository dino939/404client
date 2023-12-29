package com.denger.client.utils.rect;


import com.denger.client.utils.ReflectFileld;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.EXTPackedDepthStencil;

import static com.denger.client.MainNative.mc;
import static org.lwjgl.opengl.GL11.*;

public class StencilUtil {
    public static void checkSetupFBO(Framebuffer framebuffer) {
        try {
            ReflectFileld local = new ReflectFileld(framebuffer, Framebuffer.class, 7);
            Object depthBuffer = framebuffer.getDepthTextureId();
            if (framebuffer != null) {
                if ((int) depthBuffer > -1) {
                    setupFBO(framebuffer);
                    local.setValue(-1);
                }
            }
        } catch (Exception ignored) {
        }

    }

    public static void setupFBO(Framebuffer framebuffer) {
        try {
            EXTFramebufferObject.glDeleteRenderbuffersEXT(framebuffer.getDepthTextureId());
            final int stencilDepthBufferID = EXTFramebufferObject.glGenRenderbuffersEXT();
            EXTFramebufferObject.glBindRenderbufferEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, stencilDepthBufferID);
            EXTFramebufferObject.glRenderbufferStorageEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, EXTPackedDepthStencil.GL_DEPTH_STENCIL_EXT, mc.getWindow().getWidth(), mc.getWindow().getHeight());
            EXTFramebufferObject.glFramebufferRenderbufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_STENCIL_ATTACHMENT_EXT, EXTFramebufferObject.GL_RENDERBUFFER_EXT, stencilDepthBufferID);
            EXTFramebufferObject.glFramebufferRenderbufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_DEPTH_ATTACHMENT_EXT, EXTFramebufferObject.GL_RENDERBUFFER_EXT, stencilDepthBufferID);

        } catch (Exception ignored) {

        }
    }

    public static void renderInStencil(Runnable on, Runnable render, int mode) {
        initStencilToWrite();
        on.run();
        readStencilBuffer(mode);
        render.run();
        uninitStencilBuffer();
    }

    public static void initStencilToWrite() {
        //init
        mc.getMainRenderTarget().bindWrite(false);
        checkSetupFBO(mc.getMainRenderTarget());
        glClear(GL_STENCIL_BUFFER_BIT);
        glEnable(GL_STENCIL_TEST);
        glStencilFunc(GL_ALWAYS, 1, 1);
        glStencilOp(GL_REPLACE, GL_REPLACE, GL_REPLACE);
        glColorMask(false, false, false, false);
    }

    public static void readStencilBuffer(int ref) {
        glColorMask(true, true, true, true);
        glStencilFunc(GL_EQUAL, ref, 1);
        glStencilOp(GL_KEEP, GL_KEEP, GL_KEEP);
    }

    public static void uninitStencilBuffer() {
        glDisable(GL_STENCIL_TEST);
    }
}