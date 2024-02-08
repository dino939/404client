package com.denger.client.utils;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import static com.denger.client.Main.mc;

public class PlayerUtil {

    public static void attack(Entity entity) {
        mc.gameMode.attack(mc.player, entity);
        mc.player.swing(Hand.MAIN_HAND);
    }

    public static void click() {
        ObfuscationReflectionHelper.setPrivateValue(KeyBinding.class, mc.options.keyAttack, 1, "field_151474_i");
    }

    public static boolean checkBot(Entity entity) {
        return entity.getUUID().equals(PlayerEntity.createPlayerUUID(entity.getName().getString()));
    }

    public static boolean checkFall() {
        if (mc.player.isInWater() || mc.player.isInLava() || mc.player.onClimbable() || mc.player.isRidingJumpable() || mc.player.isFallFlying() || mc.player.abilities.flying || mc.player.hasEffect(Effects.LEVITATION)) {
            return true;
        } else if (mc.player.isOnGround() && !mc.options.keyJump.isDown()) {
            return true;
        } else if (mc.level.getBlockCollisions(mc.player, new AxisAlignedBB(mc.player.getX() - 0.3, mc.player.getY() + mc.player.getEyeHeight(), mc.player.getZ() + 0.3, mc.player.getX() + 0.3, mc.player.getY() + 2.5, mc.player.getZ() - 0.3)).findAny().isPresent()) {
            return mc.player.fallDistance > 0;
        } else {
            return mc.player.fallDistance > 0.2;
        }
    }

}