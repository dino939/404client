package com.denger.client.another.hooks;


import com.denger.client.Main;
import com.denger.client.another.hooks.forge.even.addevents.Event2D;
import com.denger.client.modules.mods.render.SwingAnimation;
import com.denger.client.modules.mods.render.ViewModel;
import com.denger.client.utils.anims.Animation;
import com.google.common.base.MoreObjects;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.FirstPersonRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.denger.client.Main.getInstance;
import static com.denger.client.Main.mc;

@OnlyIn(Dist.CLIENT)
public class FirstPersonRendererHook extends FirstPersonRenderer {
    private static final RenderType MAP_BACKGROUND = RenderType.text(new ResourceLocation("textures/map/map_background.png"));
    private static final RenderType MAP_BACKGROUND_CHECKERBOARD = RenderType.text(new ResourceLocation("textures/map/map_background_checkerboard.png"));
    private ItemStack mainHandItem = ItemStack.EMPTY;
    private ItemStack offHandItem = ItemStack.EMPTY;
    private float mainHandHeight;
    private float oMainHandHeight;
    private float offHandHeight;
    private float oOffHandHeight;
    private final EntityRendererManager entityRenderDispatcher = mc.getEntityRenderDispatcher();
    private final ItemRenderer itemRenderer = mc.getItemRenderer();
    Animation animationUtil = new Animation(0,0,0.1f);

    public FirstPersonRendererHook() {
        super(mc);
        Main.eventManager.register(this);
    }

    public void renderItem(LivingEntity p_228397_1_, ItemStack p_228397_2_, ItemCameraTransforms.TransformType p_228397_3_, boolean p_228397_4_, MatrixStack p_228397_5_, IRenderTypeBuffer p_228397_6_, int p_228397_7_) {
        if (!p_228397_2_.isEmpty()) {
            itemRenderer.renderStatic(p_228397_1_, p_228397_2_, p_228397_3_, p_228397_4_, p_228397_5_, p_228397_6_, p_228397_1_.level, p_228397_7_, OverlayTexture.NO_OVERLAY);
        }
    }
    @SubscribeEvent
    public void on2DEvent(Event2D e){

    }
    private float calculateMapTilt(float p_178100_1_) {
        float f = 1 - p_178100_1_ / 45 + 0.1f;
        f = MathHelper.clamp(f, 0, 1);
        return -MathHelper.cos(f * (float) Math.PI) * 0.5f + 0.5f;
    }

    private void renderMapHand(MatrixStack p_228403_1_, IRenderTypeBuffer p_228403_2_, int p_228403_3_, HandSide p_228403_4_) {
        mc.getTextureManager().bind(mc.player.getSkinTextureLocation());
        PlayerRenderer playerrenderer = (PlayerRenderer) entityRenderDispatcher.<AbstractClientPlayerEntity>getRenderer(mc.player);
        p_228403_1_.pushPose();
        float f = p_228403_4_ == HandSide.RIGHT ? 1 : -1;
        p_228403_1_.mulPose(Vector3f.YP.rotationDegrees(92));
        p_228403_1_.mulPose(Vector3f.XP.rotationDegrees(45));
        p_228403_1_.mulPose(Vector3f.ZP.rotationDegrees(f * -41));
        p_228403_1_.translate(f * 0.3, -1.1, 0.45);
        if (p_228403_4_ == HandSide.RIGHT) {
            playerrenderer.renderRightHand(p_228403_1_, p_228403_2_, p_228403_3_, mc.player);
        } else {
            playerrenderer.renderLeftHand(p_228403_1_, p_228403_2_, p_228403_3_, mc.player);
        }
        p_228403_1_.popPose();
    }

