package com.denger.client.modules.mods.hud;

import com.denger.client.another.resource.Gif;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import com.denger.client.utils.rect.RectUtil;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.denger.client.MainNative.getInstance;


@ModuleTarget(ModName = "Pwfsmbz", category = Category.HUD)
public class Overlay extends Module {

Gif gif = getInstance.getGifManager().getGifs().get(1);

    @Override
    public void onDisable() {

    }

    @SubscribeEvent
    public void onRender2D(RenderGameOverlayEvent.Post e) {
        //RectUtil.drawRect(45,45,55,55,-1);
        if (e.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            // Transform.translate(100,100,1);
            //Transform.rotate(180,0,0,1);
            RectUtil.drawRoundedTexture(gif.getResource(),5,5,150,150,4);
           // RenderUtil.drawImageId(new MatrixStack(),Utils.getTextureId(),5,5,150,15,-1);
            //EmulatorUtil.downloadImage("https://cdn.discordapp.com/attachments/1029135008583135272/1174022760255721553/image.png?ex=656614e6&is=65539fe6&hm=0e23c40a053c90ecbc209d877ea7276c8388a20691ce4f3eab8659b7351236db&",-10,-10,100,100);

            //Transform.stop();
        }
    }


}
