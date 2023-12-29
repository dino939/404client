package com.denger.client.modules.mods.misc;

import com.denger.client.another.hooks.forge.even.addevents.Event2D;
import com.denger.client.another.hooks.forge.even.addevents.WorldUpdate;
import com.denger.client.another.resource.NativeManager;
import com.denger.client.another.settings.SettingTarget;
import com.denger.client.another.settings.sett.BoolSetting;
import com.denger.client.another.sound.SoundManager;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import com.denger.client.utils.AnimationUtil;
import com.denger.client.utils.MathUtils;
import com.denger.client.utils.Utils;
import com.denger.client.utils.rect.BlurUtil;
import com.denger.client.utils.rect.RectUtil;
import com.denger.client.utils.rect.RenderUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Objects;

import static com.denger.client.MainNative.getInstance;
import static com.denger.client.MainNative.mc;

@ModuleTarget(ModName = "EfuiNpwf", category = Category.MISC)
public class DethMove extends Module {
    private Entity entity;
    private boolean toRender;
    @SettingTarget(name = "Толко игроков?")
    BoolSetting onlyPlayer = new BoolSetting().setBol(true);
    AnimationUtil anim = new AnimationUtil(0, 0, 10000);

    @SubscribeEvent
    public void onAttack(AttackEntityEvent event) {
        if (event.getPlayer() == mc.player) {
            if (!onlyPlayer.getState() || entity instanceof PlayerEntity) {
                entity = event.getTarget();
            }
        }
    }

    @SubscribeEvent
    public void onUpdate(WorldUpdate e) {
        if (entity == null) return;
        if (entity.distanceTo(Objects.requireNonNull(mc.player)) > 6.5f) {
            entity = null;
        }
        if (entity != null) {
            if (!entity.isAlive() || !entity.isAddedToWorld()) {
                onDeth(entity);
            }
        }
    }

    public void onDeth(Entity e) {
        SoundManager.playSound(getInstance.getSoundManager().wasted, 20);
        Utils.SetTimer(7);
        toRender = true;
        anim.goTo(1);
        entity = null;
        Utils.sleepVoid(() -> {
            anim.goTo(0);
        }, 3550);
        Utils.sleepVoid(() -> {
            toRender = false;
            Utils.SetTimer(20);
            anim.setTo(0);
        }, 4000);
    }

    @SubscribeEvent
    public void onRender(Event2D e) {
        if (toRender) {
            BlurUtil.drawBlur((int) (anim.getAnim() * 6), () -> {
                RectUtil.drawRect(0, 0, MathUtils.calc(mc.getWindow().getGuiScaledWidth()), MathUtils.calc(mc.getWindow().getGuiScaledHeight()), -1);
            });
            RenderUtil.scale(0, 0, MathUtils.calc(mc.getWindow().getGuiScaledWidth()), MathUtils.calc(mc.getWindow().getGuiScaledHeight()), anim.getAnim(), () -> {
                RenderUtil.drawImage(e.getMs(), NativeManager.getResource("imagessss", getInstance.getNativeManager().image), ((float) MathUtils.calc(mc.getWindow().getGuiScaledWidth()) / 2) - 200, ((float) MathUtils.calc(mc.getWindow().getGuiScaledHeight()) / 2) - 50, 400, 100, anim.getAnim() * 255);

            });
        }
    }
}
