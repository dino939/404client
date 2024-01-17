package com.denger.client.modules.mods.hud;

import com.denger.client.another.resource.NativeManager;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import com.denger.client.utils.Utils;
import com.denger.client.utils.rect.*;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.denger.client.MainNative.getInstance;
import static com.denger.client.MainNative.mc;


@ModuleTarget(ModName = "Pwfsmbz", category = Category.HUD)
public class Overlay extends Module {


    @Override
    public void onDisable() {

    }

    @SubscribeEvent
    public void onRender2D(RenderGameOverlayEvent.Post e) {
        //RectUtil.drawRect(45,45,55,55,-1);

        if (e.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            BloomUtil.registerRenderCall(()->{
                RenderUtil.drawImageId(e.getMatrixStack(), Utils.getTextureId(NativeManager.getResource("extazyy", getInstance.getNativeManager().extazyy)),10, 10, 80, 80, getInstance.theme.getColor(100));
            });
            BloomUtil.draw(6);
            BlurUtil.registerRenderCall(()->{
                RenderUtil.drawImageId(e.getMatrixStack(), Utils.getTextureId(NativeManager.getResource("extazyy", getInstance.getNativeManager().extazyy)),10, 10, 80, 80, getInstance.theme.getColor(100));
            });
            BlurUtil.draw(6);


        }
    }


}
