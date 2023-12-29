package com.denger.client.modules.mods.render;

import com.denger.client.another.settings.SettingTarget;
import com.denger.client.another.settings.sett.ColorSetting;
import com.denger.client.another.settings.sett.ModSetting;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import com.denger.client.utils.ColorUtil;
import com.denger.client.utils.rect.RenderUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.awt.*;

import static com.denger.client.MainNative.getInstance;
import static com.denger.client.MainNative.mc;

@ModuleTarget(ModName = "Ibu", category = Category.RENDER)
public class Hat extends Module {
    @SettingTarget(name = "Шляпа")
    ModSetting type = new ModSetting().setMods("Китайская", "Нинфа","Цилиндр").setCurent("Китайская");
    @SettingTarget(name = "Мод")
    ModSetting mods = new ModSetting().setMods("Переливание", "Статический", "Кастомный").setCurent("Статический");
    @SettingTarget(name = "Цвет шляпы")
    ColorSetting colorSetting = (ColorSetting) new ColorSetting().setColor(new Color(-1)).setVisible(() -> mods.getCurent().equals("Кастомный"));
    @SettingTarget(name = "Цвет окантовки")
    ColorSetting colorSetting2 = (ColorSetting) new ColorSetting().setColor(new Color(-1)).setVisible(() -> mods.getCurent().equals("Кастомный") && type.getCurent().equals("Китайская"));

    @SubscribeEvent
    public void onWorldRender(RenderWorldLastEvent e) {
        assert (mc.level != null);
        ClientPlayerEntity t = mc.player;
        if (t == null || mc.options.getCameraType().isFirstPerson()) {
            return;
        }
        MatrixStack ms = e.getMatrixStack();
        float pt = e.getPartialTicks();
        double x = t.xOld + (t.position().x - t.xOld) * (double) pt - mc.getEntityRenderDispatcher().camera.getPosition().x;
        double y = t.yOld + (t.position().y - t.yOld) * (double) pt - mc.getEntityRenderDispatcher().camera.getPosition().y;
        double z = t.zOld + (t.position().z - t.zOld) * (double) pt - mc.getEntityRenderDispatcher().camera.getPosition().z;
        ms.pushPose();
        ms.translate(x, y, z);
        RenderUtil.setupRender();
        int c = 0;
        int c2 = 0;
        switch (mods.getCurent()) {
            case "Переливание":
                c = getInstance.theme.getColor();
                c2 = getInstance.theme.getColor2();
                break;
            case "Кастомный":
                c = colorSetting.getColor().getRGB();
                c2 = colorSetting2.getColor().getRGB();
                break;
            case "Статический":
                c = getInstance.theme.getC().getRGB();
                c2 = getInstance.theme.getC2().getRGB();
                break;
        }
        float[] colors = ColorUtil.rgb(c);
        float[] colors2 = ColorUtil.rgb(c2);
        switch (type.getCurent()){
            case "Китайская":
                chinahat(colors, colors2, ms);
                break;
            case "Нинфа":
                ninfa(colors, ms);
                break;
            case "Цилиндр":
                cilindr(colors, ms);
                break;
        }

        RenderUtil.endRender();
        ms.popPose();
    }

