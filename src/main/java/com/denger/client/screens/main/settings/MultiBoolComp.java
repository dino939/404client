package com.denger.client.screens.main.settings;

import com.denger.client.another.settings.Setting;
import com.denger.client.another.settings.sett.BoolSetting;
import com.denger.client.another.settings.sett.MultiBoolSetting;
import com.denger.client.screens.main.MainScreen;
import com.denger.client.screens.main.comp.GuiColors;
import com.denger.client.screens.main.comp.SettComp;
import com.denger.client.utils.anims.Animation;
import com.denger.client.utils.ColorUtil;
import com.denger.client.utils.rect.RenderUtil;
import com.mojang.blaze3d.matrix.MatrixStack;

import java.awt.*;
import java.util.HashMap;

import static com.denger.client.Main.fontManager;
import static com.denger.client.Main.getInstance;

public class MultiBoolComp extends SettComp {
    boolean open = false;
    MultiBoolSetting setting;
    HashMap<BoolSetting, Animation> hashMap;
    Animation anim = new Animation(-90,-90,0.2f);
    public MultiBoolComp(Setting s) {
        super(s);
        setting = (MultiBoolSetting) s;
        hashMap = new HashMap<>();
        setting.getBoolSettings().forEach(set -> {
            hashMap.put(set, new Animation(set.getState() ? 1 : 0.4f, set.getState() ? 1 : 0.4f, 0.2f));
        });
    }

    @Override
    public void Render(MatrixStack ms, float Xpos, float Ypos) {
        super.Render(ms, Xpos, Ypos);
        String name = setting.getName();
        float strW = fontManager.font16.getStringWidth(name);
        float StartY = getYpos() + 2.5f;
        fontManager.font16.drawCenteredString(ms, name, getXpos() + 75, StartY, -1);
        float finalStartY = StartY;
        RenderUtil.rotate(getXpos() + fontManager.font16.getStringWidth(name)+55, StartY+5,6,6,anim.getAnim(),()->{
            fontManager.font16.drawString(ms, "<", getXpos() + fontManager.font16.getStringWidth(name)+55, finalStartY+5, -1);
        });

       // RectUtil.drawRound(getXpos() + fontManager.font16.getStringWidth(name)+55, StartY+4,6,6,1,-1);
        if (open) {
            for (BoolSetting boolSetting : setting.getBoolSettings()) {
                StartY += fontManager.font16.getFontHeight();
                hashMap.get(boolSetting).to = boolSetting.getState() ? 1 : 0.4f;
                hashMap.get(boolSetting).speed = 0.05f;
                fontManager.font16.drawCenteredString(ms, boolSetting.getName(), getXpos() + 75, StartY, GuiColors.transform(ColorUtil.TwoColoreffect(getInstance.theme.getC().getRGB(), Color.LIGHT_GRAY.getRGB(), hashMap.get(boolSetting).getAnim()).getRGB()));
            }
        }
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (MainScreen.ishover(getXpos() + 10, getYpos() + 5, 130, 15, mouseX, mouseY) && button == 1) {
            open = !open;
            if (open){
                anim.to = 90;
            }else anim.to = -90;
        }
        float StartY = getYpos() + 2.5f;
        for (BoolSetting boolSetting : setting.getBoolSettings()) {
            StartY += fontManager.font16.getFontHeight();
            if (MainScreen.ishover(getXpos(), StartY + 2.5f, 150, 10, mouseX, mouseY) && button == 0) {
                boolSetting.setBol(!boolSetting.getState());
            }
            //  fontManager.font16.drawCenteredString(ms, boolSetting.getName(), getXpos() + 75, StartY, GuiColors.transform(boolSetting.getState()?-1:Color.LIGHT_GRAY.getRGB()));
        }
        super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public float getHeight() {
        return open ? (16.5f) + (setting.getBoolSettings().size() * fontManager.font16.getFontHeight()) : 17;
    }
}
