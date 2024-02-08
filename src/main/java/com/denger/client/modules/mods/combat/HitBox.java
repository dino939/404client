package com.denger.client.modules.mods.combat;

import com.denger.client.another.hooks.forge.even.addevents.WorldUpdate;
import com.denger.client.another.networkutills.ConnectionUtil;
import com.denger.client.another.settings.SettingTarget;
import com.denger.client.another.settings.sett.BoolSetting;
import com.denger.client.another.settings.sett.FloatSetting;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import com.denger.client.utils.AnimationUtil;
import com.denger.client.utils.RotationUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.play.client.CPlayerPacket;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import static com.denger.client.Main.getInstance;
import static com.denger.client.Main.mc;

@ModuleTarget(ModName = "IjuCpy",category = Category.COMBAT)
public class HitBox extends Module {
    @SettingTarget(name = "Размер")
    FloatSetting razmer = new FloatSetting().setMin(0).setMax(1.5f).setVal(0.2f);
    @SettingTarget(name = "Невидимые")
    public static BoolSetting nevidimie = new BoolSetting();
    @SettingTarget(name = "Ротация")
    BoolSetting rotasiya = new BoolSetting();
    @SettingTarget(name = "От клиента")
    BoolSetting otClienta = new BoolSetting();
    AnimationUtil animationUtil = new AnimationUtil();
    boolean pointed;

    @SubscribeEvent
    public void onUpdate(WorldUpdate e) {

        try {
            pointed = true;
            animationUtil.goTo(RotationUtil.getRotationsNeeded((PlayerEntity) ((EntityRayTraceResult) mc.hitResult).getEntity(), mc.player.distanceTo(((EntityRayTraceResult) mc.hitResult).getEntity()))[0]);
        } catch (Exception ignored) {
            pointed = false;
        }
        if (rotasiya.getState() && otClienta.getState() && pointed) {
            mc.player.yRot = animationUtil.getAnim();
        }
        for (PlayerEntity playerEntity : mc.level.players()) {
            if (playerEntity != mc.player && !getInstance.getRegisterModule().isEnable(NoHitBoxes.class)) {
                if (getInstance.getFriendManager().isFriend(playerEntity)) {
                    playerEntity.setBoundingBox(new AxisAlignedBB(playerEntity.getX() - 0.3, playerEntity.getBoundingBox().minY, playerEntity.getZ() - 0.3, playerEntity.getX() + 0.3, playerEntity.getBoundingBox().maxY, playerEntity.getZ() + 0.3));
                } else {
                    playerEntity.setBoundingBox(new AxisAlignedBB(playerEntity.getX() - 0.3 - razmer.getVal(), playerEntity.getBoundingBox().minY, playerEntity.getZ() - 0.3 - razmer.getVal(), playerEntity.getX() + 0.3 + razmer.getVal(), playerEntity.getBoundingBox().maxY, playerEntity.getZ() + 0.3 + razmer.getVal()));
                }
            }
        }
    }

    @Override
    public void onDisable() {
        for (PlayerEntity playerEntity : mc.level.players()) {
            if (playerEntity != mc.player && !getInstance.getFriendManager().isFriend(playerEntity) && !getInstance.getRegisterModule().isEnable(NoHitBoxes.class)) {
                playerEntity.setBoundingBox(new AxisAlignedBB(playerEntity.getX() - 0.3, playerEntity.getBoundingBox().minY, playerEntity.getZ() - 0.3, playerEntity.getX() + 0.3, playerEntity.getBoundingBox().maxY, playerEntity.getZ() + 0.3));
            }
        }
        super.onDisable();
    }

    @Override
    public boolean onPacket(Object packet, ConnectionUtil.Side side) {
        if (packet instanceof CPlayerPacket && rotasiya.getState() && pointed) {
            ObfuscationReflectionHelper.setPrivateValue(CPlayerPacket.class, ((CPlayerPacket) packet), animationUtil.getAnim(), "field_149476_e");
        }
        return super.onPacket(packet, side);
    }
}
