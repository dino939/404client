package com.denger.client.modules.mods.render;

import com.denger.client.another.settings.SettingTarget;
import com.denger.client.another.settings.sett.BoolSetting;
import com.denger.client.another.settings.sett.FloatSetting;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import com.denger.client.utils.ColorUtil;
import com.denger.client.utils.TimerUtil;
import com.denger.client.utils.rect.RenderUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;

import static com.denger.client.MainNative.getInstance;
import static com.denger.client.MainNative.mc;
@ModuleTarget(ModName = "Usbjmt",category = Category.RENDER)
public class Trails extends Module {
    ArrayList<Trail> trails = new ArrayList<>();
    @SettingTarget(name = "Длина")
    FloatSetting length = new FloatSetting().setMax(15).setMin(3).setVal(6);
    @SettingTarget(name = "От первого лица?")
    BoolSetting noFirstPerson = new BoolSetting().setBol(true);

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent renderWorldLastEvent) {
        if (mc.options.getCameraType().isFirstPerson() && !noFirstPerson.getState()) {
            return;
        }
        MatrixStack ms = renderWorldLastEvent.getMatrixStack();
        try {
            Vector3d pos = new Vector3d(mc.player.xOld + (mc.player.getX() - mc.player.xOld) * (double) renderWorldLastEvent.getPartialTicks(), mc.player.yOld + (mc.player.getY() - mc.player.yOld) * (double) renderWorldLastEvent.getPartialTicks(), mc.player.zOld + (mc.player.getZ() - mc.player.zOld) * (double) renderWorldLastEvent.getPartialTicks());
            long length = (long) (this.length.getVal() * 100);
            this.trails.removeIf(t -> t.timer.hasReached((int) t.time));
            if (mc.player.xOld != mc.player.getX() || mc.player.yOld != mc.player.getY() || mc.player.zOld != mc.player.getZ()) {
                this.trails.add(new Trail(pos, length,getInstance.theme.getClientColorsSpeed(0.0f, 10)[0]));
            }
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuilder();
            int begin = 8;
            RenderUtil.setupRender();
            bufferbuilder.begin(begin, DefaultVertexFormats.POSITION_COLOR);
            for (Trail trail : this.trails) {
                if (this.trails.indexOf(trail) > this.trails.size() - 2) continue;
                int color = trail.getColor((int) ((float) this.trails.indexOf(trail) / (float) this.trails.size() * 255));
                double x = trail.pos.x - mc.getEntityRenderDispatcher().camera.getPosition().x;
                double y = trail.pos.y - mc.getEntityRenderDispatcher().camera.getPosition().y;
                double z = trail.pos.z - mc.getEntityRenderDispatcher().camera.getPosition().z;
                ms.pushPose();
                ms.translate(x, y, z);
                bufferbuilder.vertex(ms.last().pose(), 0, mc.player.getBbHeight(), 0).color(ColorUtil.r(color), ColorUtil.g(color), ColorUtil.b(color), ColorUtil.a(color)).endVertex();
                bufferbuilder.vertex(ms.last().pose(), 0, 0, 0).color(ColorUtil.r(color), ColorUtil.g(color), ColorUtil.b(color), ColorUtil.a(color)).endVertex();
                ms.popPose();
            }
            tessellator.end();
            RenderSystem.lineWidth(1);
            RenderUtil.endRender();
        } catch (Exception ignored) {
        }
    }

    public class Trail {
        public Vector3d pos;
        public long time;
        public TimerUtil timer;
        public int color;

        public Trail(Vector3d pos, long time, int color) {
            this.pos = pos;
            this.time = time;
            this.color = color;
            this.timer = new TimerUtil();
        }

        public int getColor(int a) {
            return ColorUtil.swapAlpha(this.color, a);
        }
    }
}
