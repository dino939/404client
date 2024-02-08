package com.denger.client.modules.mods.render;

import com.denger.client.another.networkutills.ConnectionUtil;
import com.denger.client.another.settings.SettingTarget;
import com.denger.client.another.settings.sett.BoolSetting;
import com.denger.client.another.settings.sett.FloatSetting;
import com.denger.client.another.settings.sett.MultiBoolSetting;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import com.denger.client.utils.Blockinfo;
import com.denger.client.utils.rect.RenderUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.network.play.client.CPlayerDiggingPacket;
import net.minecraft.network.play.client.CPlayerTryUseItemOnBlockPacket;
import net.minecraft.network.play.server.SChunkDataPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;

import static com.denger.client.Main.mc;

@ModuleTarget(ModName = "YSbz", category = Category.RENDER)
public class XRay extends Module {
    public static ArrayList<Blockinfo> render = new ArrayList<>();
    Thread search;
    @SettingTarget(name = "Обломки",toAdd = false)
    private BoolSetting Ancient_Debris = new BoolSetting();
    @SettingTarget(name = "Алмазы",toAdd = false)
    private BoolSetting DIAMOND_ORE = new BoolSetting();
    @SettingTarget(name = "Редстоун",toAdd = false)
    private BoolSetting REDSTONE_ORE = new BoolSetting();
    @SettingTarget(name = "Лазурит",toAdd = false)
    private BoolSetting LAPIS_ORE = new BoolSetting();
    @SettingTarget(name = "Железо",toAdd = false)
    private BoolSetting IRON_ORE = new BoolSetting();
    @SettingTarget(name = "Золото",toAdd = false)
    private BoolSetting GOLD_ORE = new BoolSetting();
    @SettingTarget(name = "Изумруды",toAdd = false)
    private BoolSetting EMERALD_ORE = new BoolSetting();
    @SettingTarget(name = "Уголь",toAdd = false)
    private BoolSetting COAL_ORE = new BoolSetting();
    @SettingTarget(name = "Квартц",toAdd = false)
    private BoolSetting QUARTZ_ORE = new BoolSetting();
    @SettingTarget(name = "Спавнер",toAdd = false)
    private BoolSetting spawner = new BoolSetting();
    @SettingTarget(name = "Сундуки",toAdd = false)
    private BoolSetting chest = new BoolSetting();
    @SettingTarget(name = "Шалкер",toAdd = false)
    private BoolSetting shulker = new BoolSetting();

    @SettingTarget(name = "Дистанция")
    FloatSetting distanciya = new FloatSetting().setMin(0).setMax(100).setVal(25);

    @SettingTarget(name = "Блоки")
    MultiBoolSetting hui = new MultiBoolSetting().addBools(
            Ancient_Debris,DIAMOND_ORE,REDSTONE_ORE,LAPIS_ORE,IRON_ORE,GOLD_ORE,EMERALD_ORE,COAL_ORE,QUARTZ_ORE,spawner,chest,shulker
    );

    @Override
    public void onEnable() {
        Search();

    }

    @Override
    public boolean onPacket(Object packet, ConnectionUtil.Side side) {

        if (packet instanceof SChunkDataPacket || packet instanceof CPlayerDiggingPacket || packet instanceof CPlayerTryUseItemOnBlockPacket) {
            Search();
        }
        return super.onPacket(packet, side);
    }

    @SubscribeEvent
    public void on3D(RenderWorldLastEvent e) {
        try {
            for (Blockinfo bi : render) {
                RenderUtil.renderBox(e.getMatrixStack(), bi.getX()-1, bi.getY(), bi.getZ(), bi.getColor());
            }
        } catch (Throwable ignored) {
        }
    }

