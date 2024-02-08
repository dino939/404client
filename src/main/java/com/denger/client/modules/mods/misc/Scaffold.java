package com.denger.client.modules.mods.misc;

import com.denger.client.another.hooks.forge.even.addevents.WorldUpdate;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

import static com.denger.client.Main.mc;

@ModuleTarget(ModName = "Tdbggpme", category = Category.MISC)
public class Scaffold extends Module {
    @SubscribeEvent
    public void ontTick(WorldUpdate e) {
        assert mc.player != null;
        assert mc.level != null;
        double posX = mc.player.getX();
        double posY = mc.player.getY() - 1;
        double posZ = mc.player.getZ();

        if (mc.level.getBlockState(new BlockPos(posX, posY, posZ)).isAir() && mc.player.isOnGround() && !mc.options.keyUp.isDown()) {
             if (75 < mc.player.xRot && mc.player.xRot < 90){
            mc.options.keyUse.setDown(false);
            mc.options.keyUse.setDown(true);
            mc.options.keyUse.setDown(false);
            mc.options.keyUse.setDown(true);
            mc.options.keyUse.setDown(false);
            mc.options.keyUse.setDown(true);
           }
            mc.options.keyShift.setDown(true);
        } else {
            mc.options.keyDown.setDown(GLFW.glfwGetKey(mc.getWindow().getWindow(), mc.options.keyDown.getKey().getValue()) == 1);
            mc.options.keyShift.setDown(GLFW.glfwGetKey(mc.getWindow().getWindow(), mc.options.keyShift.getKey().getValue()) == 1);
            mc.options.keyUse.setDown(GLFW.glfwGetMouseButton(mc.getWindow().getWindow(), mc.options.keyUse.getKey().getValue()) == 1);


        }
    }

}