    private void renderOneHandedMap(MatrixStack p_228402_1_, IRenderTypeBuffer p_228402_2_, int p_228402_3_, float p_228402_4_, HandSide p_228402_5_, float p_228402_6_, ItemStack p_228402_7_) {
        float f = p_228402_5_ == HandSide.RIGHT ? 1 : -1;
        p_228402_1_.translate(f * 0.125, -0.125, 0);
        if (!mc.player.isInvisible()) {
            p_228402_1_.pushPose();
            p_228402_1_.mulPose(Vector3f.ZP.rotationDegrees(f * 10));
            renderPlayerArm(p_228402_1_, p_228402_2_, p_228402_3_, p_228402_4_, p_228402_6_, p_228402_5_);
            p_228402_1_.popPose();
        }
        p_228402_1_.pushPose();
        p_228402_1_.translate(f * 0.51, -0.08 + p_228402_4_ * -1.2, -0.75);
        float f1 = MathHelper.sqrt(p_228402_6_);
        float f2 = MathHelper.sin(f1 * (float) Math.PI);
        float f3 = -0.5f * f2;
        float f4 = 0.4f * MathHelper.sin(f1 * ((float) Math.PI * 2));
        float f5 = -0.3f * MathHelper.sin(p_228402_6_ * (float) Math.PI);
        p_228402_1_.translate(f * f3, f4 - 0.3 * f2, f5);
        p_228402_1_.mulPose(Vector3f.XP.rotationDegrees(f2 * -45));
        p_228402_1_.mulPose(Vector3f.YP.rotationDegrees(f * f2 * -30));
        renderMap(p_228402_1_, p_228402_2_, p_228402_3_, p_228402_7_);
        p_228402_1_.popPose();
    }

    private void renderTwoHandedMap(MatrixStack p_228400_1_, IRenderTypeBuffer p_228400_2_, int p_228400_3_, float p_228400_4_, float p_228400_5_, float p_228400_6_) {
        float f = MathHelper.sqrt(p_228400_6_);
        float f1 = -0.2f * MathHelper.sin(p_228400_6_ * (float) Math.PI);
        float f2 = -0.4f * MathHelper.sin(f * (float) Math.PI);
        p_228400_1_.translate(0, -f1 / 2, f2);
        float f3 = calculateMapTilt(p_228400_4_);
        p_228400_1_.translate(0, 0.04 + p_228400_5_ * -1.2 + f3 * -0.5, -0.72);
        p_228400_1_.mulPose(Vector3f.XP.rotationDegrees(f3 * -85));
        if (!mc.player.isInvisible()) {
            p_228400_1_.pushPose();
            p_228400_1_.mulPose(Vector3f.YP.rotationDegrees(90));
            renderMapHand(p_228400_1_, p_228400_2_, p_228400_3_, HandSide.RIGHT);
            renderMapHand(p_228400_1_, p_228400_2_, p_228400_3_, HandSide.LEFT);
            p_228400_1_.popPose();
        }
        float f4 = MathHelper.sin(f * (float) Math.PI);
        p_228400_1_.mulPose(Vector3f.XP.rotationDegrees(f4 * 20));
        p_228400_1_.scale(2, 2, 2);
        renderMap(p_228400_1_, p_228400_2_, p_228400_3_, mainHandItem);
    }

    private void renderMap(MatrixStack p_228404_1_, IRenderTypeBuffer p_228404_2_, int p_228404_3_, ItemStack p_228404_4_) {
        p_228404_1_.mulPose(Vector3f.YP.rotationDegrees(180));
        p_228404_1_.mulPose(Vector3f.ZP.rotationDegrees(180));
        p_228404_1_.scale(0.38f, 0.38f, 0.38f);
        p_228404_1_.translate(-0.5, -0.5, 0);
        p_228404_1_.scale(0.0078125f, 0.0078125f, 0.0078125f);
        MapData mapdata = FilledMapItem.getOrCreateSavedData(p_228404_4_, mc.level);
        IVertexBuilder ivertexbuilder = p_228404_2_.getBuffer(mapdata == null ? MAP_BACKGROUND : MAP_BACKGROUND_CHECKERBOARD);
        Matrix4f matrix4f = p_228404_1_.last().pose();
        ivertexbuilder.vertex(matrix4f, -7, 135, 0).color(255, 255, 255, 255).uv(0, 1).uv2(p_228404_3_).endVertex();
        ivertexbuilder.vertex(matrix4f, 135, 135, 0).color(255, 255, 255, 255).uv(1, 1).uv2(p_228404_3_).endVertex();
        ivertexbuilder.vertex(matrix4f, 135, -7, 0).color(255, 255, 255, 255).uv(1, 0).uv2(p_228404_3_).endVertex();
        ivertexbuilder.vertex(matrix4f, -7, -7, 0).color(255, 255, 255, 255).uv(0, 0).uv2(p_228404_3_).endVertex();
        if (mapdata != null) {
            mc.gameRenderer.getMapRenderer().render(p_228404_1_, p_228404_2_, mapdata, false, p_228404_3_);
        }
    }

