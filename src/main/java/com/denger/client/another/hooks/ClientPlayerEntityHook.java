package com.denger.client.another.hooks;

import com.denger.client.another.hooks.forge.even.addevents.RotationEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.client.util.ClientRecipeBook;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.stats.StatisticsManager;
import net.minecraftforge.common.MinecraftForge;

public class ClientPlayerEntityHook extends ClientPlayerEntity {
    public RotationEvent exent = new RotationEvent(0, 0);

    public ClientPlayerEntityHook(Minecraft p_i232461_1_, ClientWorld p_i232461_2_, ClientPlayNetHandler p_i232461_3_, StatisticsManager p_i232461_4_, ClientRecipeBook p_i232461_5_, boolean p_i232461_6_, boolean p_i232461_7_) {
        super(p_i232461_1_, p_i232461_2_, p_i232461_3_, p_i232461_4_, p_i232461_5_, p_i232461_6_, p_i232461_7_);
    }

    @Override
    public void tick() {
        exent = new RotationEvent(this.xRot, yRot);
        MinecraftForge.EVENT_BUS.post(exent);
        this.xRot = exent.getxRot();
        this.yRot = exent.getyRot();
        super.tick();
        this.xRot = exent.getStaticXrot();
        this.yRot = exent.getStaticYrot();
    }

    @Override
    public void aiStep() {
        if (exent.isStepCancel()) {
            this.xRot = exent.getStaticXrot();
            this.yRot = exent.getStaticYrot();
        }
        super.aiStep();
        if (exent.isStepCancel()) {
            this.xRot = exent.getxRot();
            this.yRot = exent.getyRot();
        }


    }

    @Override
    public void serverAiStep() {
        if (exent.isStepCancel()) {
            this.xRot = exent.getxRot();
            this.yRot = exent.getyRot();
        }
        super.serverAiStep();
        if (exent.isStepCancel()) {
            this.xRot = exent.getStaticXrot();
            this.yRot = exent.getStaticYrot();
        }
    }
}
