package com.denger.client.another.hooks;

import com.denger.client.another.hooks.models.CapeLayerHook;
import com.denger.client.another.hooks.models.HeadLayerHook;
import com.denger.client.another.hooks.models.RenderLayerHook;
import com.denger.client.modules.mods.render.DripMode;
import com.denger.client.modules.mods.render.NameTag2;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.UseAction;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;

import static com.denger.client.Main.getInstance;

public class HookPlayerRenderer extends PlayerRenderer {
    public DripMode dripMode = getInstance.getRegisterModule().getModule(DripMode.class);

    public HookPlayerRenderer(EntityRendererManager renderManager) {
        super(renderManager);
        this.layers.clear();
        this.model = new PlayerModel<>(0.0F, false);
        this.addLayer(new BipedArmorLayer<>(this, new BipedModel(0.5F), new BipedModel(1.0F)));
        this.addLayer(new HeldItemLayer<>(this));
        this.addLayer(new ArrowLayer<>(this));
        this.addLayer(new Deadmau5HeadLayer(this));
        this.addLayer(new CapeLayerHook(this));
        this.addLayer(new HeadLayerHook<>(this));
        this.addLayer(new ElytraLayer<>(this));
        this.addLayer(new ParrotVariantLayer<>(this));
        this.addLayer(new SpinAttackEffectLayer<>(this));
        this.addLayer(new BeeStingerLayer<>(this));
        this.addLayer(new RenderLayerHook(this));
    }


    public HookPlayerRenderer(EntityRendererManager renderManager, Boolean smallArms) {
        super(renderManager, smallArms);
        this.layers.clear();
        this.addLayer(new BipedArmorLayer<>(this, new BipedModel(0.5F), new BipedModel(1.0F)));
        this.addLayer(new HeldItemLayer<>(this));
        this.addLayer(new ArrowLayer<>(this));
        this.addLayer(new Deadmau5HeadLayer(this));
        this.addLayer(new CapeLayerHook(this));
        this.addLayer(new HeadLayerHook<>(this));
        this.addLayer(new ElytraLayer<>(this));
        this.addLayer(new ParrotVariantLayer<>(this));
        this.addLayer(new SpinAttackEffectLayer<>(this));
        this.addLayer(new BeeStingerLayer<>(this));
        this.addLayer(new RenderLayerHook(this));
    }

