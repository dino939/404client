package com.denger.client.modules.mods.render;

import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import com.denger.client.utils.rect.RectUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.event.RenderNameplateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.awt.*;

import static com.denger.client.Main.*;

@ModuleTarget(ModName = "ObnfUbh", category = Category.RENDER)
public class NameTag extends Module {


    @SubscribeEvent
    public void onRender2d(RenderNameplateEvent e) {
        e.setContent(ITextComponent.nullToEmpty(""));
        if (!(e.getEntity() instanceof AbstractClientPlayerEntity) && mc.player != e.getEntity()) return;
        PlayerEntity entity = (PlayerEntity) e.getEntity();
        float partialTicks = e.getPartialTicks();
        assert mc.level != null;
        e.setCanceled(false);
        MatrixStack matrixStack = new MatrixStack();
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(mc.getEntityRenderDispatcher().camera.getXRot()));
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(mc.getEntityRenderDispatcher().camera.getYRot() + 180));
        final double x = entity.xo + (entity.getX() - entity.xo) * partialTicks;
        final double y = entity.yo + (entity.getY() - entity.yo) * partialTicks;
        final double z = entity.zo + (entity.getZ() - entity.zo) * partialTicks;
        final EntityRendererManager renderManager = Minecraft.getInstance().getEntityRenderDispatcher();
        final Vector3d renderPos = renderManager.camera.getPosition();
        matrixStack.translate((float) (x - renderPos.x()), (float) (y - renderPos.y() + entity.getBbHeight() + 0.5f), (float) (z - renderPos.z()));
        final ActiveRenderInfo renderInfo = mc.gameRenderer.getMainCamera();
        final Quaternion rotation = renderInfo.rotation().copy();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        matrixStack.mulPose(rotation);
        matrixStack.scale(-0.025f, -0.025f, -0.025f);
        matrixStack.pushPose();
        String name = entity.getName().getString();
        float w = fontManager.font20.getStringWidth(name) + 5;
        float h = fontManager.font28.getFontHeight() / 2;
        float cen = (-35.5f - w + 7) / 2;
        RectUtil.drawGradientRound(matrixStack, cen + 13.5f, -6.5f, w + 0.5f, h + 4.5f, 2, getInstance.theme.getColor(100), getInstance.theme.getColor(200), getInstance.theme.getColor(300), getInstance.theme.getColor(400));
        matrixStack.translate(0, 0, 0.5f);

        RectUtil.drawRound(matrixStack, cen + 13.5f, -6, w, h + 4, 2, new Color(0, 0, 0, 215).hashCode());

        fontManager.font20.drawCenteredString(matrixStack, name, -1.5f, -0, -1);


        matrixStack.popPose();
        matrixStack.translate(0.0, -12, 0.0);
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(true);


    }


}
