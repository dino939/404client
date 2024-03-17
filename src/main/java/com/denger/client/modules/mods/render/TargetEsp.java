package com.denger.client.modules.mods.render;

import com.denger.client.another.resource.ImageManager;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import com.denger.client.utils.ColorUtil;
import com.denger.client.utils.MathUtils;
import com.denger.client.utils.TimerUtil;
import com.denger.client.utils.Utils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL46;

import java.util.ArrayList;

import static com.denger.client.Main.mc;
import static com.denger.client.utils.rect.RenderUtil.resetColor;
import static com.mojang.blaze3d.platform.GlStateManager._bindTexture;
import static com.mojang.blaze3d.platform.GlStateManager._disableBlend;

@ModuleTarget(ModName = "UbshfuFtq", category = Category.RENDER)
public class TargetEsp extends Module {
    private Entity target;
    private Entity actualTarget;
    ArrayList<Particl> particls = new ArrayList<>();
    ArrayList<Vector3d> vecs = new ArrayList<>();
    ArrayList<Vector3d> vecs1 = new ArrayList<>();
    ArrayList<Vector3d> vecs2 = new ArrayList<>();


    public TargetEsp() {
        for (int i = 0; i <= 360; ++i) {
            double v = Math.sin(Math.toRadians(i));
            double u = Math.cos(Math.toRadians(i));
            Vector3d vec = new Vector3d((float) (u * 0.5f), 0, (float) (v * 0.5f));
            vecs.add(vec);

            double v1 = Math.sin(Math.toRadians((i + 120) % 360));
            double u1 = Math.cos(Math.toRadians(i + 120) % 360);
            Vector3d vec1 = new Vector3d((float) (u1 * 0.5f), 0, (float) (v1 * 0.5f));
            vecs1.add(vec1);

            double v2 = Math.sin(Math.toRadians((i + 240) % 360));
            double u2 = Math.cos(Math.toRadians((i + 240) % 360));
            Vector3d vec2 = new Vector3d((float) (u2 * 0.5f), 0, (float) (v2 * 0.5f));
            vecs2.add(vec2);
        }
    }

    @Override
    public void onEnable() {
        reset();
    }

    @SubscribeEvent
    public void onAttack(AttackEntityEvent event) {
        if (event.getEntity() == mc.player) {
            target = event.getTarget();
        }

    }

    private void reset() {
        particls.clear();
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.RenderTickEvent e) {
        if (target != null) {
            if (!target.isAlive() || target.distanceTo(mc.player) > 7) {
                target = null;
            }
        }
        actualTarget = mc.player;
        if (actualTarget == null) {
            reset();
            return;
        }
        particls.removeIf(particl -> {
            return particl.timer.hasReached(particl.time);
        });

        Vector3d NEW = actualTarget.getPosition(e.renderTickTime);
        //  if (particls.size() > 9) return;

        int b = (int) MathUtils.getTimeSpeedValue(100, 360);
        float a = MathUtils.calculateValue(MathUtils.calcPercentage(b, 0, 360), 0, actualTarget.getBbHeight());
        particls.add(new Particl(vecs.get(b).add(NEW).add(0, a, 0), 250, ColorUtil.getRainbow()));
        particls.add(new Particl(vecs1.get(b).add(NEW).add(0, a, 0), 250, ColorUtil.getRainbow()));
        particls.add(new Particl(vecs2.get(b).add(NEW).add(0, a, 0), 250, ColorUtil.getRainbow()));
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent renderWorldLastEvent) {
        MatrixStack ms = Utils.get3DMatrix();


        if (actualTarget != null) {
            drawTargetEsp(ms, actualTarget, renderWorldLastEvent.getPartialTicks());
        }

    }