    private void renderPlayerArm(MatrixStack p_228401_1_, IRenderTypeBuffer p_228401_2_, int p_228401_3_, float p_228401_4_, float p_228401_5_, HandSide p_228401_6_) {
        boolean flag = p_228401_6_ != HandSide.LEFT;
        float f = flag ? 1 : -1;
        float f1 = MathHelper.sqrt(p_228401_5_);
        float f2 = -0.3f * MathHelper.sin(f1 * (float) Math.PI);
        float f3 = 0.4f * MathHelper.sin(f1 * ((float) Math.PI * 2F));
        float f4 = -0.4f * MathHelper.sin(p_228401_5_ * (float) Math.PI);
        p_228401_1_.translate(f * (f2 + 0.64000005), f3 - 0.6 + p_228401_4_ * -0.6, f4 - 0.71999997);
        p_228401_1_.mulPose(Vector3f.YP.rotationDegrees(f * 45));
        float f5 = MathHelper.sin(p_228401_5_ * p_228401_5_ * (float) Math.PI);
        float f6 = MathHelper.sin(f1 * (float) Math.PI);
        p_228401_1_.mulPose(Vector3f.YP.rotationDegrees(f * f6 * 70));
        p_228401_1_.mulPose(Vector3f.ZP.rotationDegrees(f * f5 * -20));
        AbstractClientPlayerEntity abstractclientplayerentity = mc.player;
        mc.getTextureManager().bind(abstractclientplayerentity.getSkinTextureLocation());
        p_228401_1_.translate(f * -1, 3.6, 3.5);
        p_228401_1_.mulPose(Vector3f.ZP.rotationDegrees(f * 120));
        p_228401_1_.mulPose(Vector3f.XP.rotationDegrees(200));
        p_228401_1_.mulPose(Vector3f.YP.rotationDegrees(f * -135));
        p_228401_1_.translate(f * 5.6, 0, 0);
        PlayerRenderer playerrenderer = (PlayerRenderer) entityRenderDispatcher.getRenderer(abstractclientplayerentity);
        if (flag) {
            playerrenderer.renderRightHand(p_228401_1_, p_228401_2_, p_228401_3_, abstractclientplayerentity);
        } else {
            playerrenderer.renderLeftHand(p_228401_1_, p_228401_2_, p_228401_3_, abstractclientplayerentity);
        }
    }

    private void applyEatTransform(MatrixStack p_228398_1_, float p_228398_2_, HandSide p_228398_3_, ItemStack p_228398_4_) {
        float f = (float) mc.player.getUseItemRemainingTicks() - p_228398_2_ + 1;
        float f1 = f / (float) p_228398_4_.getUseDuration();
        if (f1 < 0.8) {
            float f2 = MathHelper.abs(MathHelper.cos(f / 4 * (float) Math.PI) * 0.1f);
            p_228398_1_.translate(0, f2, 0);
        }

        float f3 = 1 - (float) Math.pow(f1, 27);
        int i = p_228398_3_ == HandSide.RIGHT ? 1 : -1;
        p_228398_1_.translate(f3 * 0.6 * (float) i, f3 * -0.5, f3 * 0);
        p_228398_1_.mulPose(Vector3f.YP.rotationDegrees((float) i * f3 * 90));
        p_228398_1_.mulPose(Vector3f.XP.rotationDegrees(f3 * 10));
        p_228398_1_.mulPose(Vector3f.ZP.rotationDegrees((float) i * f3 * 30));
    }

