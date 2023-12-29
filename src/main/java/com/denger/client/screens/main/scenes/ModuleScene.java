package com.denger.client.screens.main.scenes;

import com.denger.client.MainNative;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.screens.main.comp.MoulePanel;
import com.denger.client.screens.main.comp.Scene;
import com.denger.client.utils.AnimationUtil;
import com.denger.client.utils.MathUtils;
import com.mojang.blaze3d.matrix.MatrixStack;

import java.util.ArrayList;

import static com.denger.client.MainNative.getInstance;

public class ModuleScene extends Scene {
    ArrayList<MoulePanel> array1, array2, array3;
    ArrayList<MoulePanel> panels;
    private float scrol = 0;
    private AnimationUtil scrole;
    float startPosY = 16;
    float dist = 9;
    float distW = 12;

    public ModuleScene(Category category) {
        ArrayList<Module> modules = new ArrayList<>();
        scrole = new AnimationUtil(0, 0, 50);
        for (Module module : getInstance.getRegisterModule().getModules()) {
            if (module == null) return;
            if (category == module.getCategory()) {
                modules.add(module);

            }
        }

        array1 = new ArrayList<>();
        array2 = new ArrayList<>();
        array3 = new ArrayList<>();
        int itemsPerList = (int) Math.ceil((double) modules.size() / 3);
        panels = new ArrayList<>();
        int a = 1;
        for (int i = 0; i < modules.size(); i++) {
            if (a == 1) {
                array1.add(new MoulePanel(modules.get(i)));
                a++;
            } else if (a == 2) {
                array2.add(new MoulePanel(modules.get(i)));
                a++;
            } else if (a == 3) {
                array3.add(new MoulePanel(modules.get(i)));
                a = 1;
            }
        }
        panels.addAll(array1);
        panels.addAll(array2);
        panels.addAll(array3);
    }


    @Override
    public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        float pos1 = startPosY + scrole.getAnim();
        float pos2 = startPosY + scrole.getAnim();
        float pos3 = startPosY + scrole.getAnim();

        for (MoulePanel loc : array1) {
            loc.render(p_230430_1_, getX() + distW, getY() + pos1);
            pos1 += loc.getHeight() + dist;
        }

        for (MoulePanel loc : array2) {
            loc.render(p_230430_1_, getX() + loc.getWidth() + distW * 2, getY() + pos2);
            pos2 += loc.getHeight() + dist;
        }
        for (MoulePanel loc : array3) {
            loc.render(p_230430_1_, getX() + loc.getWidth() * 2 + distW * 3, getY() + pos3);
            pos3 += loc.getHeight() + dist;
        }
    }

    @Override
    public void onClose() {
        panels.forEach(MoulePanel::close);
    }

    @Override
    public void mouseClicked(double p_231044_1_, double p_231044_3_, int p_231044_5_) {
        panels.forEach(moulePanel -> moulePanel.mouseClicked(p_231044_1_, p_231044_3_, p_231044_5_));
    }

    @Override
    public void mouseReleased(double p_231048_1_, double p_231048_3_, int p_231048_5_) {
        panels.forEach(moulePanel -> moulePanel.mouseReleased(p_231048_1_, p_231048_3_, p_231048_5_));

    }

    @Override
    public void keyPressed(int p_231046_1_, int p_231046_2_, int p_231046_3_) {
        panels.forEach(moulePanel -> moulePanel.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_));

    }

    @Override
    public void scrole(float num) {
        scrol += num * (6.5 + (MainNative.LShift ? 6 : 0));
        float size = 0;
        float size1 = 0;
        float size2 = 0;
        for (MoulePanel loc : array1) {
            size += loc.getHeight() + dist;
        }

        for (MoulePanel loc : array2) {
            size1 += loc.getHeight() + dist;
        }
        for (MoulePanel loc : array3) {
            size2 += loc.getHeight() + dist;
        }
        float midle = (size + size1 + size2) / 3;
        scrol = MathUtils.clamp(scrol, -midle, 5);
        scrole.goTo(scrol);
    }
}