    @Override
    public void render(AbstractClientPlayerEntity p_225623_1_, float p_225623_2_, float p_225623_3_, MatrixStack p_225623_4_, IRenderTypeBuffer p_225623_5_, int p_225623_6_) {


        this.setModelProperties(p_225623_1_);
        if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderPlayerEvent.Pre(p_225623_1_, this, p_225623_3_, p_225623_4_, p_225623_5_, p_225623_6_)))
            return;
        if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderLivingEvent.Pre<>(p_225623_1_, this, p_225623_3_, p_225623_4_, p_225623_5_, p_225623_6_)))
            return;
        p_225623_4_.pushPose();
        this.model.attackTime = this.getAttackAnim(p_225623_1_, p_225623_3_);

        boolean shouldSit = p_225623_1_.isPassenger() && (p_225623_1_.getVehicle() != null && p_225623_1_.getVehicle().shouldRiderSit());
        this.model.riding = shouldSit;
        this.model.young = p_225623_1_.isBaby();
        float f = MathHelper.rotLerp(p_225623_3_, p_225623_1_.yBodyRotO, p_225623_1_.yBodyRot);

        float f1 = MathHelper.rotLerp(p_225623_3_, p_225623_1_.yHeadRotO, p_225623_1_.yHeadRot);

        if (dripMode.getState()) {
            if (dripMode.mod.getCurent().equals("Custom")) {
                p_225623_4_.scale(dripMode.xz.getVal(), dripMode.y.getVal(), dripMode.xz.getVal());
            } else if (dripMode.mod.getCurent().equals("AutoDrip")) {
                float s = 0.03f;
                dripMode.anim.setSpeed(s);
                dripMode.anim2.setSpeed(s);
                float drip = dripMode.anim.getAnim();
                float drip2 = dripMode.anim2.getAnim();
                p_225623_4_.scale(drip, drip2, drip);
            }

        }


        float f2 = f1 - f;
        if (shouldSit && p_225623_1_.getVehicle() instanceof LivingEntity) {
            LivingEntity livingentity = (LivingEntity) p_225623_1_.getVehicle();
            f = MathHelper.rotLerp(p_225623_3_, livingentity.yBodyRotO, livingentity.yBodyRot);
            f2 = f1 - f;
            float f3 = MathHelper.wrapDegrees(f2);
            if (f3 < -85.0F) {
                f3 = -85.0F;
            }

            if (f3 >= 85.0F) {
                f3 = 85.0F;
            }

            f = f1 - f3;
            if (f3 * f3 > 2500.0F) {
                f += f3 * 0.2F;
            }

            f2 = f1 - f;
        }
        float f6;
        if (p_225623_1_ instanceof ClientPlayerEntityHook) {
            f6 = MathHelper.lerp(p_225623_3_, p_225623_1_.xRotO, ((ClientPlayerEntityHook) p_225623_1_).exent.getxRot());
        } else {
            f6 = MathHelper.lerp(p_225623_3_, p_225623_1_.xRotO, p_225623_1_.xRot);
        }

        if (p_225623_1_.getPose() == Pose.SLEEPING) {
            Direction direction = p_225623_1_.getBedOrientation();
            if (direction != null) {
                float f4 = p_225623_1_.getEyeHeight(Pose.STANDING) - 0.1F;
                p_225623_4_.translate((float) (-direction.getStepX()) * f4, 0.0D, (float) (-direction.getStepZ()) * f4);
            }
        }

        float f7 = this.getBob(p_225623_1_, p_225623_3_);
        this.setupRotations(p_225623_1_, p_225623_4_, f7, f, p_225623_3_);
        p_225623_4_.scale(-1.0F, -1.0F, 1.0F);
        this.scale(p_225623_1_, p_225623_4_, p_225623_3_);
        p_225623_4_.translate(0.0D, (double) -1.501F, 0.0D);
        float f8 = 0.0F;
        float f5 = 0.0F;
        if (!shouldSit && p_225623_1_.isAlive()) {
            f8 = MathHelper.lerp(p_225623_3_, p_225623_1_.animationSpeedOld, p_225623_1_.animationSpeed);
            f5 = p_225623_1_.animationPosition - p_225623_1_.animationSpeed * (1.0F - p_225623_3_);
            if (p_225623_1_.isBaby()) {
                f5 *= 3.0F;
            }

            if (f8 > 1.0F) {
                f8 = 1.0F;
            }
        }

        this.model.prepareMobModel(p_225623_1_, f5, f8, p_225623_3_);
        this.model.setupAnim(p_225623_1_, f5, f8, f7, f2, f6);
        Minecraft minecraft = Minecraft.getInstance();
        boolean flag = this.isBodyVisible(p_225623_1_);
        boolean flag1 = !flag && !p_225623_1_.isInvisibleTo(minecraft.player);
        boolean flag2 = minecraft.shouldEntityAppearGlowing(p_225623_1_);
        RenderType rendertype = this.getRenderType(p_225623_1_, flag, flag1, flag2);
        if (rendertype != null) {
            IVertexBuilder ivertexbuilder = p_225623_5_.getBuffer(rendertype);
            int i = getOverlayCoords(p_225623_1_, this.getWhiteOverlayProgress(p_225623_1_, p_225623_3_));
            this.model.renderToBuffer(p_225623_4_, ivertexbuilder, p_225623_6_, i, 1.0F, 1.0F, 1.0F, flag1 ? 0.15F : 1.0F);
        }

        if (!p_225623_1_.isSpectator()) {
            for (LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> layerrenderer : this.layers) {
                layerrenderer.render(p_225623_4_, p_225623_5_, p_225623_6_, p_225623_1_, f5, f8, p_225623_3_, f7, f2, f6);
            }
        }

        p_225623_4_.popPose();
        net.minecraftforge.client.event.RenderNameplateEvent renderNameplateEvent = new net.minecraftforge.client.event.RenderNameplateEvent(p_225623_1_, p_225623_1_.getDisplayName(), this, p_225623_4_, p_225623_5_, p_225623_6_, p_225623_3_);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(renderNameplateEvent);
        if (renderNameplateEvent.getResult() != net.minecraftforge.eventbus.api.Event.Result.DENY && (renderNameplateEvent.getResult() == net.minecraftforge.eventbus.api.Event.Result.ALLOW || this.shouldShowName(p_225623_1_))) {
            if (!getInstance.getRegisterModule().isEnable(NameTag2.class)) {
                this.renderNameTag(p_225623_1_, renderNameplateEvent.getContent(), p_225623_4_, p_225623_5_, p_225623_6_);
            }
        }
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderLivingEvent.Post<>(p_225623_1_, this, p_225623_3_, p_225623_4_, p_225623_5_, p_225623_6_));
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderPlayerEvent.Post(p_225623_1_, this, p_225623_3_, p_225623_4_, p_225623_5_, p_225623_6_));

    }

    private void setModelProperties(AbstractClientPlayerEntity p_177137_1_) {
        PlayerModel<AbstractClientPlayerEntity> playermodel = this.getModel();
        if (p_177137_1_.isSpectator()) {
            playermodel.setAllVisible(false);
            playermodel.head.visible = true;
            playermodel.hat.visible = true;
        } else {
            playermodel.setAllVisible(true);
            playermodel.hat.visible = p_177137_1_.isModelPartShown(PlayerModelPart.HAT);
            playermodel.jacket.visible = p_177137_1_.isModelPartShown(PlayerModelPart.JACKET);
            playermodel.leftPants.visible = p_177137_1_.isModelPartShown(PlayerModelPart.LEFT_PANTS_LEG);
            playermodel.rightPants.visible = p_177137_1_.isModelPartShown(PlayerModelPart.RIGHT_PANTS_LEG);
            playermodel.leftSleeve.visible = p_177137_1_.isModelPartShown(PlayerModelPart.LEFT_SLEEVE);
            playermodel.rightSleeve.visible = p_177137_1_.isModelPartShown(PlayerModelPart.RIGHT_SLEEVE);
            playermodel.crouching = p_177137_1_.isCrouching();
            BipedModel.ArmPose bipedmodel$armpose = getArmPose(p_177137_1_, Hand.MAIN_HAND);
            BipedModel.ArmPose bipedmodel$armpose1 = getArmPose(p_177137_1_, Hand.OFF_HAND);
            if (bipedmodel$armpose.isTwoHanded()) {
                bipedmodel$armpose1 = p_177137_1_.getOffhandItem().isEmpty() ? BipedModel.ArmPose.EMPTY : BipedModel.ArmPose.ITEM;
            }

            if (p_177137_1_.getMainArm() == HandSide.RIGHT) {
                playermodel.rightArmPose = bipedmodel$armpose;
                playermodel.leftArmPose = bipedmodel$armpose1;
            } else {
                playermodel.rightArmPose = bipedmodel$armpose1;
                playermodel.leftArmPose = bipedmodel$armpose;
            }
        }

    }

    private static BipedModel.ArmPose getArmPose(AbstractClientPlayerEntity p_241741_0_, Hand p_241741_1_) {
        ItemStack itemstack = p_241741_0_.getItemInHand(p_241741_1_);
        if (itemstack.isEmpty()) {
            return BipedModel.ArmPose.EMPTY;
        } else {
            if (p_241741_0_.getUsedItemHand() == p_241741_1_ && p_241741_0_.getUseItemRemainingTicks() > 0) {
                UseAction useaction = itemstack.getUseAnimation();
                if (useaction == UseAction.BLOCK) {
                    return BipedModel.ArmPose.BLOCK;
                }

                if (useaction == UseAction.BOW) {
                    return BipedModel.ArmPose.BOW_AND_ARROW;
                }

                if (useaction == UseAction.SPEAR) {
                    return BipedModel.ArmPose.THROW_SPEAR;
                }

                if (useaction == UseAction.CROSSBOW && p_241741_1_ == p_241741_0_.getUsedItemHand()) {
                    return BipedModel.ArmPose.CROSSBOW_CHARGE;
                }
            } else if (!p_241741_0_.swinging && itemstack.getItem() == Items.CROSSBOW && CrossbowItem.isCharged(itemstack)) {
                return BipedModel.ArmPose.CROSSBOW_HOLD;
            }

            return BipedModel.ArmPose.ITEM;
        }
    }
}
