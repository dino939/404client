package com.denger.client.modules.mods.render;

import com.denger.client.another.settings.SettingTarget;
import com.denger.client.another.settings.sett.BoolSetting;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import com.denger.client.utils.ColorUtil;
import com.denger.client.utils.ESPUtil;
import com.denger.client.utils.rect.RenderUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.IRenderCall;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.denger.client.Main.getInstance;
import static com.denger.client.Main.mc;

@ModuleTarget(ModName = "Usbdfst", category = Category.RENDER)
public class Tracers extends Module {
    @SettingTarget(name = "Животные")
    BoolSetting Animals = new BoolSetting();


    @SubscribeEvent
    public void on2D(RenderGameOverlayEvent.Pre e) {
        if (e.getType() != RenderGameOverlayEvent.ElementType.ALL)return;
        assert (mc.level != null);
        mc.options.bobView = false;
        boolean animals = Animals.getState();

        IRenderCall a = () -> {
            mc.level.entitiesForRendering().forEach((en) -> {
                Vector3d vec = ESPUtil.toScreen(en.position());
                int color = getInstance.theme.getColor();
                if (en instanceof MobEntity && animals) {
                    RenderUtil.drawLine((double) mc.getWindow().getGuiScaledWidth() / 2, (double) mc.getWindow().getGuiScaledHeight() / 2, vec.x, vec.y, 1, color);
                }
                if (en instanceof PlayerEntity) {
                    if (en == mc.player) return;
                    RenderUtil.drawLine((double) mc.getWindow().getGuiScaledWidth() / 2, (double) mc.getWindow().getGuiScaledHeight() / 2, vec.x, vec.y, 1, color);

                }


            });
        };

        a.execute();


    }

    public void renderTracer(Entity t, MatrixStack ms, float pt, int color) {
        double x = t.xOld + (t.position().x - t.xOld) * (double) pt - mc.getEntityRenderDispatcher().camera.getPosition().x;
        double y = t.yOld + (t.position().y - t.yOld) * (double) pt - mc.getEntityRenderDispatcher().camera.getPosition().y;
        double z = t.zOld + (t.position().z - t.zOld) * (double) pt - mc.getEntityRenderDispatcher().camera.getPosition().z;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuilder();
        RenderSystem.disableDepthTest();
        RenderUtil.glColor(-1);
        RenderUtil.setupRender();
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        RenderSystem.lineWidth((float) 0.1f);
        Vector3d vec = new Vector3d(0.0, 0.0, mc.options.getCameraType().isFirstPerson() || mc.options.getCameraType().cycle().isMirrored() ? 1.0 : -1.0);
        assert mc.player != null;
        vec = vec.xRot(-((float) Math.toRadians(mc.player.xRot)));
        Vector3d vec2 = vec.yRot(-((float) Math.toRadians(mc.player.yRot)));
        ms.pushPose();
        bufferbuilder.vertex(ms.last().pose(), (float) vec2.x(), (float) vec2.y(), (float) vec2.z()).color(ColorUtil.r(color), ColorUtil.g(color), ColorUtil.b(color), ColorUtil.a(color)).endVertex();
        ms.popPose();
        ms.pushPose();
        ms.translate(x, y, z);
        bufferbuilder.vertex(ms.last().pose(), 0.0f, 0.0f, 0.0f).color(ColorUtil.r(color), ColorUtil.g(color), ColorUtil.b(color), ColorUtil.a(color)).endVertex();
        ms.popPose();
        tessellator.end();
        RenderSystem.enableDepthTest();
        RenderUtil.endRender();
    }
}
