package com.denger.client.modules.mods.hud;

import com.denger.client.MainNative;
import com.denger.client.another.hooks.forge.even.addevents.Event2D;
import com.denger.client.another.hooks.forge.even.addevents.EventInit;
import com.denger.client.another.hooks.forge.even.addevents.ModuleToggleEvent;
import com.denger.client.modules.Module;
import com.denger.client.modules.ShineModule;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import com.denger.client.utils.AnimationUtils;
import com.denger.client.utils.rect.BloomUtil;
import com.denger.client.utils.rect.RectUtil;
import com.mojang.blaze3d.systems.IRenderCall;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

import static com.denger.client.MainNative.*;

@ModuleTarget(ModName = "NpevmfMjtu", category = Category.HUD, description = "отображает список функций")
public class ModuleList extends ShineModule {

    private final List<Mod> mods = new ArrayList<>();;

    float x = 1.0f;
    float y = 30.0f;
    float h = 8.5f;
    @Override
    public void onEventContinuous(Event e) {
        if (e instanceof EventInit) {
            getInstance.getRegisterModule().getModules().forEach(module -> {
                mods.add(new Mod(module));
            });
            mods.sort((module1, module2) -> MainNative.fontManager.code16.getStringWidth(module2.getModule().getName()) - MainNative.fontManager.code16.getStringWidth(module1.getModule().getName()));
        }
        if (e instanceof ModuleToggleEvent) {
            ModuleToggleEvent event = (ModuleToggleEvent) e;
            Mod mod = mods.stream().filter(mod1 -> {
                return mod1.getModule() == event.getModule();
            }).findFirst().get();
            mod.animX.reset();
            mod.animY.reset();
            if (event.getState()) {
                mod.animX.to = 10;
                mod.animY.to = 10;
            } else {
                mod.animX.to = -20;
                mod.animY.to = -20;
            }
        }

    }


    @SubscribeEvent
    public void onRender2D(Event2D e) {

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
                RectUtil.drawGradientRound(locX, locY + 4, fontManager.code16.getStringWidth(m.getModule().getName()) + 7, h * 0.985f + 1, 1.8f, color2, color1, color2, color1);
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
        });

    }

    static class Mod {
        AnimationUtils animX;
        AnimationUtils animY;
        Module module;

        public Mod(Module m) {
            module = m;
            animX = new AnimationUtils(-20, -20, 0.9f);
            animY = new AnimationUtils(-20, -20, 0.9f);
        }

        public Module getModule() {
            return module;
        }
    }
}