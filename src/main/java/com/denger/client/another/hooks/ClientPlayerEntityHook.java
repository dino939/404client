package com.denger.client.another.hooks;

import com.denger.client.another.hooks.forge.even.addevents.RotationEvent;
import com.denger.client.another.hooks.forge.even.addevents.RotationPostEvent;
import com.denger.client.modules.mods.combat.NoPush;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.client.util.ClientRecipeBook;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.stats.StatisticsManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.MinecraftForge;

import static com.denger.client.Main.getInstance;

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
        MinecraftForge.EVENT_BUS.post(new RotationPostEvent());
        this.xRot = exent.getStaticXrot();
        this.yRot = exent.getStaticYrot();
    }

    @Override
    public void baseTick() {

        super.baseTick();
        if (getInstance.getRegisterModule().isEnable(NoPush.class)) {
            this.setBoundingBox(new AxisAlignedBB(this.getBoundingBox().maxX, this.getBoundingBox().maxY, this.getBoundingBox().maxZ, this.getBoundingBox().minX, this.getBoundingBox().minY, this.getBoundingBox().minZ));

        }

    }

    @Override
    public void push(double p_70024_1_, double p_70024_3_, double p_70024_5_) {


        if (getInstance.getRegisterModule().isEnable(NoPush.class)) {
            return;
        }
        super.push(p_70024_1_, p_70024_3_, p_70024_5_);
    }

    @Override
    public void serverAiStep() {
        this.xRot = exent.getStaticXrot();
        this.yRot = exent.getStaticYrot();
        super.serverAiStep();
        this.yHeadRot = exent.getyRot();
        this.xRot = exent.getxRot();
        this.yRot = exent.getyRot();

    }
}
