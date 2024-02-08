package com.denger.client.screens.main.settings;


import com.denger.client.modules.Module;
import com.denger.client.screens.main.comp.SettComp;
import com.denger.client.utils.Utils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.denger.client.screens.main.comp.GuiColors;
import org.lwjgl.glfw.GLFW;

import static com.denger.client.Main.fontManager;
import static com.denger.client.screens.main.MainScreen.ishover;

public class BindComp extends SettComp {
    private boolean binding;

    String buttons;
    Module module;

    public BindComp(Module m) {
        super(m);
        this.module = m;
    }

    @Override
    public void Render(MatrixStack ms, float Xpos, float Ypos) {
        super.Render(ms, Xpos, Ypos);
        buttons = binding ? "Binding" : Utils.getKeyName(module.getKeycode()).toUpperCase();
        //RectUtil.drawRound(getXpos() + 25, getYpos() + 2, 50, 10, 3, -1);
        fontManager.font20.drawCenteredString(ms, buttons, getXpos() + 75, getYpos(), GuiColors.White.getColor());
     }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
         boolean d = ishover(getXpos() + 50, getYpos() + 2, 50, 10, mouseX, mouseY) && button == 0;
        if (d) {
            binding = !binding;
        }
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        super.keyPressed(keyCode, scanCode, modifiers);
        if (modifiers == 0 && binding) {
            if ( keyCode != GLFW.GLFW_KEY_DELETE && keyCode != GLFW.GLFW_KEY_ESCAPE) {
                module.setKeycode(keyCode);
            } else {
                module.setKeycode((keyCode == GLFW.GLFW_KEY_DELETE) ? 0 : module.getKeycode());
             }
            binding = false;

        }
    }

    @Override
    public void close() {
         binding = false;
    }

    @Override
    public float getHeight() {
        return fontManager.font28.getFontHeight();
    }

    @Override
    public boolean isVisible() {
        return true;
    }
}
