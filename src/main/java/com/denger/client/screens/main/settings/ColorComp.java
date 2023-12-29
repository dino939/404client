package com.denger.client.screens.main.settings;

import com.denger.client.another.settings.Setting;
import com.denger.client.another.settings.sett.ColorSetting;
import com.denger.client.screens.main.MainScreen;
import com.denger.client.screens.main.comp.GuiColors;
import com.denger.client.screens.main.comp.SettComp;
import com.denger.client.utils.ColorUtil;
import com.denger.client.utils.MathUtils;
import com.denger.client.utils.rect.RectUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static com.denger.client.MainNative.fontManager;
import static org.lwjgl.opengl.GL11.glVertex2d;
import static org.lwjgl.opengl.GL11C.*;


public class ColorComp extends SettComp {
    ColorSetting colorSetting;
    float[] hsb;
    float hue;
    float saturation;
    float brightness;
    boolean draggingHue = false;
    boolean draggSaturation = false;
    boolean open = false;

    public ColorComp(Setting s) {
        super(s);
        colorSetting = (ColorSetting) s;
        hsb = Color.RGBtoHSB(colorSetting.getColor().getRed(), colorSetting.getColor().getGreen(), colorSetting.getColor().getBlue(), null);
        hue = hsb[0];
        saturation = hsb[1];
        brightness = hsb[2];
    }

    @Override
    public void Render(MatrixStack ms, float Xpos, float Ypos) {
        super.Render(ms, Xpos, Ypos -= open ? 4 : -2);
        //RectUtil.drawRect(getXpos(), getYpos(), 150, getHeight(), -1);
        if (open) {
            float y = getYpos() + 10;

            RectUtil.drawGradientRound(getXpos() + 4, y + 2, 142, 60 - 10, 2, GuiColors.transform(Color.BLACK.getRGB()), GuiColors.transform(Color.BLACK.getRGB()), GuiColors.transform(ColorUtil.toRGBA(255, 255, 255, 255)), GuiColors.transform(new Color(Color.HSBtoRGB(hue, 1, 1)).getRGB()));
            for (int i = 0; i <= 142; i++) {
                RectUtil.drawRect(getXpos() + 4 + i, y + 55, 1f, 3, GuiColors.transform(Color.HSBtoRGB((float) i / 142, 1, 1)));


            }
            if (draggingHue) {
                float mouse = MathUtils.clamp(getMouseX(), getXpos(), getXpos() + 148);
                hue = (mouse - (getXpos() + 4)) / 142;
                colorSetting.setColor(new Color(Color.HSBtoRGB(hue, 1 - saturation, brightness)));
            }

            hue = MathUtils.clamp(hue, 0, 1);

            RectUtil.drawRect(Xpos + 4 + hue * 142, y + 60 - 5, 1, 3, GuiColors.transform(ColorUtil.toRGBA(255, 255, 255, 255)));

            if (draggSaturation) {
                float saturation = (getMouseY() - (y + 2)) / (60 - 10f);
                saturation = MathUtils.clamp(saturation, 0, 1);
                float brightness = (getMouseX() - (getXpos() + 4)) / 142;
                brightness = MathUtils.clamp(brightness, 0, 1);
                this.brightness = brightness;
                this.saturation = 1 - saturation;//
                colorSetting.setColor(new Color(Color.HSBtoRGB(hue, saturation, brightness)));
            }

            drawRoundCircle(getXpos() + 4 + brightness * 142, y + 2 + (1 - saturation) * (60 - 10), 13, GuiColors.transform(ColorUtil.toRGBA(21, 21, 21, 255)));
            drawRoundCircle(getXpos() + 4 + brightness * 142, y + 2 + (1 - saturation) * (60 - 10), 12, GuiColors.transform(colorSetting.getColor().getRGB()));
        } else {
            boolean a = MainScreen.ishover(getXpos(), getYpos(), 150, getHeight() - 3, getMouseX(), getMouseY());
            fontManager.font16.drawString(ms, colorSetting.getName(), getXpos() + 10, getYpos(), a ? -1 : new Color(200, 200, 200).getRGB());
            RectUtil.drawRound(getXpos() + 115, getYpos(), 20, getHeight() - 3, 6, colorSetting.getColor().getRGB());
        }
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
        if (MainScreen.ishover(getXpos(), getYpos(), 150, getHeight(), getMouseX(), getMouseY()) && button == 1) {
            open = !open;
        }

        if (open) {
            if (MainScreen.ishover(getXpos() + 4, getYpos() + 10 + 2, 142, 50, getMouseX(), getMouseY()) && button == 0) {
                draggSaturation = true;
            }
            if (MainScreen.ishover(getXpos() + 4, getYpos() + 63, 142, 4, mouseX, mouseY) && button == 0) {
                draggingHue = true;
            }
        }
    }

    public static void drawRoundCircle(float x, float y, float radius, int color) {
        RectUtil.drawRound(x - (radius / 2), y - (radius / 2), radius, radius, (radius / 2) - 0.5f, color);
    }


    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        super.mouseReleased(mouseX, mouseY, button);
        draggingHue = false;
        draggSaturation = false;
    }

    public void drawPicker(float x, float y, float radius, float bright) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glShadeModel(7425);
        GlStateManager._blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        GL11.glBegin(GL_LINE_STRIP);

        for (int i = 0; i < 360; i++) {
            ColorUtil.setColor(Color.HSBtoRGB(i / 360f, 0, bright));
            glVertex2d(x, y);
            ColorUtil.setColor(Color.HSBtoRGB(i / 360f, 1, bright));
            glVertex2d(x + Math.sin(Math.toRadians(i)) * radius, y + Math.cos(Math.toRadians(i)) * radius);
        }
        GL11.glEnd();

        GL11.glBegin(GL_LINE_LOOP);

        for (int i = 0; i < 360; i++) {
            ColorUtil.setColor(Color.HSBtoRGB(i / 360f, 1, bright));
            glVertex2d(x + Math.sin(Math.toRadians(i)) * radius, y + Math.cos(Math.toRadians(i)) * radius);
        }
        GL11.glEnd();

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL_LINE_SMOOTH);
        GL11.glShadeModel(7424);


        GL11.glPopMatrix();


    }

    @Override
    public float getHeight() {
        return open ? 70 : 15;
    }
}

