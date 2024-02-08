package com.denger.client.another.hooks;

import com.denger.client.another.hooks.forge.even.addevents.Event2D;
import com.denger.client.another.hooks.forge.even.addevents.WorldUpdate;
import com.denger.client.modules.mods.combat.HitBox;
import com.denger.client.modules.mods.combat.NoPush;
import com.denger.client.utils.AnimationUtil;
import com.denger.client.utils.rect.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.profiler.IProfileResult;
import net.minecraft.profiler.IResultableProfiler;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.MinecraftForge;

import java.util.function.Supplier;

import static com.denger.client.Main.getInstance;
import static com.denger.client.Main.mc;


public class ProfilerHook implements IResultableProfiler {


    public static IResultableProfiler INSTANCE = new ProfilerHook();


    @Override
    public IProfileResult getResults() {
        return null;
    }

    @Override
    public void startTick() {

    }

    @Override
    public void endTick() {

    }

    @Override
    public void push(String p_76320_1_) {
        if (p_76320_1_.contains("livingEntityBaseTick")){
            if (mc.player.isInWall() && getInstance.getRegisterModule().isEnable(NoPush.class)){
                mc.player.setBoundingBox(new AxisAlignedBB( mc.player.getX(),  mc.player.getY(),  mc.player.getZ(),  mc.player.getX(),   mc.player.getY(),  mc.player.getZ()));
            }


        }
        if (p_76320_1_.contains("render")) {
            if (getInstance.getRegisterModule().isEnable(HitBox.class) && HitBox.nevidimie.getState()) {
                assert mc.level != null;
                for (PlayerEntity playerEntity : mc.level.players()) {
                    if (playerEntity == null) continue;
                    if (playerEntity != mc.player) {
                        playerEntity.setBoundingBox(new AxisAlignedBB(playerEntity.getX() - 0.3, playerEntity.getBoundingBox().minY, playerEntity.getZ() - 0.3, playerEntity.getX() + 0.3, playerEntity.getBoundingBox().maxY, playerEntity.getZ() + 0.3));
                    }
                }
            }

        }
        if (p_76320_1_.contains("tick")) {
            if (Minecraft.getInstance().player != null && Minecraft.getInstance().level != null) {
                MinecraftForge.EVENT_BUS.post(new WorldUpdate());
            }


        }
        if (p_76320_1_.contains("bossHealth")) {
            if (Minecraft.getInstance().player != null && Minecraft.getInstance().level != null) {
                RenderUtil.setScaleRender(2);
                MinecraftForge.EVENT_BUS.post(new Event2D());
                RenderUtil.setScaleRenderStandar();
            }
        }

    }


    @Override
    public void push(Supplier<String> p_194340_1_) {
    }

    @Override
    public void pop() {

    }

    @Override
    public void popPush(String p_219895_1_) {
        if (p_219895_1_.contains("update")) {
            AnimationUtil.update();

        }
    }

    @Override
    public void popPush(Supplier<String> p_194339_1_) {

    }

    @Override
    public void incrementCounter(String p_230035_1_) {

    }

    @Override
    public void incrementCounter(Supplier<String> p_230036_1_) {

    }
}
