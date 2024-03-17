package com.denger.client.modules.mods.render;

import com.denger.client.another.resource.ImageManager;
import com.denger.client.another.settings.SettingTarget;
import com.denger.client.another.settings.sett.BoolSetting;
import com.denger.client.another.settings.sett.FloatSetting;
import com.denger.client.another.settings.sett.ModSetting;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import com.denger.client.utils.ColorUtil;
import com.denger.client.utils.MathUtils;
import com.denger.client.utils.TimerUtil;
import com.denger.client.utils.rect.RenderUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL46;

import java.util.ArrayList;
import java.util.Objects;

import static com.denger.client.Main.getInstance;
import static com.denger.client.Main.mc;

@ModuleTarget(ModName = "IjuCvccmf", category = Category.RENDER)
public class HitBubble extends Module {
    @SettingTarget(name = "Мод ->", toAdd = false)
    ModSetting modSetting = new ModSetting().setMods("Bubble").setCurent("Bubble");
    @SettingTarget(name = "Крутица?")
    BoolSetting rotationOwn = new BoolSetting().setBol(true);
    @SettingTarget(name = "Сохранение камеры")
    BoolSetting rotationC = new BoolSetting().setBol(true);
    @SettingTarget(name = "Длительность")
    FloatSetting speed = new FloatSetting().setMin(1000).setMax(5000).setVal(2900).seType("ms");
    private ResourceLocation bubble;
    ArrayList<Bubble> bubbles = new ArrayList<>();
    TimerUtil cooldown = new TimerUtil();

    @SubscribeEvent
    public void onAttack(AttackEntityEvent event) {
        if (cooldown.hasReached(15)){
            cooldown.reset();
        }else {
            return;
        }
        assert mc.player != null;
        if (event.getPlayer() == mc.player) {
            bubbles.add(new Bubble(new Vector3d(event.getTarget().getX(), event.getTarget().getY(), event.getTarget().getZ()), (float) (event.getTarget().getBbHeight() / 1.6), mc.gameRenderer.getMainCamera().rotation().copy(), getInstance.theme.getC().getRGB()));
        }
    }

    @SubscribeEvent
    public void on3DRender(RenderWorldLastEvent renderWorldLastEvent) {
        bubbles.removeIf(Bubble::needToRemove);

        bubbles.stream().filter(Objects::nonNull).forEach(bubbleObj -> {

            MatrixStack matrixStack = new MatrixStack();
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(mc.getEntityRenderDispatcher().camera.getXRot()));
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(mc.getEntityRenderDispatcher().camera.getYRot() + 180));
            final double x = bubbleObj.pos.x();
            final double y = bubbleObj.pos.y();
            final double z = bubbleObj.pos.z();
            final EntityRendererManager renderManager = Minecraft.getInstance().getEntityRenderDispatcher();
            final Vector3d renderPos = renderManager.camera.getPosition();
            matrixStack.translate((float) (x - renderPos.x()), (float) (y - renderPos.y() + bubbleObj.BbHeight), (float) (z - renderPos.z()));
            final ActiveRenderInfo renderInfo = mc.gameRenderer.getMainCamera();
            final Quaternion rotation = renderInfo.rotation().copy();
            matrixStack.mulPose(rotationC.getState() ? bubbleObj.quaternion : rotation);
            float siz = -0.1f;

            matrixStack.scale(siz, siz, siz);
            matrixStack.pushPose();


            matrixStack.translate(0, 0, 0.5f);


            if (bubbleObj.getLifeProgers() <= 0.5f) {
                bubbleObj.anim = MathUtils.lerp(bubbleObj.anim, 1, bubbleObj.getLifeProgers() / 2);
            } else {
                bubbleObj.anim = MathUtils.lerp(bubbleObj.anim, 0, (MathUtils.calcPercentage(MathUtils.clamp(bubbleObj.getLifeProgers(), 0.50f, 1), 0.50f, 1) / 100));
            }


            bubbleObj.color = ColorUtil.swapAlpha(bubbleObj.color, MathUtils.clamp((1 - bubbleObj.getLifeProgers()), 0, 1) * 255);
            mc.getTextureManager().bind(getBubbleTex());
            RenderUtil.scale(matrixStack, -5, -5, 10, 10, bubbleObj.anim, () -> {
                RenderUtil.rotate(matrixStack, -5, -5, 10, 10, rotationOwn.getState() ? bubbleObj.getLifeProgers() * -(360*speed.getVal()/500) : 0, () -> {
                    bufferbuilder.begin(GL20.GL_QUADS, DefaultVertexFormats.POSITION_COLOR_TEX);
                    bufferbuilder.vertex(matrixStack.last().pose(), -5, -5, 0).color(ColorUtil.r(bubbleObj.color), ColorUtil.g(bubbleObj.color), ColorUtil.b(bubbleObj.color), bubbleObj.anim).uv(0.0F, 0.0F).endVertex();
                    bufferbuilder.vertex(matrixStack.last().pose(), -5, 5, 0).color(ColorUtil.r(bubbleObj.color), ColorUtil.g(bubbleObj.color), ColorUtil.b(bubbleObj.color), bubbleObj.anim).uv(0.0F, 1.0F).endVertex();
                    bufferbuilder.vertex(matrixStack.last().pose(), 5, 5, 0).color(ColorUtil.r(bubbleObj.color), ColorUtil.g(bubbleObj.color), ColorUtil.b(bubbleObj.color), bubbleObj.anim).uv(1.0F, 1.0F).endVertex();
                    bufferbuilder.vertex(matrixStack.last().pose(), 5, -5, 0).color(ColorUtil.r(bubbleObj.color), ColorUtil.g(bubbleObj.color), ColorUtil.b(bubbleObj.color), bubbleObj.anim).uv(1.0F, 0.0F).endVertex();

                    GL46.glDepthMask(false);
                    GL46.glDisable(2884);
                    GL46.glEnable(3042);
                    GL46.glDisable(3008);
                    GL46.glEnable(GL46.GL_BLEND);
                    GL46.glBlendFunc(GL46.GL_SRC_ALPHA, GL46.GL_ONE);
                    tessellator.end();

                    GL46.glBlendFunc(GL46.GL_SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.value);
                    GL46.glDisable(GL46.GL_BLEND);
                    GL46.glEnable(3008);
                    GL46.glDisable(3042);
                    GL46.glEnable(2884);
                    GL46.glDepthMask(true);

                });


            });


        });

    }

    ResourceLocation getBubbleTex() {
        if (bubble == null) {
            bubble = ImageManager.getResource("bubble.png");
        }
        if (modSetting.getCurent().equals("Bubble")) {
            return bubble;
        }
        return bubble;
    }

    private float getMaxLifeTime() {
        return speed.getVal();
    }

    private class Bubble {
        Vector3d pos;
        float BbHeight;
        int color;
        long bornTime;
        Quaternion quaternion;
        float anim;

        public Bubble(Vector3d pos, float BbHeight, Quaternion quaternion, int color) {
            this.pos = pos;
            this.BbHeight = BbHeight;
            this.quaternion = quaternion;
            this.color = color;
            bornTime = System.currentTimeMillis();
            this.anim = 0;
        }

        public float getLifeProgers() {
            return (System.currentTimeMillis() - bornTime) / getMaxLifeTime();
        }

        public boolean needToRemove() {
            return getLifeProgers() > 1.1;
        }
    }
}
