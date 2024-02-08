package com.denger.client.modules.mods.render;

import com.denger.client.another.hooks.forge.even.addevents.RenderWorldEvent;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.denger.client.Main.mc;

@ModuleTarget(ModName = "", category = Category.RENDER)
public class FirstPersonModel extends Module {

boolean a;
    @SubscribeEvent
    public void onRender(RenderWorldEvent e) {
        if (!a &&  mc.options.getCameraType().isFirstPerson()){
            a = true;
            isFirst();
        }else if (a && !mc.options.getCameraType().isFirstPerson()){
            a = false;
            isNotFirst();
        }
        renderEntity(mc.player, e.getMs(), e.getInfo(), e.getField());




    }

    public void isFirst(){

    }
    public void isNotFirst(){

    }

    @Override
    public void onDisable() {
        isNotFirst();
    }

    public void renderEntity(Entity entity, MatrixStack ms, ActiveRenderInfo p_228426_6_, float p_228426_2_) {
        IRenderTypeBuffer.Impl irendertypebuffer$impl = mc.renderBuffers().bufferSource();
        Vector3d vector3d = p_228426_6_.getPosition();
        double d0 = vector3d.x();
        double d1 = vector3d.y();
        double d2 = vector3d.z();
        this.renderEntity(entity, d0, d1, d2, p_228426_2_, ms, irendertypebuffer$impl);
    }

    private void renderEntity(Entity p_228418_1_, double p_228418_2_, double p_228418_4_, double p_228418_6_, float p_228418_8_, MatrixStack p_228418_9_, IRenderTypeBuffer p_228418_10_) {
        double d0 = MathHelper.lerp(p_228418_8_, p_228418_1_.xOld, p_228418_1_.getX());
        double d1 = MathHelper.lerp(p_228418_8_, p_228418_1_.yOld, p_228418_1_.getY());
        double d2 = MathHelper.lerp(p_228418_8_, p_228418_1_.zOld, p_228418_1_.getZ());
        float f = MathHelper.lerp(p_228418_8_, p_228418_1_.yRotO, p_228418_1_.yRot);
        mc.getEntityRenderDispatcher().render(p_228418_1_, d0 - p_228418_2_, d1 - p_228418_4_, d2 - p_228418_6_, f, p_228418_8_, p_228418_9_, p_228418_10_, mc.getEntityRenderDispatcher().getPackedLightCoords(p_228418_1_, p_228418_8_));
    }
}