    public void Search() {
        search = new Thread(() ->
        {
            for (double chunkX = mc.player.getX() - distanciya.getVal(); chunkX <= mc.player.getX() + distanciya.getVal(); chunkX++) {
                for (double chunkY = 0; chunkY <= 256; chunkY++) {
                    for (double chunkZ = mc.player.getZ() - distanciya.getVal(); chunkZ <= mc.player.getZ() + distanciya.getVal(); chunkZ++) {
                        BlockPos pos2 = new BlockPos(new Vector3i(chunkX, chunkY, chunkZ));
                        BlockState currentState = mc.level.getBlockState(pos2);

                        if (currentState.getBlock().equals(Blocks.DIAMOND_ORE)) {
                            if (!DIAMOND_ORE.getState()) continue;
                            render.add(new Blockinfo(chunkX, chunkY, chunkZ, new Color(0x00C4FF).hashCode(), currentState.getBlock().getName().getString()));
                        }
                        if (currentState.getBlock().equals(Blocks.REDSTONE_ORE)) {
                            if (!REDSTONE_ORE.getState()) continue;
                            render.add(new Blockinfo(chunkX, chunkY, chunkZ, new Color(0xFF0000).hashCode(), currentState.getBlock().getName().getString()));
                        }
                        if (currentState.getBlock().equals(Blocks.LAPIS_ORE)) {
                            if (!LAPIS_ORE.getState()) continue;
                            render.add(new Blockinfo(chunkX, chunkY, chunkZ, new Color(0x2909FC).hashCode(), currentState.getBlock().getName().getString()));
                        }
                        if (currentState.getBlock().equals(Blocks.IRON_ORE)) {
                            if (!IRON_ORE.getState()) continue;
                            render.add(new Blockinfo(chunkX, chunkY, chunkZ, new Color(0xC5C3C3).hashCode(), currentState.getBlock().getName().getString()));
                        }
                        if (currentState.getBlock().equals(Blocks.GOLD_ORE)) {
                            if (!GOLD_ORE.getState()) continue;
                            render.add(new Blockinfo(chunkX, chunkY, chunkZ, new Color(0xFDFD00).hashCode(), currentState.getBlock().getName().getString()));
                        }
                        if (currentState.getBlock().equals(Blocks.EMERALD_ORE)) {
                            if (!EMERALD_ORE.getState()) continue;
                            render.add(new Blockinfo(chunkX, chunkY, chunkZ, new Color(0x08EF14).hashCode(), currentState.getBlock().getName().getString()));
                        }
                        if (currentState.getBlock().equals(Blocks.COAL_ORE)) {
                            if (!COAL_ORE.getState()) continue;
                            render.add(new Blockinfo(chunkX, chunkY, chunkZ, new Color(0xDB070000, true).hashCode(), currentState.getBlock().getName().getString()));
                        }
                        if (currentState.getBlock().equals(Blocks.ANCIENT_DEBRIS)) {
                            if (!Ancient_Debris.getState()) continue;
                            render.add(new Blockinfo(chunkX, chunkY, chunkZ, new Color(0xFF413232, true).hashCode(), currentState.getBlock().getName().getString()));
                        }
                        if (currentState.getBlock().equals(Blocks.NETHER_QUARTZ_ORE)) {
                            if (!QUARTZ_ORE.getState()) continue;
                            render.add(new Blockinfo(chunkX, chunkY, chunkZ, new Color(0xFFFFFFFF, true).hashCode(), currentState.getBlock().getName().getString()));
                        }
                        if (currentState.getBlock().getDescriptionId().contains("shulker")) {
                            if (!shulker.getState()) continue;
                            render.add(new Blockinfo(chunkX, chunkY, chunkZ, new Color(0xFF8600FF, true).hashCode(), currentState.getBlock().getName().getString()));
                        }
                        if (currentState.getBlock() .getDescriptionId().contains("chest")) {
                            if (!chest.getState()) continue;
                            render.add(new Blockinfo(chunkX, chunkY, chunkZ, new Color(0xFFFF6200, true).hashCode(), currentState.getBlock().getName().getString()));
                        }
                        if (currentState.getBlock() == Blocks.SPAWNER) {
                            if (!spawner.getState()) continue;
                            render.add(new Blockinfo(chunkX, chunkY, chunkZ, new Color(0xFF527370, true).hashCode(), currentState.getBlock().getName().getString()));
                        }
                    }
                }
            }
        });
        search.start();

    }

    @Override
    public void onDisable() {
        search.stop();
        render = new ArrayList<>();
    }
}
