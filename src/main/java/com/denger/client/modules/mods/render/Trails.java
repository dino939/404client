package com.denger.client.modules.mods.render;

import com.denger.client.Main;
import com.denger.client.another.resource.GifManager;
import com.denger.client.another.resource.ImageManager;
import com.denger.client.another.settings.SettingTarget;
import com.denger.client.another.settings.sett.BoolSetting;
import com.denger.client.another.settings.sett.FloatSetting;
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
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL46;

import java.util.ArrayList;

import static com.denger.client.Main.getInstance;
import static com.denger.client.Main.mc;
import static com.denger.client.utils.rect.RenderUtil.resetColor;
import static com.mojang.blaze3d.platform.GlStateManager._bindTexture;
import static com.mojang.blaze3d.platform.GlStateManager._disableBlend;

@ModuleTarget(ModName = "Usbjmt", category = Category.RENDER)
public class Trails extends Module {
    @SettingTarget(name = "От первого лица?")
    BoolSetting noFirstPerson = new BoolSetting().setBol(true);
    @SettingTarget(name = "Количество частиц")
    FloatSetting particleCount = new FloatSetting().setMin(1).setMax(20).setVal(3);
    @SettingTarget(name = "Продолжительность")
    FloatSetting duration = new FloatSetting().setMin(100).setMax(1000).setVal(500);
    @SettingTarget(name = "Диапазон")
    FloatSetting range = new FloatSetting().setMin(1).setMax(4).setVal(1);
    ArrayList<Particl> particls = new ArrayList<>();
    private Vector3d oldPos;

    @SubscribeEvent
    public void onUpdate(TickEvent.RenderTickEvent e) {
        particls.removeIf(particl -> {
            return particl.timer.hasReached(particl.time);
        });

        Vector3d NEW = mc.player.getPosition(e.renderTickTime).add(0, 0.45f, 0);
        if (mc.player.xOld != mc.player.getX() || mc.player.zOld != mc.player.getZ()) {
            if (oldPos != null) {
                double xt = NEW.x - oldPos.x;
                double yt = NEW.y - oldPos.y;
                double zt = NEW.z - oldPos.z;
                try {
                    double distance = Math.sqrt(xt * xt + yt * yt + zt * zt);
                    int numberOfPoints = (int) (distance * 100);
                   // for (int i = 0; i < numberOfPoints; i++) {
                   //     float x = MathUtils.interpolate((float) i, 0, (float) oldPos.x, numberOfPoints, (float) NEW.x);
                   //     float y = MathUtils.interpolate((float) i, 0, (float) oldPos.y, numberOfPoints, (float) NEW.y)+1;
                   //     float z = MathUtils.interpolate((float) i, 0, (float) oldPos.z, numberOfPoints, (float) NEW.z);
                   //     particls.add(new Particl(new Vector3d(x, y, z), (long) duration.getVal(),getInstance.theme.getColor()));
                   // }
                    for (int i = 0; i < numberOfPoints; i++) {
                        float x = MathUtils.interpolate((float) i, 0, (float) oldPos.x, numberOfPoints, (float) NEW.x);
                        float y = MathUtils.interpolate((float) i, 0, (float) oldPos.y, numberOfPoints, (float) NEW.y);
                        float z = MathUtils.interpolate((float) i, 0, (float) oldPos.z, numberOfPoints, (float) NEW.z);
                        particls.add(new Particl(new Vector3d(x, y, z), (long) duration.getVal(),getInstance.theme.getColor()));
                    }
                } catch (Exception ignore) {
                }
            }
            oldPos = NEW;
        }

    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent renderWorldLastEvent) {
        if (mc.options.getCameraType().isFirstPerson() && !noFirstPerson.getState()) {
            return;
        }
        MatrixStack ms = Utils.get3DMatrix();


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
                int a = (int) ((float) this.particls.indexOf(particl) / (float) this.particls.size() * 255);
                int color2 = ColorUtil.swapAlpha(particl.color, MathUtils.clamp(a,0,20));
                float proccess = (this.particls.indexOf(particl) / (float) this.particls.size());
                double x = particl.getX() - mc.getEntityRenderDispatcher().camera.getPosition().x;
                double y = particl.getY() - mc.getEntityRenderDispatcher().camera.getPosition().y;
                double z = particl.getZ() - mc.getEntityRenderDispatcher().camera.getPosition().z;
                float scale = -MathUtils.calculateValue(proccess * 100, 0.025f, 0.20f);

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



