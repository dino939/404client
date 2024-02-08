package com.denger.client.modules.mods.render;

import com.denger.client.another.settings.SettingTarget;
import com.denger.client.another.settings.sett.BoolSetting;
import com.denger.client.another.settings.sett.FloatSetting;
import com.denger.client.another.settings.sett.MultiBoolSetting;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import com.denger.client.utils.BlockInfoUtil;
import com.denger.client.utils.ColorUtil;
import com.denger.client.utils.rect.RenderUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.network.play.client.CPlayerDiggingPacket;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;

import static com.denger.client.Main.mc;

@ModuleTarget(ModName = "YSbzCzqbtt", category = Category.RENDER)

public class XRayBypass extends Module {
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
    @SettingTarget(name = "Сундуки",toAdd = false)
    private BoolSetting chest = new BoolSetting();
    @SettingTarget(name = "Шалкер",toAdd = false)
    private BoolSetting shulker = new BoolSetting();
    @SettingTarget(name = "Спавнер",toAdd = false)
    private BoolSetting spawner = new BoolSetting();
    ArrayList<BlockInfoUtil> blockInfoUtils = new ArrayList<>(), blockInfoUtilsProven = new ArrayList<>();
    @SettingTarget(name = "Дистанция")
    FloatSetting distanciya = new FloatSetting().setMin(0).setMax(100).setVal(25);
    @SettingTarget(name = "Задержка")
    FloatSetting zaderjca = new FloatSetting().setMin(0).setMax(50).setVal(20);
    @SettingTarget(name = "Блоки")
    MultiBoolSetting hui = new MultiBoolSetting().addBools(
            Ancient_Debris,DIAMOND_ORE,REDSTONE_ORE,LAPIS_ORE,IRON_ORE,GOLD_ORE,EMERALD_ORE,COAL_ORE,QUARTZ_ORE,spawner,chest,shulker
    );
    @Override
    public void onEnable() {
        for (double chunkX = mc.player.getX() - distanciya.getVal(); chunkX <= mc.player.getX() + distanciya.getVal(); chunkX++) {
            for (double chunkY = 0; chunkY <= 256; chunkY++) {
                for (double chunkZ = mc.player.getZ() - distanciya.getVal(); chunkZ <= mc.player.getZ() + distanciya.getVal(); chunkZ++) {
                    BlockPos blockPos = new BlockPos(chunkX, chunkY, chunkZ);
                    addOre(blockPos, Blocks.COAL_ORE, COAL_ORE.getState(), new Color(0, 0, 0));
                    addOre(blockPos, Blocks.LAPIS_ORE, LAPIS_ORE.getState(), new Color(0, 0, 165));
                    addOre(blockPos, Blocks.REDSTONE_ORE, REDSTONE_ORE.getState(), new Color(165, 0, 0));
                    addOre(blockPos, Blocks.NETHER_QUARTZ_ORE, QUARTZ_ORE.getState(), new Color(255, 255, 255));
                    addOre(blockPos, Blocks.IRON_ORE, IRON_ORE.getState(), new Color(165, 165, 165));
                    addOre(blockPos, Blocks.GOLD_ORE, GOLD_ORE.getState(), new Color(255, 255, 0));
                    addOre(blockPos, Blocks.DIAMOND_ORE, DIAMOND_ORE.getState(), new Color(0, 0, 255));
                    addOre(blockPos, Blocks.EMERALD_ORE, EMERALD_ORE.getState(), new Color(0, 255, 0));
                    addOre(blockPos, Blocks.ANCIENT_DEBRIS, Ancient_Debris.getState(), new Color(255, 0, 0));
                    addOre(blockPos, Blocks.SPAWNER, spawner.getState(), new Color(0xFF8600FF));
                    addOre(blockPos, Blocks.CHEST, chest.getState(), new Color(0xFFFF6200));
                    addOre(blockPos, Blocks.SHULKER_BOX, shulker.getState(), new Color(0xFF527370));
                }
            }
        }

        new Thread(() -> {
            for (BlockInfoUtil blockInfoUtil : blockInfoUtils) {
                mc.player.connection.send(new CPlayerDiggingPacket(CPlayerDiggingPacket.Action.START_DESTROY_BLOCK, blockInfoUtil.getPos(), Direction.UP));
                if (mc.level.getBlockState(blockInfoUtil.getPos()).getBlock().equals(blockInfoUtil.getBlock())) {
                    blockInfoUtilsProven.add(blockInfoUtil);
                }
                try {
                    Thread.sleep((long) zaderjca.getVal());
                } catch (Exception ignored) {
                }
            }

        }).start();

    }

    @SubscribeEvent
    public void onRender3D(RenderWorldLastEvent event) {
        try {
            blockInfoUtilsProven.forEach((blockInfoUtil) -> {
                if (mc.level.getBlockState(blockInfoUtil.getPos()).getBlock().equals(blockInfoUtil.getBlock())) {
                    renderBox(event.getMatrixStack(), blockInfoUtil.getPos(), blockInfoUtil.getColor().getRGB());
                }
            });

        } catch (Exception ignored) {
        }
    }

