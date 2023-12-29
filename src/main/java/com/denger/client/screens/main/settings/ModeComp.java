package com.denger.client.screens.main.settings;

import com.denger.client.another.settings.Setting;
import com.denger.client.another.settings.sett.ModSetting;
import com.denger.client.screens.main.comp.SettComp;
import com.denger.client.utils.AnimationUtil;
import com.denger.client.utils.ColorUtil;
import com.denger.client.utils.rect.RectUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.denger.client.screens.main.comp.GuiColors;

import java.awt.*;
import java.util.ArrayList;

import static com.denger.client.MainNative.*;
import static com.denger.client.screens.main.MainScreen.ishover;

public class ModeComp extends SettComp {
    ArrayList<Mode> modes = new ArrayList<>();
    AnimationUtil anim;
    ModSetting setting;
    private float width;
    public ModeComp(Setting setting) {
        super(setting);
        width = 100;
        anim = new AnimationUtil(0, 0, 150);
        this.setting = (ModSetting) setting;
        for (String s : this.setting.getMods()){
            modes.add(new Mode(s));
        }
    }

    @Override
    public void Render(MatrixStack ms, float Xpos, float Ypos) {
        super.Render(ms,Xpos+35,Ypos-3);
        boolean a = ishover(getXpos(),getYpos(),  getWidth(),  getHeight(), getMouseX(),getMouseY());
        fontManager.font16.drawString(new MatrixStack(),setting.getName(), getXpos()-25,getYpos()+getHeight()/2-fontManager.font16.getFontHeight()/2, GuiColors.transform( a? -1 : new Color(200, 200, 200).getRGB()));
        int max = 1;
        float size = 0;
        float nowSize = 0;
        boolean sizable = true;
        for (Mode mode : modes){
            if (fontManager.font16.getStringWidth(mode.name) > max){
                max = fontManager.font16.getStringWidth(mode.name);
            }
            size +=fontManager.font16.getStringWidth(mode.name)+5;
            if (mode.name.equals((setting).getCurent())){
                sizable = false;
            }
            if (sizable){
                nowSize +=fontManager.font16.getStringWidth(mode.name)+5;
            }

        }
        float posX = getXpos()+getWidth()-5-7-7-max;
        fontManager.font16.drawCenteredString(new MatrixStack(),"<", posX+7/2, getYpos()+getHeight()/2- (double) fontManager.font16.getFontHeight() /2, GuiColors.transform(new Color(200, 200, 200).getRGB()));
        fontManager.font16.drawCenteredString(new MatrixStack(),">", posX+7+max+7/2, getYpos()+getHeight()/2- (double) fontManager.font16.getFontHeight() /2, GuiColors.transform(new Color(200, 200, 200).getRGB()));

        anim.goTo(nowSize);
        float yz = 0;
        float pp =posX+7+5+max/2-(modes.size()*4)+4;
        for (Mode mode : modes){
            yz+=mode.render(posX+7-anim.getAnim()+yz+ (float) (max - fontManager.font16.getStringWidth(setting.getCurent())) /2, getYpos()+getHeight()/2- (float) fontManager.font16.getFontHeight() /2, getMouseX(), getMouseY(), getXpos(), getYpos(),a)+5;
            RectUtil.drawCircle(pp, getYpos()+getHeight()/2+ (double) fontManager.font16.getFontHeight() /2+2, 1.5f, GuiColors.transform(mode.name.equals(setting.getCurent()) ? getInstance.theme.getC().getRGB() : new Color(200, 200, 200).getRGB()));
            pp+=4;
        }



    }
    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
         if (button == 0){
            int max = 1;
            for (Mode mode : modes){
                if (fontManager.font16.getStringWidth(mode.name) > max){
                    max = fontManager.font16.getStringWidth(mode.name);
                }
            }
            float posX = getXpos()+getWidth()-5-7-7-max;
            if (ishover(posX, getYpos(),  7,  getHeight(), mouseX, mouseY)){
                if (setting.getMods().indexOf(setting.getCurent()) != 0){
                    setting.setCurent(setting.getMods().get(setting.getMods().indexOf(setting.getCurent())-1));
                }

            }
            if (ishover(posX+7+max, getYpos(),  7+max+7,  getHeight(), mouseX, mouseY)){
                if (setting.getMods().indexOf(setting.getCurent()) != setting.getMods().size()-1){
                    setting.setCurent(setting.getMods().get(setting.getMods().indexOf(setting.getCurent())+1));
                }
            }
        }
    }


    @Override
    public float getHeight() {
        return 15;
    }

    @Override
    public void close() {
        super.close();
        anim.setAnim(0);
    }

    class Mode{
        String name;
        AnimationUtil animAlpha;
        public Mode(String name){
            this.name = name;
            animAlpha = new AnimationUtil(0, 0, 150);
        }

        public float render(float x, float y, float mouseX, float mouseY, float globX, float globY,boolean a){
            animAlpha.goTo(name.equals((setting).getCurent()) ? 255 : 0);
            if (animAlpha.getAnim() >= 50){
                fontManager.font16.drawString(new MatrixStack(),name, (double) x, y, ColorUtil.swapAlpha(a ? GuiColors.transform(-1) :GuiColors.transform(new Color(200, 200, 200).getRGB()) , (int) animAlpha.getAnim()));

            }
            return fontManager.font16.getStringWidth(name);
        }

    }

    public float getWidth() {
        return width;
    }
}
