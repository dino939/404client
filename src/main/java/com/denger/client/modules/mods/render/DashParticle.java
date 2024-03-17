package com.denger.client.modules.mods.render;

import com.denger.client.another.resource.Gif;
import com.denger.client.another.resource.GifManager;
import com.denger.client.another.settings.SettingTarget;
import com.denger.client.another.settings.sett.BoolSetting;
import com.denger.client.another.settings.sett.FloatSetting;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import com.denger.client.utils.*;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL46;

import java.util.ArrayList;

import static com.denger.client.Main.getInstance;
import static com.denger.client.Main.mc;
import static com.denger.client.utils.rect.RenderUtil.resetColor;
import static com.mojang.blaze3d.platform.GlStateManager._bindTexture;
import static com.mojang.blaze3d.platform.GlStateManager._disableBlend;

@ModuleTarget(ModName = "EbtiQbsujdmf", category = Category.RENDER)
public class DashParticle extends Module {
    @SettingTarget(name = "От первого лица?")
    BoolSetting noFirstPerson = new BoolSetting().setBol(true);
    @SettingTarget(name = "Количество частиц")
    FloatSetting particleCount = new FloatSetting().setMin(1).setMax(20).setVal(3);
    @SettingTarget(name = "Продолжительность")
    FloatSetting duration = new FloatSetting().setMin(100).setMax(1000).setVal(500);
    @SettingTarget(name = "Диапазон")
    FloatSetting range = new FloatSetting().setMin(1).setMax(4).setVal(1);
    ArrayList<Particl> particls = new ArrayList<>();

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent renderWorldLastEvent) {
        if (mc.options.getCameraType().isFirstPerson() && !noFirstPerson.getState()) {
            return;
        }
        MatrixStack ms = Utils.get3DMatrix();
        mc.player.getPosition(1);
        particls.removeIf(particl -> {
            return particl.timer.hasReached(particl.time);
        });
        if (mc.player.xOld != mc.player.getX() ||  mc.player.zOld != mc.player.getZ()) {

            // Utils.getVecEntity(mc.player, renderWorldLastEvent.getPartialTicks(), 0);
            for (int i = 0; i < particleCount.getVal(); i++) {
                particls.add(new Particl(mc.player, new Vector3d(MathUtils.randomFloat(-(range.getVal() / 10), (range.getVal() / 10)), MathUtils.randomFloat(0, 0.4f), MathUtils.randomFloat(-(range.getVal() / 10), (range.getVal() / 10))), (long) duration.getVal(), getInstance.theme.getColors()[MathUtils.randomInt(0, 2)].getRGB(), new Vector2f(mc.player.xRot - MathUtils.randomInt(1, 91), mc.player.yBodyRot + 90), getParticle()));
            }

        }

        float startX = -2f;
        float startY = -2f;
        float width = 4;
        float height = 4;

        float endX = startX + width;
        float endY = startY + height;
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        int colorAWT = getInstance.theme.getC().getRGB();
        particls.forEach(particl -> {

            {
                if (particls.indexOf(particl) > this.particls.size() - 2) return;
                int a = (int) ((float) this.particls.indexOf(particl) / (float) this.particls.size() * 255);
                int color = particl.getColor(a);
                int color2 = ColorUtil.swapAlpha(colorAWT, a);
                float proccess = (this.particls.indexOf(particl) / (float) this.particls.size());
                double x = particl.getX(proccess) - mc.getEntityRenderDispatcher().camera.getPosition().x;
                double y = particl.getY(proccess) - mc.getEntityRenderDispatcher().camera.getPosition().y;
                double z = particl.getZ(proccess) - mc.getEntityRenderDispatcher().camera.getPosition().z;

                ms.pushPose();
                ms.translate(x, y, z);

                boolean flag = mc.options.getCameraType().isFirstPerson() || mc.options.getCameraType().cycle().isMirrored();
                if (flag) {
                    ms.mulPose(Vector3f.YN.rotation((float) Math.toRadians(particl.rot.y)));
                    ms.mulPose(Vector3f.XN.rotation((float) Math.toRadians(-particl.rot.x)));
                } else {
                    ms.mulPose(Vector3f.YN.rotation((float) Math.toRadians(particl.rot.x - 180f)));
                    ms.mulPose(Vector3f.XN.rotation((float) Math.toRadians(particl.rot.y)));
                }

                ms.scale(-0.025f, -0.025f, -0.025f);

                resetColor();
                _bindTexture(Utils.getTextureId(particl.local.getResource((int) ((float) this.particls.indexOf(particl) / (float) this.particls.size() * 99))));
                Matrix4f matrix = ms.last().pose();
                bufferbuilder.begin(GL20.GL_QUADS, DefaultVertexFormats.POSITION_COLOR_TEX);
                ms.pushPose();
                bufferbuilder.vertex(matrix, startX - 0.2f, startY - 0.2f, 0).color(ColorUtil.r(color2), ColorUtil.g(color2), ColorUtil.b(color2), ColorUtil.a(color2)).uv(0.0F, 0.0F).endVertex();
                bufferbuilder.vertex(matrix, startX - 0.2f, endY + 0.2f, 0).color(ColorUtil.r(color2), ColorUtil.g(color2), ColorUtil.b(color2), ColorUtil.a(color2)).uv(0.0F, 1.0F).endVertex();
                bufferbuilder.vertex(matrix, endX + 0.2f, endY + 0.2f, 0).color(ColorUtil.r(color2), ColorUtil.g(color2), ColorUtil.b(color2), ColorUtil.a(color2)).uv(1.0F, 1.0F).endVertex();
                bufferbuilder.vertex(matrix, endX + 0.2f, startY - 0.2f, 0).color(ColorUtil.r(color2), ColorUtil.g(color2), ColorUtil.b(color2), ColorUtil.a(color2)).uv(1.0F, 0.0F).endVertex();

               // bufferbuilder.vertex(matrix, startX, startY, 0).color(ColorUtil.r(color), ColorUtil.g(color), ColorUtil.b(color), ColorUtil.b.a(color)).uv(0.0F, 0.0F).endVertex();
               // bufferbuilder.vertex(matrix, startX, endY, 0).color(ColorUtil.r(color), ColorUtil.g(color), ColorUtil.b(color), ColorUtil.b.a(color)).uv(0.0F, 1.0F).endVertex();
               // bufferbuilder.vertex(matrix, endX, endY, 0).color(ColorUtil.r(color), ColorUtil.g(color), ColorUtil.b(color), ColorUtil.b.a(color)).uv(1.0F, 1.0F).endVertex();
               // bufferbuilder.vertex(matrix, endX, startY, 0).color(ColorUtil.r(color), ColorUtil.g(color), ColorUtil.b(color), ColorUtil.b.a(color)).uv(1.0F, 0.0F).endVertex();
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

    public Gif getParticle() {

        int a = MathUtils.randomInt(1, 6);
        switch (a) {
            case 1:
                return GifManager.getResource("X");
            case 2:
                return GifManager.getResource("infinity");
            case 3:
                return GifManager.getResource("line");
            case 4:
                return GifManager.getResource("BX");
            default:
                return GifManager.getResource("cirle");
        }
    }

    class Particl {
        public Vector3d prev;
        public Vector3d pos;
        public Vector3d pos2;
        public long time;
        public TimerUtil timer;
        public int color;
        public Vector2f rot;
        public Gif local;


        public Particl(LivingEntity e, Vector3d pos, long time, int color, Vector2f rot, Gif gif) {
            this.local = gif;
            this.rot = rot;
            this.prev = pos;
            this.time = time;
            this.color = color;
            this.timer = new TimerUtil();
            this.pos = e.position();
           // this.pos2 = new Vector3d(smoothPos((float) e.getX(), (float) e.xOld),smoothPos((float) e.getY(), (float) e.yOld),smoothPos((float) e.getZ(), (float) e.zOld));
           this.pos2 = e.getPosition((2));
        }
        public Vector3d getPrePos(Entity entity,float prev){
return null;
        }
        public float smoothPos(float pos,float oldPos){
            float prev = (float) (pos-oldPos);
            if (prev == 0)return pos;
            if (prev<1){
                return   pos + 2.5f;
            }else if (prev < 0){
                return   pos - 2.5f;
            }
            return pos;
        }

        public int getColor(int a) {
            return ColorUtil.swapAlpha(-1, a);
        }

        public float getX(float procent) {
            return (float) ((MathUtils.lerp((float) pos2.x, (float) pos.x, procent)) + prev.x);
        }

        public float getY(float procent) {
            return (float) ((MathUtils.lerp((float) pos2.y, (float) pos.y, procent)) + prev.y);
        }

        public float getZ(float procent) {
            return (float) ((MathUtils.lerp((float) pos2.z, (float) pos.z, procent)) + prev.z);
        }
    }
}
