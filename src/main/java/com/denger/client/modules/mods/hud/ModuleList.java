package com.denger.client.modules.mods.hud;

import com.denger.client.MainNative;
import com.denger.client.another.hooks.forge.even.addevents.Event2D;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import com.denger.client.utils.AnimationUtils;
import com.denger.client.utils.Crypt;
import com.denger.client.utils.rect.BloomUtil;
import com.denger.client.utils.rect.RectUtil;
import com.mojang.blaze3d.systems.IRenderCall;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static com.denger.client.MainNative.fontManager;
import static com.denger.client.MainNative.getInstance;

@ModuleTarget(ModName = "NpevmfMjtu", category = Category.HUD, description = "отображает список функций")
public class ModuleList extends Module {

    private List<Mod> mods;

    float x = 1.0f;
    float y = 30.0f;
    float h = 8.5f;
    int gray = new Color(0xB0B0B2).getRGB();
    @Override
    public void onEnable() {
        if (mods == null) {
            mods = new ArrayList<>();
            getInstance.getRegisterModule().getModules().forEach(module -> {
                mods.add(new Mod(module));
            });
            mods.sort((module1, module2) -> MainNative.fontManager.code16.getStringWidth(module2.getName()) - MainNative.fontManager.code16.getStringWidth(module1.getName()));
        }
    }


    @SubscribeEvent
    public void onRender2D(Event2D e) {
        mods.stream().filter(mod -> {
            return !mod.name.equals(mod.getName());
        }).forEach(mod -> {
            mod.name = mod.getName();
            mods.sort((module1, module2) -> MainNative.fontManager.code16.getStringWidth(module2.getName()) - MainNative.fontManager.code16.getStringWidth(module1.getName()));
        });
        mods.stream().filter(mod -> {
            return mod.lasState != mod.module.getState();
        }).forEach(mod -> {
            if (mod.module.getState()) {
                mod.animX.to = 10;
                mod.animY.to = 10;
            } else {
                mod.animX.to = -20;
                mod.animY.to = -20;
            }
            mod.lasState = mod.module.getState();
        });
        final int[] mval = {0};
        IRenderCall bg = () -> {
            mods.stream().filter(mod -> {
                return mod.animX.getAnim() > -19;
            }).forEach(m -> {
                m.animY.to = ((y + mval[0] * (h)));
                float locY = m.animY.getAnim();
                float locX = x + m.animX.getAnim();
                int num = mval[0];
                int color1 = MainNative.getInstance.theme.getColor(num * 5);
                int color2 = MainNative.getInstance.theme.getColor((num - 1) * 5);
                RectUtil.drawGradientRound(locX, locY + 4, fontManager.code16.getStringWidth(m.name) + 7, h * 0.985f + 1, 1.8f, color2, color1, color2, color1);
                RectUtil.drawGradientRound(locX, locY + 4, 2, h * 1.1f, 0, color2, color1, color2, color1);
                mval[0]++;
            });

        };
        BloomUtil.registerRenderCall(bg);
        BloomUtil.draw(14);

        mods.stream().filter(mod -> {
            return mod.animX.getAnim() > -19;
        }).forEach(m -> {
            float locY = m.animY.getAnim();
            float locX = x + m.animX.getAnim();
            fontManager.code16.drawString(e.getMs(), m.getModule().getName(), locX + 2, locY + 2, -1);
            if (m.getModule().getModSetting() != null) {
                fontManager.code16.drawString(e.getMs(), "-> [ " + m.mod + " ]", locX + 2 + fontManager.code16.getStringWidth(m.getModule().getName()), locY + 2, gray);
            }
        });

    }

    static class Mod {
        AnimationUtils animX;
        AnimationUtils animY;
        Module module;
        boolean lasState;
        String name;
        String mod = "";

        public Mod(Module m) {
            module = m;
            name = module.getName() + (module.getModSetting() != null ? module.getModSetting().getCurent() : "");
            animX = new AnimationUtils(-20, -20, 0.9f);
            animY = new AnimationUtils(-20, -20, 0.9f);
        }

        public Module getModule() {
            return module;
        }

        public String getName() {
            if (module.getModSetting() != null) mod = Crypt.encrypt(module.getModSetting().getCurent());
            return module.getName() + (module.getModSetting() != null ? "-> [" + module.getModSetting().getCurent() + "]" : "");
        }
    }
}