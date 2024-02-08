package com.denger.client;

import by.radioegor146.nativeobfuscator.Native;
import com.denger.client.another.hooks.HookPlayerRenderer;
import com.denger.client.another.hooks.SocialInteractionsServiceHook;
import com.denger.client.another.hooks.TimeTrackerHook;
import com.denger.client.another.hooks.forge.even.addevents.WorldUpdate;
import com.denger.client.another.sound.SoundManager;
import com.denger.client.modules.mods.hud.Design;

import com.denger.client.utils.ReflectFileld;
import com.google.common.collect.Maps;
import com.mojang.authlib.minecraft.SocialInteractionsService;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.profiler.TimeTracker;
import net.minecraft.util.Timer;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Map;

import static com.denger.client.Main.*;

@Native
public class Execute {
    private final Map<String, PlayerRenderer> skinMap = Maps.newHashMap();
    public boolean customColor;
    public Design desing;
    Main main;

    public Execute(Main main) {
        this.main = main;
        getInstance.timer = new Timer(20.0F, 0L);
    }

    public void hook() {
        desing = (Design) main.getRegisterModule().getModules().stream().filter(module -> {
            return module instanceof Design;
        }).findFirst().get();
        new ReflectFileld(mc, Minecraft.class, 41).setValue(true);
        new ReflectFileld(mc, Minecraft.class, 42).setValue(true);
        new ReflectFileld(mc, Minecraft.class, SocialInteractionsService.class).setValue(new SocialInteractionsServiceHook());
        new ReflectFileld(mc, Minecraft.class, TimeTracker.class).setValue(new TimeTrackerHook(System::nanoTime, () -> 0));
        new Thread(() -> {
            try {
                Thread.sleep(3500);
                new ReflectFileld(mc, Minecraft.class, 14).setValue(getInstance.timer);
            } catch (Exception ignored) {
            }
        }).start();
    }

    @SubscribeEvent
    public void upDate(WorldUpdate e) {
        this.skinMap.put("default", new HookPlayerRenderer(Minecraft.getInstance().getEntityRenderDispatcher()));
        this.skinMap.put("slim", new HookPlayerRenderer(Minecraft.getInstance().getEntityRenderDispatcher(), true));
        new ReflectFileld(Minecraft.getInstance().getEntityRenderDispatcher(), EntityRendererManager.class, 2).setValueFinal(skinMap);
        new ReflectFileld(Minecraft.getInstance().getEntityRenderDispatcher(), EntityRendererManager.class, 3).setValueFinal(new HookPlayerRenderer(Minecraft.getInstance().getEntityRenderDispatcher()));
        eventManager.unregister(this);

    }

    public void panic() {
        new ReflectFileld(mc, Minecraft.class, TimeTracker.class).setValue(new TimeTracker(System::nanoTime, () -> 0));
        new ReflectFileld(mc, Minecraft.class, 14).setValue(new Timer(20.0F, 0L));
        this.skinMap.put("default", new PlayerRenderer(Minecraft.getInstance().getEntityRenderDispatcher()));
        this.skinMap.put("slim", new PlayerRenderer(Minecraft.getInstance().getEntityRenderDispatcher(), true));
        new ReflectFileld(Minecraft.getInstance().getEntityRenderDispatcher(), EntityRendererManager.class, 2).setValueFinal(skinMap);
        new ReflectFileld(Minecraft.getInstance().getEntityRenderDispatcher(), EntityRendererManager.class, 3).setValueFinal(new PlayerRenderer(Minecraft.getInstance().getEntityRenderDispatcher()));
        new ReflectFileld(mc, Minecraft.class, 25).setValueFinal(new GameRenderer(mc, mc.getResourceManager(), mc.renderBuffers()));
    }

    public void start() {
        SoundManager.playSound(getInstance.getSoundManager().starting, 15);
    }


}
