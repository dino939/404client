package com.denger.client.modules.mods.combat;

import com.denger.client.another.hooks.forge.even.addevents.RotationEvent;
import com.denger.client.another.settings.SettingTarget;
import com.denger.client.another.settings.sett.FloatSetting;
import com.denger.client.another.settings.sett.ModSetting;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import com.denger.client.utils.AnimationUtils;
import com.denger.client.utils.MathUtils;
import com.denger.client.utils.Utils;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Comparator;

import static com.denger.client.MainNative.mc;

@ModuleTarget(ModName = "LjmmBvsb", category = Category.COMBAT)
public class KillAura extends Module {

    @SettingTarget(name = "Ротации")
    private ModSetting rots = new ModSetting().setMods("Matrix", "NCP").setCurent("Matrix");
    @SettingTarget(name = "Дальность")
    private FloatSetting ramge = new FloatSetting().setMax(10).setMin(0.1f).setVal(4.1f);
    @SettingTarget(name = "Крит режим")
    private ModSetting сrit = new ModSetting().setMods("умный", "толко", "без").setCurent("умный");

    boolean critflag;

    AnimationUtils yawAnim = new AnimationUtils(0.0f, 0.0f, 0.1f);
    AnimationUtils pitchAnim = new AnimationUtils(0.0f, 0.0f, 0.1f);
    private LivingEntity target;
    private Vector2f rotations = null;

    @Override
    public void onEnable() {
        assert mc.player != null;
        this.yawAnim = new AnimationUtils(mc.player.yRot, mc.player.yRot, 0.1f);
        this.pitchAnim = new AnimationUtils(mc.player.xRot, mc.player.xRot, 0.1f);
    }


    public void rotMatrix(LivingEntity t) {
        float speed = 0.05f;
        assert mc.player != null;
        float yo = mc.player.distanceTo(t) / ramge.getVal() * 1.2f;
        yo = MathUtils.clamp(yo, 0.0f, 1.2f);
        mc.player.getX();
        float[] rot = Utils.rotationsPro(t, yo);
        this.yawAnim.speed = speed;
        this.pitchAnim.speed = speed;
        this.yawAnim.to = rot[0];
        this.pitchAnim.to = rot[1];

    }

    public void rotNCP(LivingEntity t) {
        float speed = 0.05f;

        float yo = mc.player.distanceTo(t) / ramge.getVal() * 1.2f;
        yo = MathUtils.clamp(yo, 0.0f, 1.2f);
        float[] rot = Utils.rotationsPro(t, yo);
        this.yawAnim.speed = speed;
        this.pitchAnim.speed = speed;
        this.yawAnim.to = rot[0];
        this.pitchAnim.to = rot[1];
    }

    @SubscribeEvent
    public void onRot(RotationEvent e) {
        if (rotations != null) {
            e.setxRot(rotations.x);
            e.setyRot(rotations.y);
            rotations = null;
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent e) {
        if (mc.level != null) {
            target = mc.level.players().stream().filter(entityPlayer -> entityPlayer != mc.player).min(Comparator.comparing(entityPlayer ->
            {
                assert mc.player != null;
                return entityPlayer.distanceTo(mc.player);
            })).filter(entityPlayer -> entityPlayer.distanceTo(mc.player) <= ramge.getVal()).orElse(null);
        }
        try {
            if (target != null) {
                if (mc.player == null) return;
                switch (rots.getCurent()) {
                    case "Matrix": {
                        rotMatrix(target);
                        break;
                    }
                    case "NCP": {
                        rotNCP(target);
                        break;
                    }
                }
                rotations = new Vector2f(pitchAnim.getAnim(), yawAnim.getAnim());
                attackModule(target);
            }
        } catch (Exception ignore) {
        }


    }

    public void attackModule(Entity t) {
        assert mc.player != null;
        boolean flag = mc.player.getAttackStrengthScale(0.5f) > 0.9f;
        boolean flag2 = flag && mc.player.fallDistance != 0.0f && !mc.player.isOnGround() && !mc.player.isRidingJumpable();
        float f5 = mc.player.getAttackStrengthScale(0.0f);
        if ((mc.level != null ? mc.level.getBlockState(new BlockPos(mc.player.getX(), mc.player.getY() + 0.10000000149011612, mc.player.getZ())).getBlock() : null) == Blocks.AIR) ;

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