    public void renderBox(MatrixStack ms, BlockPos pos, int color) {
        double x = pos.getX() - mc.getEntityRenderDispatcher().camera.getPosition().x;
        double y = pos.getY() - mc.getEntityRenderDispatcher().camera.getPosition().y;
        double z = pos.getZ() - mc.getEntityRenderDispatcher().camera.getPosition().z;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuilder();
        ms.pushPose();
        ms.translate(x, y, z);
        RenderSystem.disableDepthTest();
        RenderUtil.setupRender();
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.vertex(ms.last().pose(), 0.0f, 0.0f, 0.0f).color(ColorUtil.r(color), ColorUtil.g(color), ColorUtil.b(color), ColorUtil.a(color)).endVertex();
        bufferbuilder.vertex(ms.last().pose(), 1.0f, 0.0f, 0.0f).color(ColorUtil.r(color), ColorUtil.g(color), ColorUtil.b(color), ColorUtil.a(color)).endVertex();
        bufferbuilder.vertex(ms.last().pose(), 1.0f, 1.0f, 0.0f).color(ColorUtil.r(color), ColorUtil.g(color), ColorUtil.b(color), ColorUtil.a(color)).endVertex();
        bufferbuilder.vertex(ms.last().pose(), 0.0f, 1.0f, 0.0f).color(ColorUtil.r(color), ColorUtil.g(color), ColorUtil.b(color), ColorUtil.a(color)).endVertex();
        bufferbuilder.vertex(ms.last().pose(), 0.0f, 0.0f, 0.0f).color(ColorUtil.r(color), ColorUtil.g(color), ColorUtil.b(color), ColorUtil.a(color)).endVertex();
        tessellator.end();
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.vertex(ms.last().pose(), 0.0f, 0.0f, 1.0f).color(ColorUtil.r(color), ColorUtil.g(color), ColorUtil.b(color), ColorUtil.a(color)).endVertex();
        bufferbuilder.vertex(ms.last().pose(), 1.0f, 0.0f, 1.0f).color(ColorUtil.r(color), ColorUtil.g(color), ColorUtil.b(color), ColorUtil.a(color)).endVertex();
        bufferbuilder.vertex(ms.last().pose(), 1.0f, 1.0f, 1.0f).color(ColorUtil.r(color), ColorUtil.g(color), ColorUtil.b(color), ColorUtil.a(color)).endVertex();
        bufferbuilder.vertex(ms.last().pose(), 0.0f, 1.0f, 1.0f).color(ColorUtil.r(color), ColorUtil.g(color), ColorUtil.b(color), ColorUtil.a(color)).endVertex();
        tessellator.end();
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.vertex(ms.last().pose(), 0.0f, 0.0f, 0.0f).color(ColorUtil.r(color), ColorUtil.g(color), ColorUtil.b(color), ColorUtil.a(color)).endVertex();
        bufferbuilder.vertex(ms.last().pose(), 0.0f, 0.0f, 1.0f).color(ColorUtil.r(color), ColorUtil.g(color), ColorUtil.b(color), ColorUtil.a(color)).endVertex();
        bufferbuilder.vertex(ms.last().pose(), 0.0f, 1.0f, 1.0f).color(ColorUtil.r(color), ColorUtil.g(color), ColorUtil.b(color), ColorUtil.a(color)).endVertex();
        bufferbuilder.vertex(ms.last().pose(), 0.0f, 1.0f, 0.0f).color(ColorUtil.r(color), ColorUtil.g(color), ColorUtil.b(color), ColorUtil.a(color)).endVertex();
        tessellator.end();
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.vertex(ms.last().pose(), 1.0f, 0.0f, 0.0f).color(ColorUtil.r(color), ColorUtil.g(color), ColorUtil.b(color), ColorUtil.a(color)).endVertex();
        bufferbuilder.vertex(ms.last().pose(), 1.0f, 0.0f, 1.0f).color(ColorUtil.r(color), ColorUtil.g(color), ColorUtil.b(color), ColorUtil.a(color)).endVertex();
        bufferbuilder.vertex(ms.last().pose(), 1.0f, 1.0f, 1.0f).color(ColorUtil.r(color), ColorUtil.g(color), ColorUtil.b(color), ColorUtil.a(color)).endVertex();
        bufferbuilder.vertex(ms.last().pose(), 1.0f, 1.0f, 0.0f).color(ColorUtil.r(color), ColorUtil.g(color), ColorUtil.b(color), ColorUtil.a(color)).endVertex();
        bufferbuilder.color(255, 255, 255, 255);
        tessellator.end();
        RenderSystem.enableDepthTest();
        RenderUtil.endRender();
        ms.popPose();
    }

    @Override
    public void onDisable() {
        blockInfoUtils.clear();
        blockInfoUtilsProven.clear();
        super.onDisable();
    }

    public void addOre(BlockPos blockPos, Block block, boolean included, Color color) {
        if (mc.level.getBlockState(blockPos).getBlock().equals(block) && included) {
            blockInfoUtils.add(new BlockInfoUtil(blockPos, block, color));
        }
    }
}