    public void drawTargetEsp(MatrixStack ms, Entity target, float pt) {
        float startX = -2f;
        float startY = -2f;
        float width = 4;
        float height = 4;

        float endX = startX + width;
        float endY = startY + height;
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        particls.forEach(particl -> {

            {
                if (particls.indexOf(particl) > this.particls.size() - 2) return;
                // particl.mov(target.getPosition(pt));
                int a = (int) ((float) this.particls.indexOf(particl) / (float) this.particls.size() * 255);
                int color2 = ColorUtil.swapAlpha(particl.color, MathUtils.clamp(a, 0, 20));
                float proccess = (this.particls.indexOf(particl) / (float) this.particls.size());
                double x = particl.getX() - mc.getEntityRenderDispatcher().camera.getPosition().x;
                double y = particl.getY() - mc.getEntityRenderDispatcher().camera.getPosition().y;
                double z = particl.getZ() - mc.getEntityRenderDispatcher().camera.getPosition().z;
                float scale = -MathUtils.calculateValue(proccess * 100, 0.025f, 0.10f);

                ms.pushPose();
                ms.translate(x, y, z);

                final ActiveRenderInfo renderInfo = mc.gameRenderer.getMainCamera();
                ms.mulPose(renderInfo.rotation().copy());

                ms.scale(scale, scale, scale);

                resetColor();
                mc.getTextureManager().bind(ImageManager.getResource("bloom.png"));
                Matrix4f matrix = ms.last().pose();
                bufferbuilder.begin(GL20.GL_QUADS, DefaultVertexFormats.POSITION_COLOR_TEX);
                ms.pushPose();

                bufferbuilder.vertex(matrix, startX - 0.2f, startY - 0.2f, 0).color(ColorUtil.r(color2), ColorUtil.g(color2), ColorUtil.b(color2), ColorUtil.a(color2)).uv(0.0F, 0.0F).endVertex();
                bufferbuilder.vertex(matrix, startX - 0.2f, endY + 0.2f, 0).color(ColorUtil.r(color2), ColorUtil.g(color2), ColorUtil.b(color2), ColorUtil.a(color2)).uv(0.0F, 1.0F).endVertex();
                bufferbuilder.vertex(matrix, endX + 0.2f, endY + 0.2f, 0).color(ColorUtil.r(color2), ColorUtil.g(color2), ColorUtil.b(color2), ColorUtil.a(color2)).uv(1.0F, 1.0F).endVertex();
                bufferbuilder.vertex(matrix, endX + 0.2f, startY - 0.2f, 0).color(ColorUtil.r(color2), ColorUtil.g(color2), ColorUtil.b(color2), ColorUtil.a(color2)).uv(1.0F, 0.0F).endVertex();
                GL46.glDepthMask(false);
                GL46.glDisable(2884);
                GL46.glEnable(3042);
                GL46.glDisable(3008);
                GL46.glEnable(GL46.GL_BLEND);
                GL46.glBlendFunc(GL46.GL_SRC_ALPHA, GL46.GL_ONE);

                tessellator.end();

                GL46.glBlendFunc(GL46.GL_SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.value);
                GL46.glEnable(3008);
                GL46.glDisable(3042);
                GL46.glEnable(2884);
                GL46.glDepthMask(true);
                ms.popPose();
                _bindTexture(0);
                _disableBlend();
                ms.popPose();


            }
        });

        RenderSystem.lineWidth(1);
    }

    class Particl {
        public Vector3d pos;
        public long time;
        public TimerUtil timer;
        public int color;


        public Particl(Vector3d pos, long time, int color) {
            this.time = time;
            this.color = color;
            this.timer = new TimerUtil();
            this.pos = pos;
        }

        public void mov(Vector3d vec) {
            pos = pos.add(vec);
        }

        public int getColor(int a) {
            return ColorUtil.swapAlpha(-1, a);
        }

        public float getX() {
            return (float) pos.x;
        }

        public float getY() {
            return (float) pos.y;
        }

        public float getZ() {
            return (float) pos.z;
        }
    }


}
