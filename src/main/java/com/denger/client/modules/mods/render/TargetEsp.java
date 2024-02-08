package com.denger.client.modules.mods.render;

import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import com.denger.client.modules.mods.combat.Aura;
import com.denger.client.utils.ColorUtil;
import com.denger.client.utils.Utils;
import com.denger.client.utils.rect.RenderUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.opengl.GL46;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;

import static com.denger.client.Main.getInstance;
import static com.denger.client.Main.mc;

@ModuleTarget(ModName = "UbshfuFtq", category = Category.RENDER)
public class TargetEsp extends Module {
    public static long initTime = System.currentTimeMillis();
    private Entity target;

    @SubscribeEvent
    public void onRender3D(RenderWorldLastEvent event) {

        if (target != null) {
            if (!target.isAlive() || target.distanceTo(mc.player) > 7) {
                target = null;
            }
        }
        Entity entity = getInstance.getRegisterModule().isEnable(Aura.class) ? Aura.target : target;
        if (entity != null) {
            drawTargetEsp(Utils.get3DMatrix(), entity);
        }
    }

    @SubscribeEvent
    public void onAttack(AttackEntityEvent event) {
        if (event.getEntity() == mc.player) {
            target = event.getTarget();
        }

    }

    public static void drawTargetEsp(MatrixStack stack, @Nullable Entity target) {
        ArrayList<Vector3d> vecs = new ArrayList<>();
        ArrayList<Vector3d> vecs1 = new ArrayList<>();
        ArrayList<Vector3d> vecs2 = new ArrayList<>();
        float pt = Utils.getPartialTick();
        double x = target.xOld + (target.getX() - target.xOld) * pt - mc.getEntityRenderDispatcher().camera.getPosition().x();
        double y = target.yOld + (target.getY() - target.yOld) * pt - mc.getEntityRenderDispatcher().camera.getPosition().y();
        double z = target.zOld + (target.getZ() - target.zOld) * pt - mc.getEntityRenderDispatcher().camera.getPosition().z();

        int mode = 8;
        double height = target.getBbHeight();
        Matrix4f matrix = stack.last().pose();

        for (int i = 0; i <= 360; ++i) {
            double v = Math.sin(Math.toRadians(i));
            double u = Math.cos(Math.toRadians(i));
            Vector3d vec = new Vector3d((float) (u * 0.5f), height, (float) (v * 0.5f));
            vecs.add(vec);

            double v1 = Math.sin(Math.toRadians((i + 120) % 360));
            double u1 = Math.cos(Math.toRadians(i + 120) % 360);
            Vector3d vec1 = new Vector3d((float) (u1 * 0.5f), height, (float) (v1 * 0.5f));
            vecs1.add(vec1);

            double v2 = Math.sin(Math.toRadians((i + 240) % 360));
            double u2 = Math.cos(Math.toRadians((i + 240) % 360));
            Vector3d vec2 = new Vector3d((float) (u2 * 0.5f), height, (float) (v2 * 0.5f));
            vecs2.add(vec2);
            height -= 0.004f;
        }
        stack.translate(x, y, z);

        RenderUtil.setupRender();
        RenderSystem.depthMask( true);
        RenderSystem.disableCull();
        RenderSystem.enableAlphaTest();
        GL46.glEnable(GL46.GL_BLEND);
        GL46.glEnable(3042);
        bufferbuilder.begin(mode, DefaultVertexFormats.POSITION_COLOR);

        for (int j = 0; j < vecs.size() - 1; ++j) {
            float alpha = 1f - (((float) j + ((System.currentTimeMillis() - initTime) / 5f)) % 340) / 60f;
            int color = ColorUtil.swapAlpha(new Color(getInstance.theme.getColor((int) (j / 20f))), (int) (alpha * 255)).getRGB();
            bufferbuilder.vertex(matrix, (float) vecs.get(j).x, (float) vecs.get(j).y, (float) vecs.get(j).z).color(ColorUtil.r(color), ColorUtil.g(color), ColorUtil.b(color), ColorUtil.a(color)).endVertex();
            bufferbuilder.vertex(matrix, (float) vecs.get(j + 1).x, (float) vecs.get(j + 1).y + 0.1f, (float) vecs.get(j + 1).z).color(ColorUtil.r(color), ColorUtil.g(color), ColorUtil.b(color), ColorUtil.a(color)).endVertex();
        }


        tessellator.end();


        bufferbuilder.begin(mode, DefaultVertexFormats.POSITION_COLOR);
        for (int j = 0; j < vecs1.size() - 1; ++j) {
            float alpha = 1f - (((float) j + ((System.currentTimeMillis() - initTime) / 5f)) % 340) / 60f;
            int color = ColorUtil.swapAlpha(new Color(getInstance.theme.getColor((int) (j / 20f))), (int) (alpha * 255)).getRGB();
            bufferbuilder.vertex(matrix, (float) vecs1.get(j).x, (float) vecs1.get(j).y, (float) vecs1.get(j).z).color(ColorUtil.r(color), ColorUtil.g(color), ColorUtil.b(color), ColorUtil.a(color)).endVertex();
            bufferbuilder.vertex(matrix, (float) vecs1.get(j + 1).x, (float) vecs1.get(j + 1).y + 0.1f, (float) vecs1.get(j + 1).z).color(ColorUtil.r(color), ColorUtil.g(color), ColorUtil.b(color), ColorUtil.a(color)).endVertex();
        }
        tessellator.end();


        bufferbuilder.begin(mode, DefaultVertexFormats.POSITION_COLOR);
        for (int j = 0; j < vecs2.size() - 1; ++j) {
            float alpha = 1f - (((float) j + ((System.currentTimeMillis() - initTime) / 5f)) % 340) / 60f;
            int color = ColorUtil.swapAlpha(new Color(getInstance.theme.getColor((int) (j / 20f))), (int) (alpha * 255)).getRGB();
            bufferbuilder.vertex(matrix, (float) vecs2.get(j).x, (float) vecs2.get(j).y, (float) vecs2.get(j).z).color(ColorUtil.r(color), ColorUtil.g(color), ColorUtil.b(color), ColorUtil.a(color)).endVertex();
            bufferbuilder.vertex(matrix, (float) vecs2.get(j + 1).x, (float) vecs2.get(j + 1).y + 0.1f, (float) vecs2.get(j + 1).z).color(ColorUtil.r(color), ColorUtil.g(color), ColorUtil.b(color), ColorUtil.a(color)).endVertex();
        }

        tessellator.end();
        RenderUtil.endRender();

    }


}