    private void chinahat(float[] colors, float[] colors2, MatrixStack ms) {
        assert mc.player != null;
        float height = mc.player.getBbHeight() + 0.1f;

        bufferbuilder.begin(5, DefaultVertexFormats.POSITION_COLOR);
        for (int i = 0; i <= 360; i++) {
            double cosValue = Math.cos(Math.toRadians(i)) * 0.5;
            double sinValue = Math.sin(Math.toRadians(i)) * 0.5;

            bufferbuilder.vertex(ms.last().pose(), 0, height + 0.2f, 0).color(colors[0], colors[1], colors[2], 0.5F).endVertex();
            bufferbuilder.vertex(ms.last().pose(), (float) cosValue, height, (float) sinValue).color(colors[0], colors[1], colors[2], 0.5F).endVertex();
        }
        tessellator.end();

        bufferbuilder.begin(2, DefaultVertexFormats.POSITION_COLOR);
        for (int i = 0; i <= 360; i++) {
            double cosValue = Math.cos(Math.toRadians(i)) * 0.5;
            double sinValue = Math.sin(Math.toRadians(i)) * 0.5;

            bufferbuilder.vertex(ms.last().pose(), (float) cosValue, height, (float) sinValue).color(colors2[0], colors2[1], colors2[2], 0.5F).endVertex();
        }
        RenderSystem.lineWidth(3);
        tessellator.end();
    }
    private void ninfa(float[] colors, MatrixStack ms) {
        assert mc.player != null;
        float height = mc.player.getBbHeight();

        bufferbuilder.begin(8, DefaultVertexFormats.POSITION_COLOR);
        for (int i = 0; i <= 360; i++) {
            double cosValue = Math.cos(Math.toRadians(i)) * 0.5;
            double sinValue = Math.sin(Math.toRadians(i)) * 0.5;
            bufferbuilder.vertex(ms.last().pose(), (float) (cosValue * 0.75), height + 0.22f, (float) (sinValue * 0.75)).color(colors[0], colors[1], colors[2], 0.5F).endVertex();
            bufferbuilder.vertex(ms.last().pose(), (float) (cosValue * 0.75), height + 0.21f, (float) (sinValue * 0.75)).color(colors[0], colors[1], colors[2], 1).endVertex();
            bufferbuilder.vertex(ms.last().pose(), (float) (cosValue * 0.75), height + 0.2f, (float) (sinValue * 0.75)).color(colors[0], colors[1], colors[2], 0.5F).endVertex();
        }
        tessellator.end();

    }
    private void cilindr(float[] colors, MatrixStack ms){
        assert mc.player != null;
        float height = mc.player.getBbHeight()-0.085f;
        float[] garay = ColorUtil.rgb(new Color(0x1E1C1C).getRGB());
        float[] black = ColorUtil.rgb(Color.BLACK.getRGB());
        RenderSystem.lineWidth(4);
           RenderSystem.depthMask(true);


        bufferbuilder.begin(2, DefaultVertexFormats.POSITION_COLOR);
        for (int i = 0; i <= 360; i++) {
            double cosValue = Math.cos(Math.toRadians(i)) * 0.5;
            double sinValue = Math.sin(Math.toRadians(i)) * 0.5;
            bufferbuilder.vertex(ms.last().pose(), (float) (cosValue * 0.75), height + 0.22f, (float) (sinValue * 0.75)).color(black[0], black[1], black[2], 1).endVertex();
        }
        tessellator.end();
        bufferbuilder.begin(2, DefaultVertexFormats.POSITION_COLOR);
        for (int i = 0; i <= 360; i++) {
            double cosValue = Math.cos(Math.toRadians(i)) * 0.5;
            double sinValue = Math.sin(Math.toRadians(i)) * 0.5;
            bufferbuilder.vertex(ms.last().pose(), (float) (cosValue * 0.76), height + 0.205f, (float) (sinValue * 0.76)).color(black[0], black[1], black[2], 1).endVertex();
        }
        tessellator.end();
        RenderSystem.lineWidth(2);
        bufferbuilder.begin(2, DefaultVertexFormats.POSITION_COLOR);
        for (int i = 0; i <= 360; i++) {
            double cosValue = Math.cos(Math.toRadians(i)) * 0.5;
            double sinValue = Math.sin(Math.toRadians(i)) * 0.5;
            bufferbuilder.vertex(ms.last().pose(), (float) (cosValue * 0.4), height +  0.6f, (float) (sinValue * 0.4)).color(black[0], black[1], black[2], 1).endVertex();
        }
        tessellator.end();
        RenderSystem.lineWidth(4);
        bufferbuilder.begin(6, DefaultVertexFormats.POSITION_COLOR);
        for (int i = 0; i <= 360; i++) {
            double cosValue = Math.cos(Math.toRadians(i)) * 0.5;
            double sinValue = Math.sin(Math.toRadians(i)) * 0.5;
            bufferbuilder.vertex(ms.last().pose(), (float) (cosValue * 0.4), height + 0.6f, (float) (sinValue * 0.4)).color(garay[0], garay[1], garay[2], 1).endVertex();
            bufferbuilder.vertex(ms.last().pose(), (float) (cosValue * 0.4), height + 0.58f, (float) (sinValue * 0.4)).color(garay[0], garay[1], garay[2], 1).endVertex();
        }
        tessellator.end();
        bufferbuilder.begin(6, DefaultVertexFormats.POSITION_COLOR);
        for (int i = 0; i <= 360; i++) {
            double cosValue = Math.cos(Math.toRadians(i)) * 0.5;
            double sinValue = Math.sin(Math.toRadians(i)) * 0.5;
            bufferbuilder.vertex(ms.last().pose(), (float) (cosValue * 0.75), height + 0.22f, (float) (sinValue * 0.75)).color(garay[0], garay[1], garay[2], 1).endVertex();
            bufferbuilder.vertex(ms.last().pose(), (float) (cosValue * 0.75), height + 0.2f, (float) (sinValue * 0.75)).color(garay[0], garay[1], garay[2], 1).endVertex();
        }
        tessellator.end();
        bufferbuilder.begin(8, DefaultVertexFormats.POSITION_COLOR);
        for (int i = 0; i <= 360; i++) {
            double cosValue = Math.cos(Math.toRadians(i)) * 0.5;
            double sinValue = Math.sin(Math.toRadians(i)) * 0.5;
            bufferbuilder.vertex(ms.last().pose(), (float) (cosValue * 0.75), height + 0.22f, (float) (sinValue * 0.75)).color(black[0], black[1], black[2], 1).endVertex();
            bufferbuilder.vertex(ms.last().pose(), (float) (cosValue * 0.75), height + 0.2f, (float) (sinValue * 0.75)).color(black[0], black[1], black[2], 1).endVertex();
        }
        tessellator.end();
        bufferbuilder.begin(8, DefaultVertexFormats.POSITION_COLOR);
        for (int i = 0; i <= 360; i++) {
            double cosValue = Math.cos(Math.toRadians(i)) * 0.5;
            double sinValue = Math.sin(Math.toRadians(i)) * 0.5;
            bufferbuilder.vertex(ms.last().pose(), (float) (cosValue * 0.41), height + 0.25f, (float) (sinValue * 0.41)).color(colors[0], colors[1], colors[2], 1).endVertex();
            bufferbuilder.vertex(ms.last().pose(), (float) (cosValue * 0.41), height + 0.22f, (float) (sinValue * 0.41)).color(colors[0], colors[1], colors[2], 1).endVertex();
        }
        tessellator.end();
        bufferbuilder.begin(8, DefaultVertexFormats.POSITION_COLOR);
        for (int i = 0; i <= 360; i++) {
            double cosValue = Math.cos(Math.toRadians(i)) * 0.5;
            double sinValue = Math.sin(Math.toRadians(i)) * 0.5;
            bufferbuilder.vertex(ms.last().pose(), (float) (cosValue * 0.4), height + 0.6f, (float) (sinValue * 0.4)).color(garay[0], garay[1], garay[2], 1).endVertex();
            bufferbuilder.vertex(ms.last().pose(), (float) (cosValue * 0.4), height + 0.2f, (float) (sinValue * 0.4)).color(garay[0], garay[1], garay[2], 1).endVertex();
        }
        tessellator.end();
 
    }


}
