package com.denger.client.utils;

import com.denger.client.utils.rect.RectUtil;
import com.denger.client.utils.rect.StencilUtil;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public class GlichUtils {
    AnimationUtil anim;
    TimerUtil timer;
    float posY;

    boolean doGlich = false;
    long time;

    public GlichUtils() {
        anim = new AnimationUtil(0, 0, 200);
        timer = new TimerUtil();
        this.time = 5000;
    }

    public GlichUtils(long time) {
        anim = new AnimationUtil(0, 0, 200);
        timer = new TimerUtil();
        this.time = time;
    }


    public void render(ArrayList<RenderInterface> renderInterfaces, float posYY, float height, float maxHight) {
        if (timer.hasReached(time)) {
            doGlich = true;
            timer.reset();
            posY = Utils.randomInt((int) posYY, (int) (posYY + height - maxHight));
            anim.goTo(Utils.randomInt(-10, 10));
        }
        if (anim.getAnim() == anim.getTo()) {
            anim.goTo(0);
        }
        if (anim.getAnim() == 0 && anim.getTo() == 0) {
            doGlich = false;
        }
        if (!doGlich) {
            for (RenderInterface renderInterface : renderInterfaces) {
                renderInterface.render();
            }
        } else {
            for (RenderInterface renderInterface : renderInterfaces) {
                renderInterface.render();
            }
            StencilUtil.initStencilToWrite();
            RectUtil.drawRect(0, posY, 1000, maxHight, -1);
            StencilUtil.readStencilBuffer(1);
            GL11.glPushMatrix();
            for (RenderInterface renderInterface : renderInterfaces) {
                GL11.glTranslated(anim.getAnim(), 0, 0);
                renderInterface.render();
            }
            GL11.glPopMatrix();
            StencilUtil.uninitStencilBuffer();
        }

    }

    public void render(ArrayList<RenderInterface> renderInterfaces, float posYY, float height, float maxHight, boolean render) {
        if (timer.hasReached(time)) {
            doGlich = true;
            timer.reset();
            posY = Utils.randomInt((int) posYY, (int) (posYY + height - maxHight));
            anim.goTo(Utils.randomInt(-10, 10));
        }
        if (anim.getAnim() == anim.getTo()) {
            anim.goTo(0);
        }
        if (anim.getAnim() == 0 && anim.getTo() == 0) {
            doGlich = false;
        }
        if (!doGlich) {
            for (RenderInterface renderInterface : renderInterfaces) {
                if (render) {
                    renderInterface.render();
                }
            }
        } else {
            for (RenderInterface renderInterface : renderInterfaces) {
                if (render) {
                    renderInterface.render();
                }
            }
            StencilUtil.initStencilToWrite();
            RectUtil.drawRect(0, posY, 1000, maxHight, -1);
            StencilUtil.readStencilBuffer(1);
            GL11.glPushMatrix();
            for (RenderInterface renderInterface : renderInterfaces) {
                GL11.glTranslated(anim.getAnim(), 0, 0);
                renderInterface.render();
            }
            GL11.glPopMatrix();
            StencilUtil.uninitStencilBuffer();
        }

    }

}


