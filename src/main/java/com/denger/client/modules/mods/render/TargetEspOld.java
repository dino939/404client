package com.denger.client.modules.mods.render;

import com.denger.client.another.hooks.forge.even.addevents.Event2D;
import com.denger.client.another.settings.SettingTarget;
import com.denger.client.another.settings.sett.FloatSetting;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import com.denger.client.utils.ESPUtil;
import com.denger.client.utils.Transform;
import com.denger.client.utils.Utils;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.opengl.GL46;

import java.util.Objects;

import static com.denger.client.Main.mc;
import static com.mojang.blaze3d.platform.GlStateManager.*;

@ModuleTarget(ModName = "UbshfuFtq", category = Category.RENDER)
public class TargetEspOld extends Module {
    private Entity e;
    Vector3d vet;
    @SettingTarget(name = "Размер")
    FloatSetting scale = new FloatSetting().setMin(1).setMax(5).setVal(2);

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (e != null) {
            if (!e.isAlive() || e.distanceTo(Objects.requireNonNull(mc.player)) > 10f) {
                e = null;
                return;
            }
            if (e != null) {
                vet = ESPUtil.toScreen(Utils.getVecEntity(e, event.getPartialTicks(), e.getBbHeight() / 2));

            } else {
                vet = null;
            }


        }
    }

    @SubscribeEvent
    public void on2D(Event2D ev) {
        if (vet == null || e == null) return;
        Transform.translate(vet.x(), vet.y(), 1);
        //Transform.rotate((float) (Math.sin((double) System.currentTimeMillis() / 1000) * 360), 0, 0, 1);
        Transform.scale(scale.getVal(), scale.getVal(), 1);
        GL46.glDepthMask(false);
        GL46.glDisable(2884);
        GL46.glEnable(3042);
        GL46.glDisable(3008);
        GL46.glEnable(GL46.GL_BLEND);
        GL46.glBlendFunc(GL46.GL_SRC_ALPHA, GL46.GL_ONE);

        //RenderUtil.drawImage(ev.getMs(), getInstance.getGifManager().getGifs().get(1).getResource(), -20, -25, 40, 40);

        GL46.glBlendFunc(GL46.GL_SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.value);
        GL46.glDisable(GL46.GL_BLEND);
        GL46.glEnable(3008);
        GL46.glDisable(3042);
        GL46.glEnable(2884);
        GL46.glDepthMask(true);

        _disableBlend();
        Transform.stop();
    }

    @SubscribeEvent
    public void onAttack(AttackEntityEvent event) {
        if (event.getPlayer() == mc.player) {
            e = event.getTarget();
        }
    }


}