    private void applyItemArmAttackTransform(MatrixStack p_228399_1_, HandSide p_228399_2_, float p_228399_3_) {
        int i = p_228399_2_ == HandSide.RIGHT ? 1 : -1;
        float f = MathHelper.sin(p_228399_3_ * p_228399_3_ * (float) Math.PI);
        p_228399_1_.mulPose(Vector3f.YP.rotationDegrees((float) i * (45 + f * -20)));
        float f1 = MathHelper.sin(MathHelper.sqrt(p_228399_3_) * (float) Math.PI);
        p_228399_1_.mulPose(Vector3f.ZP.rotationDegrees((float) i * f1 * -20));
        p_228399_1_.mulPose(Vector3f.XP.rotationDegrees(f1 * -80));
        p_228399_1_.mulPose(Vector3f.YP.rotationDegrees((float) i * -45));
    }

    private void applyItemArmTransform(MatrixStack p_228406_1_, HandSide p_228406_2_, float p_228406_3_) {
        int i = p_228406_2_ == HandSide.RIGHT ? 1 : -1;
        p_228406_1_.translate((float) i * 0.56, -0.52 + (getInstance.getRegisterModule().isEnable(SwingAnimation.class) ? 0 : p_228406_3_ * -0.6), -0.72);
    }

    public void renderHandsWithItems(float p_228396_1_, MatrixStack p_228396_2_, IRenderTypeBuffer.Impl p_228396_3_, ClientPlayerEntity p_228396_4_, int p_228396_5_) {
        float f = p_228396_4_.getAttackAnim(p_228396_1_);
        Hand hand = MoreObjects.firstNonNull(p_228396_4_.swingingArm, Hand.MAIN_HAND);
        float f1 = MathHelper.lerp(p_228396_1_, p_228396_4_.xRotO, p_228396_4_.xRot);
        boolean flag = true;
        boolean flag1 = true;
        if (p_228396_4_.isUsingItem()) {
            ItemStack itemstack = p_228396_4_.getUseItem();
            if (itemstack.getItem() instanceof ShootableItem) {
                flag = p_228396_4_.getUsedItemHand() == Hand.MAIN_HAND;
                flag1 = !flag;
            }
            Hand hand1 = p_228396_4_.getUsedItemHand();
            if (hand1 == Hand.MAIN_HAND) {
                ItemStack itemstack1 = p_228396_4_.getOffhandItem();
                if (itemstack1.getItem() == Items.CROSSBOW && CrossbowItem.isCharged(itemstack1)) {
                    flag1 = false;
                }
            }
        } else {
            ItemStack itemstack2 = p_228396_4_.getMainHandItem();
            ItemStack itemstack3 = p_228396_4_.getOffhandItem();
            if (itemstack2.getItem() == Items.CROSSBOW && CrossbowItem.isCharged(itemstack2)) {
                flag1 = false;
            }

            if (itemstack3.getItem() == Items.CROSSBOW && CrossbowItem.isCharged(itemstack3)) {
                flag = !itemstack2.isEmpty();
                flag1 = !flag;
            }
        }
        float f3 = MathHelper.lerp(p_228396_1_, p_228396_4_.xBobO, p_228396_4_.xBob);
        float f4 = MathHelper.lerp(p_228396_1_, p_228396_4_.yBobO, p_228396_4_.yBob);
        p_228396_2_.mulPose(Vector3f.XP.rotationDegrees((p_228396_4_.getViewXRot(p_228396_1_) - f3) * 0.1f));
        p_228396_2_.mulPose(Vector3f.YP.rotationDegrees((p_228396_4_.getViewYRot(p_228396_1_) - f4) * 0.1f));
        if (flag) {
            float f5 = hand == Hand.MAIN_HAND ? f : 0;
            float f2 = 1 - MathHelper.lerp(p_228396_1_, oMainHandHeight, mainHandHeight);
            p_228396_2_.pushPose();
            if (getInstance.getRegisterModule().isEnable(ViewModel.class)) {
                p_228396_2_.translate(ViewModel.rightX.getVal(), ViewModel.rightY.getVal(), ViewModel.rightZ.getVal());
            }
            if (!ForgeHooksClient.renderSpecificFirstPersonHand(Hand.MAIN_HAND, p_228396_2_, p_228396_3_, p_228396_5_, p_228396_1_, f1, f5, f2, mainHandItem)) {
                renderArmWithItem(p_228396_4_, p_228396_1_, f1, Hand.MAIN_HAND, f5, mainHandItem, f2, p_228396_2_, p_228396_3_, p_228396_5_);
            }
            p_228396_2_.popPose();
        }
        if (flag1) {
            float f6 = hand == Hand.OFF_HAND ? f : 0;
            float f7 = 1 - MathHelper.lerp(p_228396_1_, oOffHandHeight, offHandHeight);
            p_228396_2_.pushPose();
            if (getInstance.getRegisterModule().isEnable(ViewModel.class)) {
                p_228396_2_.translate(ViewModel.leftX.getVal(), ViewModel.leftY.getVal(), ViewModel.leftZ.getVal());
            }
            if (!ForgeHooksClient.renderSpecificFirstPersonHand(Hand.OFF_HAND, p_228396_2_, p_228396_3_, p_228396_5_, p_228396_1_, f1, f6, f7, offHandItem)) {
                renderArmWithItem(p_228396_4_, p_228396_1_, f1, Hand.OFF_HAND, f6, offHandItem, f7, p_228396_2_, p_228396_3_, p_228396_5_);
            }
            p_228396_2_.popPose();
        }
        p_228396_3_.endBatch();
    }

