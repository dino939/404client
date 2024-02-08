package com.denger.client.modules.mods.combat;

import com.denger.client.another.hooks.forge.even.addevents.WorldUpdate;
import com.denger.client.another.settings.SettingTarget;
import com.denger.client.another.settings.sett.FloatSetting;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import com.denger.client.utils.Utils;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.item.Items;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

import static com.denger.client.Main.mc;

@ModuleTarget(ModName = "BvupHbqqmf",category = Category.COMBAT)
public class AutoGapple extends Module {
@SettingTarget(name = "Здоровье")
    FloatSetting sett = new FloatSetting().setMin(1).setMax(20).setVal(10).seType(" хп");



    @SubscribeEvent
    public void onUpdate(WorldUpdate e){
        float fd = sett.getVal();
        assert mc.player != null;
        if ( mc.player.getHealth() < fd && mc.player.getOffhandItem().getItem() == Items.GOLDEN_APPLE){
            mc.options.keyUse.setDown(true);

        }else {
            mc.options.keyUse.setDown(GLFW.glfwGetMouseButton(mc.getWindow().getWindow(),  mc.options.keyUse.getKey().getValue()) == 1);
        }


        if (true)return;
        int a = Utils.findItem(Items.GOLDEN_APPLE);
        if (Utils.findItem(Items.TOTEM_OF_UNDYING) == -1 && a != -1){
            mc.gameMode.handleInventoryMouseClick(0, 45, 0, ClickType.PICKUP,mc.player);
            mc.gameMode.handleInventoryMouseClick(0, a, 0, ClickType.PICKUP,mc.player);
            mc.gameMode.handleInventoryMouseClick(0, 45, 0, ClickType.PICKUP,mc.player);
        }
    }
}
