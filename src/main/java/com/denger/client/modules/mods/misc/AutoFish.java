package com.denger.client.modules.mods.misc;

import com.denger.client.another.hooks.forge.even.addevents.WorldUpdate;
import com.denger.client.another.networkutills.ConnectionUtil;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import com.denger.client.utils.TimerUtil;
import net.minecraft.network.play.client.CPlayerTryUseItemPacket;
import net.minecraft.network.play.server.SPlaySoundEffectPacket;
import net.minecraft.util.Hand;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.denger.client.Main.mc;

@ModuleTarget(ModName = "BvupGjti", category = Category.MISC)
public class AutoFish extends Module {
    private final TimerUtil delay = new TimerUtil();
    private boolean isHooked = false;
    private boolean needToHook = false;

    @Override
    public boolean onPacket(Object packet, ConnectionUtil.Side side) {
        if (packet instanceof SPlaySoundEffectPacket && ((SPlaySoundEffectPacket) packet).getSound().getLocation().getPath().equals("entity.fishing_bobber.splash")) {
            this.isHooked = true;
            this.delay.reset();
        }
        return super.onPacket(packet, side);
    }

    @SubscribeEvent
    public void onUpdate(WorldUpdate e) {
        if (this.delay.hasReached(600L) && this.isHooked) {
            mc.player.connection.send(new CPlayerTryUseItemPacket(Hand.MAIN_HAND));
            this.isHooked = false;
            this.needToHook = true;
            this.delay.reset();
        }
        if (this.delay.hasReached(300L) && this.needToHook) {
            mc.player.connection.send(new CPlayerTryUseItemPacket(Hand.MAIN_HAND));
            this.needToHook = false;
            this.delay.reset();
        }

    }

    @Override
    public void onDisable() {
        this.delay.reset();
        this.isHooked = false;
        this.needToHook = false;
    }


}