    private void renderArmWithItem(AbstractClientPlayerEntity p_228405_1_, float p_228405_2_, float p_228405_3_, Hand p_228405_4_, float p_228405_5_, ItemStack p_228405_6_, float p_228405_7_, MatrixStack p_228405_8_, IRenderTypeBuffer p_228405_9_, int p_228405_10_) {

        boolean flag = p_228405_4_ == Hand.MAIN_HAND;
        HandSide handside = flag ? p_228405_1_.getMainArm() : p_228405_1_.getMainArm().getOpposite();
        p_228405_8_.pushPose();
        if (p_228405_6_.isEmpty()) {
            if (flag && !p_228405_1_.isInvisible()) {
                renderPlayerArm(p_228405_8_, p_228405_9_, p_228405_10_, p_228405_7_, p_228405_5_, handside);
            }
        } else if (p_228405_6_.getItem() == Items.FILLED_MAP) {
            if (flag && offHandItem.isEmpty()) {
                renderTwoHandedMap(p_228405_8_, p_228405_9_, p_228405_10_, p_228405_3_, p_228405_7_, p_228405_5_);
            } else {
                renderOneHandedMap(p_228405_8_, p_228405_9_, p_228405_10_, p_228405_7_, handside, p_228405_5_, p_228405_6_);
            }
        } else if (p_228405_6_.getItem() == Items.CROSSBOW) {
            boolean flag1 = CrossbowItem.isCharged(p_228405_6_);
            boolean flag2 = handside == HandSide.RIGHT;
            int i = flag2 ? 1 : -1;
            if (p_228405_1_.isUsingItem() && p_228405_1_.getUseItemRemainingTicks() > 0 && p_228405_1_.getUsedItemHand() == p_228405_4_) {
                applyItemArmTransform(p_228405_8_, handside, p_228405_7_);
                p_228405_8_.translate((float) i * -0.4785682, -0.094387, 0.05731531);
                p_228405_8_.mulPose(Vector3f.XP.rotationDegrees(-11.935f));
                p_228405_8_.mulPose(Vector3f.YP.rotationDegrees((float) i * 65.3f));
                p_228405_8_.mulPose(Vector3f.ZP.rotationDegrees((float) i * -9.785f));
                float f9 = (float) p_228405_6_.getUseDuration() - ((float) mc.player.getUseItemRemainingTicks() - p_228405_2_ + 1);
                float f13 = f9 / (float) CrossbowItem.getChargeDuration(p_228405_6_);
                if (f13 > 1) {
                    f13 = 1;
                }

                if (f13 > 0.1) {
                    float f16 = MathHelper.sin((f9 - 0.1f) * 1.3f);
                    float f3 = f13 - 0.1f;
                    float f4 = f16 * f3;
                    p_228405_8_.translate(f4 * 0, f4 * 0.004, f4 * 0);
                }
                p_228405_8_.translate(f13 * 0, f13 * 0, f13 * 0.04);
                p_228405_8_.scale(1, 1, 1 + f13 * 0.2f);
                p_228405_8_.mulPose(Vector3f.YN.rotationDegrees((float) i * 45));
            } else {
                float f = -0.4f * MathHelper.sin(MathHelper.sqrt(p_228405_5_) * (float) Math.PI);
                float f1 = 0.2f * MathHelper.sin(MathHelper.sqrt(p_228405_5_) * ((float) Math.PI * 2));
                float f2 = -0.2f * MathHelper.sin(p_228405_5_ * (float) Math.PI);
                p_228405_8_.translate((float) i * f, f1, f2);
                applyItemArmTransform(p_228405_8_, handside, p_228405_7_);
                applyItemArmAttackTransform(p_228405_8_, handside, p_228405_5_);
                if (flag1 && p_228405_5_ < 0.001) {
                    p_228405_8_.translate((float) i * -0.641864, 0, 0);
                    p_228405_8_.mulPose(Vector3f.YP.rotationDegrees((float) i * 10));
                }
            }
            renderItem(p_228405_1_, p_228405_6_, flag2 ? ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND : ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, !flag2, p_228405_8_, p_228405_9_, p_228405_10_);
        } else {
            boolean flag3 = handside == HandSide.RIGHT;
            if (p_228405_1_.isUsingItem() && p_228405_1_.getUseItemRemainingTicks() > 0 && p_228405_1_.getUsedItemHand() == p_228405_4_) {
                int k = flag3 ? 1 : -1;
                switch (p_228405_6_.getUseAnimation()) {
                    case NONE:
                    case BLOCK:
                        applyItemArmTransform(p_228405_8_, handside, p_228405_7_);
                        break;
                    case EAT:
                    case DRINK:
                        applyEatTransform(p_228405_8_, p_228405_2_, handside, p_228405_6_);
                        applyItemArmTransform(p_228405_8_, handside, p_228405_7_);
                        break;
                    case BOW:
                        applyItemArmTransform(p_228405_8_, handside, p_228405_7_);
                        p_228405_8_.translate((float) k * -0.2785682, 0.18344387, 0.15731531);
                        p_228405_8_.mulPose(Vector3f.XP.rotationDegrees(-13.935f));
                        p_228405_8_.mulPose(Vector3f.YP.rotationDegrees((float) k * 35.3f));
                        p_228405_8_.mulPose(Vector3f.ZP.rotationDegrees((float) k * -9.785f));
                        float f8 = (float) p_228405_6_.getUseDuration() - ((float) mc.player.getUseItemRemainingTicks() - p_228405_2_ + 1);
                        float f12 = f8 / 20;
                        f12 = (f12 * f12 + f12 * 2) / 3;
                        if (f12 > 1) {
                            f12 = 1;
                        }
                        if (f12 > 0.1) {
                            float f15 = MathHelper.sin((f8 - 0.1f) * 1.3f);
                            float f18 = f12 - 0.1f;
                            float f20 = f15 * f18;
                            p_228405_8_.translate(f20 * 0, f20 * 0.004, f20 * 0);
                        }

                        p_228405_8_.translate(f12 * 0, f12 * 0, f12 * 0.04);
                        p_228405_8_.scale(1, 1, 1 + f12 * 0.2F);
                        p_228405_8_.mulPose(Vector3f.YN.rotationDegrees((float) k * 45));
                        break;
                    case SPEAR:
                        applyItemArmTransform(p_228405_8_, handside, p_228405_7_);
                        p_228405_8_.translate((float) k * -0.5, 0.7, 0.1);
                        p_228405_8_.mulPose(Vector3f.XP.rotationDegrees(-55));
                        p_228405_8_.mulPose(Vector3f.YP.rotationDegrees((float) k * 35.3f));
                        p_228405_8_.mulPose(Vector3f.ZP.rotationDegrees((float) k * -9.785f));
                        float f7 = (float) p_228405_6_.getUseDuration() - ((float) mc.player.getUseItemRemainingTicks() - p_228405_2_ + 1);
                        float f11 = f7 / 10;
                        if (f11 > 1) {
                            f11 = 1;
                        }
                        if (f11 > 0.1) {
                            float f14 = MathHelper.sin((f7 - 0.1f) * 1.3f);
                            float f17 = f11 - 0.1f;
                            float f19 = f14 * f17;
                            p_228405_8_.translate(f19 * 0, f19 * 0.004, f19 * 0);
                        }
                        p_228405_8_.translate(0, 0, f11 * 0.2);
                        p_228405_8_.scale(1, 1, 1 + f11 * 0.2f);
                        p_228405_8_.mulPose(Vector3f.YN.rotationDegrees((float) k * 45));
                }
            } else if (p_228405_1_.isAutoSpinAttack()) {
                applyItemArmTransform(p_228405_8_, handside, p_228405_7_);
                int j = flag3 ? 1 : -1;
                p_228405_8_.translate((float) j * -0.4, 0.8, 0.3);
                p_228405_8_.mulPose(Vector3f.YP.rotationDegrees((float) j * 65));
                p_228405_8_.mulPose(Vector3f.ZP.rotationDegrees((float) j * -85));
            } else {
                float f5 = -0.4f * MathHelper.sin(MathHelper.sqrt(p_228405_5_) * (float) Math.PI);
                float f6 = 0.2f * MathHelper.sin(MathHelper.sqrt(p_228405_5_) * ((float) Math.PI * 2));
                float f10 = -0.2f * MathHelper.sin(p_228405_5_ * (float) Math.PI);
                int l = flag3 ? 1 : -1;
                animationUtil.speed = SwingAnimation.plavnost.getVal();
                animationUtil.to = MathHelper.sin((float) (p_228405_5_ * Math.PI));
                if (!getInstance.getRegisterModule().isEnable(SwingAnimation.class)) {
                    p_228405_8_.translate((float) l * f5, f6, f10);
                }
                applyItemArmTransform(p_228405_8_, handside, p_228405_7_);
                if (!getInstance.getRegisterModule().isEnable(SwingAnimation.class) || handside == HandSide.LEFT) {
                    applyItemArmAttackTransform(p_228405_8_, handside, p_228405_5_);
                } else if (SwingAnimation.modeSetting.getCurent().equals("Короткая")) {
                    int i = handside == HandSide.RIGHT ? 1 : -1;
                    p_228405_8_.mulPose(Vector3f.YP.rotationDegrees((float) i * (45 + MathHelper.sin(p_228405_5_ * p_228405_5_ * (float) Math.PI) * -20)));
                    float f1 = MathHelper.sin(MathHelper.sqrt(p_228405_5_) * (float) Math.PI);
                    p_228405_8_.mulPose(Vector3f.ZP.rotationDegrees((float) i * f1 * -20));
                    p_228405_8_.mulPose(Vector3f.XP.rotationDegrees(f1 * -SwingAnimation.power.getVal()));
                    p_228405_8_.mulPose(Vector3f.YP.rotationDegrees((float) i * -45));
                } else if (SwingAnimation.modeSetting.getCurent().equals("Простая")) {
                    p_228405_8_.translate(0, 0.1, 0);
                    p_228405_8_.mulPose(Vector3f.XP.rotationDegrees(animationUtil.getAnim() * -SwingAnimation.power.getVal()));
                    p_228405_8_.mulPose(Vector3f.ZP.rotationDegrees(animationUtil.getAnim() * 45));
                    p_228405_8_.mulPose(Vector3f.YP.rotationDegrees(animationUtil.getAnim() * 15));
                    p_228405_8_.translate(0, -0.1, 0);
                } else if (SwingAnimation.modeSetting.getCurent().equals("На себя")) {
                    p_228405_8_.mulPose(Vector3f.YP.rotationDegrees(90));
                    p_228405_8_.mulPose(Vector3f.ZP.rotationDegrees(-70));
                    p_228405_8_.mulPose(Vector3f.XP.rotationDegrees(-90 + -SwingAnimation.power.getVal() * animationUtil.getAnim()));
                } else if (SwingAnimation.modeSetting.getCurent().equals("От себя")) {
                    p_228405_8_.mulPose(Vector3f.YP.rotationDegrees(90));
                    p_228405_8_.mulPose(Vector3f.ZP.rotationDegrees(-70));
                    p_228405_8_.mulPose(Vector3f.XP.rotationDegrees(-90 + SwingAnimation.power.getVal() * animationUtil.getAnim()));
                } else if (SwingAnimation.modeSetting.getCurent().equals("Защита")) {
                    p_228405_8_.mulPose(Vector3f.YP.rotationDegrees(76));
                    p_228405_8_.mulPose(Vector3f.YP.rotationDegrees(animationUtil.getAnim() * -5));
                    p_228405_8_.mulPose(Vector3f.XN.rotationDegrees(animationUtil.getAnim() * SwingAnimation.power.getVal()));
                    p_228405_8_.mulPose(Vector3f.XP.rotationDegrees(-90));
                } else if (SwingAnimation.modeSetting.getCurent().equals("Размер")) {
                    p_228405_8_.scale(1, 1, animationUtil.getAnim() * 5 + 1);
                    p_228405_8_.mulPose(Vector3f.YP.rotationDegrees(60));
                    p_228405_8_.mulPose(Vector3f.XP.rotationDegrees(-90));
                    p_228405_8_.mulPose(Vector3f.ZP.rotationDegrees(-45));
                } else if (SwingAnimation.modeSetting.getCurent().equals("360")) {
                    p_228405_8_.mulPose(Vector3f.XP.rotationDegrees(p_228405_5_ * -360));
                } else {
                    p_228405_8_.translate(0.5, -0.1, 0);
                    p_228405_8_.mulPose(Vector3f.XP.rotationDegrees(animationUtil.getAnim() * -45));
                    p_228405_8_.translate(-0.5, 0.1, 0);
                    p_228405_8_.translate(0.5, -0.1, 0);
                    p_228405_8_.mulPose(Vector3f.YP.rotationDegrees(animationUtil.getAnim() * -20));
                    p_228405_8_.translate(-0.5, 0.1, 0);
                    p_228405_8_.mulPose(Vector3f.YP.rotationDegrees(50));
                    p_228405_8_.mulPose(Vector3f.XP.rotationDegrees(-90));
                    p_228405_8_.mulPose(Vector3f.YP.rotationDegrees(50));
                }
            }
            renderItem(p_228405_1_, p_228405_6_, flag3 ? ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND : ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, !flag3, p_228405_8_, p_228405_9_, p_228405_10_);
        }
        p_228405_8_.popPose();
    }

