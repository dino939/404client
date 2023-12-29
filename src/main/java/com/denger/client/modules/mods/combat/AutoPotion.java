package com.denger.client.modules.mods.combat;

import com.denger.client.another.hooks.forge.even.addevents.WorldUpdate;
import com.denger.client.another.networkutills.ConnectionUtil;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import com.denger.client.utils.ReflectFileld;
import net.minecraft.item.PotionItem;
import net.minecraft.network.play.client.CPlayerPacket;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

import static com.denger.client.MainNative.mc;

@ModuleTarget(ModName = "BvupQpujpo", category = Category.COMBAT)
public class AutoPotion extends Module {
    private int old;
    boolean setted;
    boolean roNeded = false;

    public void onDisable() {
        mc.options.keyUse.setDown(GLFW.glfwGetMouseButton(mc.getWindow().getWindow(), mc.options.keyUse.getKey().getValue()) == 1);
    }

    @SubscribeEvent
    public void onUpdate(WorldUpdate e) {
        if (mc.overlay == null && (mc.screen == null || mc.screen.passEvents)) {
            if (mc.player.inventory.getItem(mc.player.inventory.selected).getItem() instanceof PotionItem) {
                String potionName = getPotionName(mc.player.inventory.getItem(mc.player.inventory.selected).getDescriptionId());
                if (checkPotion(potionName) && isHoldingPotionItem()) {
                    if (checkNegativeEffect(potionName)) {
                        assert mc.player != null;
                        roNeded = true;
                         mc.options.keyUse.setDown(true);
                    }
                } else {
                    if (setted) {
                        setted = false;
                        mc.player.inventory.selected = old;
                    }
                    mc.options.keyUse.setDown(GLFW.glfwGetMouseButton(mc.getWindow().getWindow(), mc.options.keyUse.getKey().getValue()) == 1);

                }


            } else {
                if (setted) {
                    setted = false;
                    mc.player.inventory.selected = old;

                }
                mc.options.keyUse.setDown(GLFW.glfwGetMouseButton(mc.getWindow().getWindow(), mc.options.keyUse.getKey().getValue()) == 1);
            }

        }
        for (int index = 0; index < 9; ++index) {
            assert mc.player != null;
            if (mc.player.inventory.getItem(index).getItem() instanceof PotionItem) {
                String potionName = getPotionName(mc.player.inventory.getItem(index).getDescriptionId());
                if (checkPotion(potionName) && checkNegativeEffect(potionName)) {
                    setted = true;

                    old = mc.player.inventory.selected;
                    mc.player.inventory.selected = index;
                    break;
                }
            }
        }
    }

    @Override
    public boolean onPacket(Object packet, ConnectionUtil.Side side) {
        if (packet instanceof CPlayerPacket && roNeded){
            new ReflectFileld(packet, CPlayerPacket.class,3).setValue(90);
            roNeded = false;
        }
        return super.onPacket(packet, side);
    }

    private boolean isHoldingPotionItem() {
        assert mc.player != null;
        return mc.player.inventory.getItem(mc.player.inventory.selected).getItem() instanceof PotionItem;
    }

    private String getPotionName(String str) {
        return str.split("\\.")[str.split("\\.").length - 1];
    }

    private boolean checkPotion(String potName) {
        assert mc.player != null;
        for (EffectInstance effect : mc.player.getActiveEffects()) {
            Effect effectType = effect.getEffect();
            if (effectType.getRegistryName() != null && effectType.getRegistryName().getPath().equals(potName)) {
                return false;
            }
        }
        return true;
    }

    public boolean checkNegativeEffect(String name) {

        switch (name) {
            case "poison":
            case "slowness":
            case "harming":
            case "turtle_master":
                return false;
            default:
                return true;
        }
    }

    ;
}
