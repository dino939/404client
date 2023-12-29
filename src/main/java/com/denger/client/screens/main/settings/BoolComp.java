package com.denger.client.screens.main.settings;

import com.denger.client.another.settings.Setting;
import com.denger.client.another.settings.sett.BoolSetting;
import com.denger.client.screens.main.MainScreen;
import com.denger.client.screens.main.comp.SettComp;
import com.denger.client.utils.AnimationUtil;
import com.denger.client.utils.Utils;
import com.denger.client.utils.rect.RectUtil;
import com.denger.client.utils.rect.RenderUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.denger.client.screens.main.comp.GuiColors;

import static com.denger.client.MainNative.fontManager;

public class BoolComp extends SettComp {
    private BoolSetting boolSetting;
    AnimationUtil animationUtil;
    public BoolComp(Setting s) {
        super(s);
        boolSetting = (BoolSetting) s;
        animationUtil = new AnimationUtil(0,0,200);
    }

    @Override
    public void Render(MatrixStack ms, float Xpos, float Ypos) {
        super.Render(ms, Xpos, Ypos);
         Utils.bool(boolSetting.getState(),()->{animationUtil.goTo(0.65f);},()->{animationUtil.goTo(0);});
        fontManager.font16.drawString(ms,boolSetting.getName(),getXpos()+10,getYpos(),-1);
        RectUtil.drawRound(Xpos +128,Ypos+getHeight()/2-7,10,10,2, GuiColors.White.getColor());
        RenderUtil.scale(Xpos +128,Ypos+getHeight()/2-7,10,10,animationUtil.getAnim(),()->{
            RectUtil.drawRound(Xpos +128,Ypos+getHeight()/2-7,10,10,2, GuiColors.getThemeColor());

        });

        //RectUtil.drawRect(getXpos()+10,getYpos(),128,getHeight(),-1);

        //RectUtil.drawRound();
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
       if (MainScreen.ishover(getXpos()+10,getYpos(),128,getHeight(),mouseX,mouseY) && button == 0){
           boolSetting.toggle();
       }
     }

    @Override
    public float getHeight() {
        return 15;
    }
}
