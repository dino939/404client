package com.denger.client.screens.main.comp;

import com.denger.client.another.settings.Setting;
import com.denger.client.another.settings.sett.*;
import com.denger.client.modules.Module;
import com.denger.client.screens.main.settings.*;
import com.denger.client.utils.AnimationUtil;
import com.denger.client.utils.rect.RectUtil;
import com.denger.client.utils.rect.RenderUtil;
import com.mojang.blaze3d.matrix.MatrixStack;

import java.util.ArrayList;

import static com.denger.client.Main.fontManager;
import static com.denger.client.screens.main.MainScreen.ishover;

public class MoulePanel {

    private Module module;
    private float Xpos, Ypos, Width, Height, SettHeight;
    private AnimationUtil Settings, anim, enable;

    private ArrayList<SettComp> settComps;
    private boolean openSett = false;

    public MoulePanel(Module module) {
        this.module = module;
        settComps = new ArrayList<>();
        anim = new AnimationUtil(0, 0, 1000);
        enable = new AnimationUtil(0, 0, 1000);
        Settings = new AnimationUtil(0, 0, 1000);
        Height = 30;
        Width = 150;
        for (Setting s : module.getSettings()) {
            SettComp loc = createSetting(s);
            if (loc == null) continue;
            settComps.add(loc);
        }
        settComps.add(new BindComp(module));
    }

    public void render(MatrixStack ms, float Xpos, float Ypos) {
        this.Xpos = Xpos;
        this.Ypos = Ypos;
        enable.goTo(module.getState()?0.65f:0);
        anim.goTo(openSett?1:0);

        RectUtil.drawRound(Xpos, Ypos, Width, getHeight(), 4, GuiColors.Black.getColor(130));
        RectUtil.drawRound(Xpos + 128, Ypos + Height / 2 - 7, 10, 10, 2, GuiColors.White.getColor());
        RenderUtil.scale(Xpos + 128, Ypos + Height / 2 - 7, 10, 10, enable.getAnim(), () -> {
            RectUtil.drawRound(Xpos + 128, Ypos + Height / 2 - 7, 10, 10, 2, GuiColors.getThemeColor());

        });
        RectUtil.drawline(Xpos + 10, anim.getAnim() * (Width - 20), Ypos + 26, 0.1f, GuiColors.White.getColor());
        fontManager.code20.drawString(ms, module.getName(), Xpos + Width / 4 - 30, Ypos + 8, GuiColors.White.getColor());
        final float[] pos = {0};
        RenderUtil.StartScissor(Xpos, Ypos, Width, getHeight() - 5);
        settComps.forEach(s ->{
            if (!s.isVisible() || anim.getAnim() == 0) return;
            s.Render(ms, Xpos, Ypos + pos[0] + 30);
            pos[0] += s.getHeight();
        });
        if (anim.getAnim() != 0 && module.getDescription().length() <1 ){
            fontManager.font10.drawString(ms,"i",Xpos-1.6f+132, Ypos + pos[0]+ 13.5,-1);
            RectUtil.drawCircleOutline(ms,Xpos+132, Ypos + pos[0] + 18,5,1,-1);
        }

        RenderUtil.stopScissor();

        Settings.goTo(openSett ? pos[0] : 0);
     }


    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (ishover(Xpos, Ypos, Width, Height + (openSett ? -9 : 0), mouseX, mouseY) && button == 0) {
            module.toggle();
        }
        if (ishover(Xpos, Ypos, Width, Height, mouseX, mouseY) && button == 1) {
            openSett = !openSett;
        }
        if (openSett){
            settComps.stream().filter(SettComp::isVisible).forEach(settComp -> {
                settComp.mouseClicked(mouseX, mouseY, button);
            });
        }


    }

    public void mouseReleased(double mouseX, double mouseY, int button) {
        settComps.stream().filter(SettComp::isVisible).forEach(settComp -> {
            settComp.mouseReleased(mouseX, mouseY, button);
        });


    }

    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        settComps.stream().filter(SettComp::isVisible).forEach(settComp -> {
            settComp.keyPressed(keyCode, scanCode, modifiers);
        });

    }

    public Module getModule() {
        return module;
    }

    public float getHeight() {
        return Height + Settings.getAnim();
    }

    public float getWidth() {
        return Width;
    }

    public void close() {
        settComps.forEach(SettComp::close);
    }

    public SettComp createSetting(Setting s) {
        if (s instanceof FloatSetting) {
            return new FloatComp(s);

        } else if (s instanceof BoolSetting) {
            return new BoolComp(s);

        } else if (s instanceof ThemeSetting) {
            return new ThemeComp(s);
        } else if (s instanceof ModSetting) {

            return new ModeComp(s);
        } else if (s instanceof ColorSetting) {

        return new ColorComp(s);
    }else if (s instanceof MultiBoolSetting){
            return new MultiBoolComp(s);
        }
        return null;
    }

}
