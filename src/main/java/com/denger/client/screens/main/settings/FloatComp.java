package com.denger.client.screens.main.settings;

import com.denger.client.another.settings.Setting;
import com.denger.client.another.settings.sett.FloatSetting;
import com.denger.client.screens.main.comp.SettComp;
import com.denger.client.utils.AnimationUtil;
import com.denger.client.utils.MathUtils;
import com.denger.client.utils.rect.RectUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.denger.client.screens.main.comp.GuiColors;

import java.text.DecimalFormat;

import static com.denger.client.Main.fontManager;
import static com.denger.client.Main.getInstance;
import static com.denger.client.screens.main.MainScreen.ishover;

public class FloatComp extends SettComp {
    FloatSetting local;
    AnimationUtil animX;
    AnimationUtil animVal;

    boolean flag;
    public FloatComp(Setting s) {
        super(s);
        local = (FloatSetting) s;
        float temp = MathUtils.calcPercentage(local.getVal(),local.getMin(),local.getMax());
        animX = new AnimationUtil(temp,temp,50);
        animVal = new AnimationUtil(local.getVal(),local.getVal(),50);

    }

    @Override
    public void Render(MatrixStack ms, float Xpos, float Ypos) {
         Ypos += 9;
        super.Render(ms, Xpos, Ypos);
        float temp = MathUtils.calcPercentage(local.getVal(),local.getMin(),local.getMax());
        temp =  MathUtils.calculateValue(temp,0,130);
        animX.goTo(temp);
        animVal.goTo( local.getVal());


        fontManager.font12.drawString(ms, local.getName(),getXpos()+12,getYpos()-10,-1);
        DecimalFormat decimalFormat = new DecimalFormat( "#.##" );
        String text = decimalFormat.format(animVal.getAnim()) + local.getType();
        RectUtil.drawRound(getXpos()+125.8f - (float) fontManager.font10.getStringWidth(text) /2,getYpos()-10.01f,fontManager.font10.getStringWidth(text)+4.4f,fontManager.font10.getFontHeight()+.4f,1,GuiColors.White.getColor(100));

        RectUtil.drawRound(getXpos()+126 - (float) fontManager.font10.getStringWidth(text) /2,getYpos()-10,fontManager.font10.getStringWidth(text)+4,fontManager.font10.getFontHeight(),1,  GuiColors.Black.getColor(210) );
        fontManager.font10.drawCenteredString(ms,text,getXpos()+127,getYpos()-10,-1);
        // RectUtil.drawRect(getXpos()+128 - (float) fontManager.font10.getStringWidth(text) /2,getYpos()-10,fontManager.font10.getStringWidth(text),fontManager.font10.getFontHeight(),-1);
        RectUtil.drawRound(getXpos()+10,getYpos(),130,2.5f,0.5f,-1);
        RectUtil.drawRound(getXpos()+10,getYpos(),animX.getAnim(),2.5f,0.5f, getInstance.theme.getC().getRGB());
        RectUtil.drawRound(getXpos()+animX.getAnim()+8,getYpos()-2f,3,7,0, -1);
        if (flag){
            float loc2 = MathUtils.clamp(getMouseX() - getXpos() - 10,0,130);
            float loc = MathUtils.calcPercentage(loc2,0,130);
            loc = MathUtils.calculateValue(loc,local.getMin(),local.getMax());
            local.setVal(loc);
        }
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && ishover(getXpos()+10,getYpos()-4,130,8,mouseX,mouseY)){
            flag = true;
        }
        super.mouseClicked(mouseX, mouseY, button);

    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0){
            flag = false;
        }
        super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public float getHeight() {
        return 18;
    }
}
