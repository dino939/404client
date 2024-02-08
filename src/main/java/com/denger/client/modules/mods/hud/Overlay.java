package com.denger.client.modules.mods.hud;

import com.denger.client.another.resource.ImageManager;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import com.denger.client.utils.Utils;
import com.denger.client.utils.anims.CycleAnimation;
import com.denger.client.utils.anims.Type;
import com.denger.client.utils.rect.*;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.denger.client.Main.getInstance;


@ModuleTarget(ModName = "Pwfsmbz", category = Category.HUD)
public class Overlay extends Module {
    CycleAnimation cycleAnimation = new CycleAnimation(1,100,0.5f, Type.LERP);

    @Override
    public void onDisable() {

    }

    @SubscribeEvent
    public void onRender2D(RenderGameOverlayEvent.Post e) {
        //RectUtil.drawRect(45,45,55,55,-1);

        float a = cycleAnimation.getAnim();
        if (e.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            BloomUtil.registerRenderCall(()->{
                RenderUtil.drawImageId(e.getMatrixStack(), Utils.getTextureId(ImageManager.getResource("extazyy.png")),a, 10, 80, 80, getInstance.theme.getColor(100));
            });
            BloomUtil.draw(6);
            BlurUtil.registerRenderCall(()->{
                RenderUtil.drawImageId(e.getMatrixStack(), Utils.getTextureId(ImageManager.getResource("extazyy.png")),a, 10, 80, 80, getInstance.theme.getColor(100));
            });
            BlurUtil.draw(6);


        }
    }


}
