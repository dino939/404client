package com.denger.client.modules.mods.combat;


import com.denger.client.another.hooks.forge.even.addevents.WorldUpdate;
import com.denger.client.another.settings.SettingTarget;
import com.denger.client.another.settings.sett.BoolSetting;
import com.denger.client.another.settings.sett.ModSetting;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.ModuleTarget;
import com.denger.client.utils.Utils;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import com.denger.client.modules.another.Category;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.denger.client.MainNative.mc;
@ModuleTarget(ModName = "UsjhhfsCpu",category = Category.COMBAT)
public class TriggerBot extends Module {
    @SettingTarget(name = "Крит режим")
    private ModSetting сrit = new ModSetting().setMods("умный", "толко", "без").setCurent("умный");
@SettingTarget(name = "Толко мечь")
    private BoolSetting Only_Sword = new BoolSetting().setBol(true);
    boolean critflag;

    @SubscribeEvent
    public void onUpdate(WorldUpdate e) {
        if (Utils.checkNot())return;
        if (mc.player.getUseItem().getItem() != Items.SHIELD) {
            if (!(mc.hitResult instanceof EntityRayTraceResult))return;
            if (((EntityRayTraceResult) mc.hitResult).getEntity() instanceof PlayerEntity) {
                 if (mc.player.getAttackStrengthScale(0.75f) == 1) {
                    if ((mc.player.getMainHandItem().getItem() instanceof SwordItem) && Only_Sword.getState()) {
                        mc.gameMode.attack(mc.player, ((EntityRayTraceResult) mc.hitResult).getEntity());
                        mc.player.swing(Hand.MAIN_HAND);
                    } else if (!Only_Sword.getState()) {
                        mc.gameMode.attack(mc.player, ((EntityRayTraceResult) mc.hitResult).getEntity());
                        mc.player.swing(Hand.MAIN_HAND);
                    }

                }

            } else {
                if ((mc.player.getMainHandItem().getItem() instanceof SwordItem) && Only_Sword.getState()) {

                    mc.gameMode.attack(mc.player, ((EntityRayTraceResult) mc.hitResult).getEntity());
                    mc.player.swing(Hand.MAIN_HAND);
                } else if (!Only_Sword.getState()) {
                    mc.gameMode.attack(mc.player, ((EntityRayTraceResult) mc.hitResult).getEntity());
                    mc.player.swing(Hand.MAIN_HAND);
                }
            }

        }
    }
    public void attackModule(Entity t) {
        assert mc.player != null;
        boolean flag = mc.player.getAttackStrengthScale(0.5f) > 0.9f;
        boolean flag2 = flag && mc.player.fallDistance != 0.0f && !mc.player.isOnGround() && !mc.player.isRidingJumpable();
        float f5 = mc.player.getAttackStrengthScale(0.0f);
        if ((mc.level != null ? mc.level.getBlockState(new BlockPos(mc.player.getX(),mc.player.getY() + 0.10000000149011612, mc.player.getZ())).getBlock() : null) == Blocks.AIR);

        boolean f = false;
        switch (сrit.getCurent()) {
            case "умный": {
                f = !mc.options.keyJump.isDown() || flag2 && this.critflag;
                break;
            }
            case "без": {
                f = true;
                break;
            }
            case "толко": {
                f = flag2 && this.critflag;
                break;
            }
        }
        if ((double) f5 > 0.9 && f) {
            if (mc.player.isSprinting()) {
                mc.player.setSprinting(false);
            }
            assert mc.gameMode != null;
            mc.gameMode.attack(mc.player, t);
            mc.player.swing(Hand.MAIN_HAND);
         }
        this.critflag = flag2;
    }
}