    public void tick() {
        oMainHandHeight = mainHandHeight;
        oOffHandHeight = offHandHeight;
        ClientPlayerEntity clientplayerentity = mc.player;
        ItemStack itemstack = clientplayerentity.getMainHandItem();
        ItemStack itemstack1 = clientplayerentity.getOffhandItem();
        if (ItemStack.matches(mainHandItem, itemstack)) {
            mainHandItem = itemstack;
        }
        if (ItemStack.matches(offHandItem, itemstack1)) {
            offHandItem = itemstack1;
        }
        if (clientplayerentity.isHandsBusy()) {
            mainHandHeight = MathHelper.clamp(mainHandHeight - 0, 0, 1);
            offHandHeight = MathHelper.clamp(offHandHeight - 0, 0, 1);
        } else {
            float f = clientplayerentity.getAttackStrengthScale(1);
            boolean requipM = ForgeHooksClient.shouldCauseReequipAnimation(mainHandItem, itemstack, clientplayerentity.inventory.selected);
            boolean requipO = ForgeHooksClient.shouldCauseReequipAnimation(offHandItem, itemstack1, -1);
            if (!requipM && mainHandItem != itemstack) {
                mainHandItem = itemstack;
            }
            if (!requipO && offHandItem != itemstack1) {
                offHandItem = itemstack1;
            }
            mainHandHeight += MathHelper.clamp((!requipM ? f * f * f : 0) - mainHandHeight, -0.4, 0.4);
            offHandHeight += MathHelper.clamp((float) (!requipO ? 1 : 0) - offHandHeight, -0.4, 0.4);
        }
        if (mainHandHeight < 0.1) {
            mainHandItem = itemstack;
        }
        if (offHandHeight < 0.1) {
            offHandItem = itemstack1;
        }
    }

    public void itemUsed(Hand p_187460_1_) {
        if (p_187460_1_ == Hand.MAIN_HAND) {
            mainHandHeight = 0;
        } else {
            offHandHeight = 0;
        }
    }
}
