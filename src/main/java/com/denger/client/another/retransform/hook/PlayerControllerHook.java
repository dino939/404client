package com.denger.client.another.retransform.hook;

import com.denger.client.another.hooks.ClientPlayerEntityHook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.multiplayer.PlayerController;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.client.util.ClientRecipeBook;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.stats.StatisticsManager;

import static com.denger.client.Main.mc;

public class PlayerControllerHook extends PlayerController {

    public PlayerControllerHook(Minecraft p_i45062_1_, ClientPlayNetHandler p_i45062_2_) {
        super(p_i45062_1_, p_i45062_2_);
    }

    @Override
    public ClientPlayerEntity createPlayer(ClientWorld p_199681_1_, StatisticsManager p_199681_2_, ClientRecipeBook p_199681_3_) {
        return this.createPlayer(p_199681_1_, p_199681_2_, p_199681_3_, false, false);
    }
    @Override
    public ClientPlayerEntity createPlayer(ClientWorld p_239167_1_, StatisticsManager p_239167_2_, ClientRecipeBook p_239167_3_, boolean p_239167_4_, boolean p_239167_5_) {
        return new ClientPlayerEntityHook(mc, p_239167_1_, mc.getConnection(), p_239167_2_, p_239167_3_, p_239167_4_, p_239167_5_);
    }


}
