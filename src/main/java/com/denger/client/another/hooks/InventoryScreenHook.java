package com.denger.client.another.hooks;

import com.denger.client.screens.main.MainScreen;
import com.denger.client.utils.rect.BlurUtil;
import com.denger.client.utils.rect.RectUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.DisplayEffectsScreen;
import net.minecraft.client.gui.recipebook.IRecipeShownListener;
import net.minecraft.client.gui.recipebook.RecipeBookGui;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.play.client.CCreativeInventoryActionPacket;
import com.denger.client.another.Themes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.denger.client.MainNative.*;


@OnlyIn(Dist.CLIENT)
public class InventoryScreenHook extends DisplayEffectsScreen<PlayerContainer> implements IRecipeShownListener {
    private static final ResourceLocation RECIPE_BUTTON_LOCATION = new ResourceLocation("textures/gui/recipe_button.png");
    private float xMouse;
    private float yMouse;
    private final RecipeBookGui recipeBookComponent = new RecipeBookGui();
    private boolean recipeBookComponentInitialized;
    private boolean widthTooNarrow;
    private boolean buttonClicked;

    public InventoryScreenHook() {
        super(mc.player.inventoryMenu, mc.player.inventory, new TranslationTextComponent("container.crafting"));
        passEvents = true;
        titleLabelX = 97;
    }

    public void tick() {
        if (minecraft.gameMode.hasInfiniteItems()) {
            minecraft.setScreen(new CreativeScreen(minecraft.player));
        } else {
            recipeBookComponent.tick();
        }
    }

    protected void init() {
        if (minecraft.gameMode.hasInfiniteItems()) {
            minecraft.setScreen(new CreativeScreen(minecraft.player));
        } else {
            super.init();
            widthTooNarrow = width < 379;
            recipeBookComponent.init(width, height, minecraft, widthTooNarrow, menu);
            recipeBookComponentInitialized = true;
            leftPos = recipeBookComponent.updateScreenPosition(widthTooNarrow, width, imageWidth);
            children.add(recipeBookComponent);
            setInitialFocus(recipeBookComponent);
            addButton(new ImageButton(leftPos + 104, height / 2 - 22, 20, 18, 0, 0, 19, RECIPE_BUTTON_LOCATION, (p_214086_1_) -> {
                recipeBookComponent.initVisuals(widthTooNarrow);
                recipeBookComponent.toggleVisibility();
                leftPos = recipeBookComponent.updateScreenPosition(widthTooNarrow, width, imageWidth);
                ((ImageButton) p_214086_1_).setPosition(leftPos + 104, height / 2 - 22);
                buttonClicked = true;
            }));
        }
    }

    protected void renderLabels(MatrixStack p_230451_1_, int p_230451_2_, int p_230451_3_) {
        font.draw(p_230451_1_, title, (float) titleLabelX, (float) titleLabelY, 4210752);
    }

    public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        renderBackground(p_230430_1_);
        doRenderEffects = !recipeBookComponent.isVisible();
        if (recipeBookComponent.isVisible() && widthTooNarrow) {
            renderBg(p_230430_1_, p_230430_4_, p_230430_2_, p_230430_3_);
            recipeBookComponent.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
        } else {
            recipeBookComponent.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
            super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
            recipeBookComponent.renderGhostRecipe(p_230430_1_, leftPos, topPos, false, p_230430_4_);
        }
        renderTooltip(p_230430_1_, p_230430_2_, p_230430_3_);
        recipeBookComponent.renderTooltip(p_230430_1_, leftPos, topPos, p_230430_2_, p_230430_3_);
        xMouse = (float) p_230430_2_;
        yMouse = (float) p_230430_3_;
        Themes themes = getInstance.theme;
        BlurUtil.registerRenderCall(()->{
            RectUtil.drawGradientRound(mc.getWindow().getGuiScaledWidth() / 2 - 50, mc.getWindow().getGuiScaledHeight() / 2 - 120, 100, 20, 10,themes.getColor(100),themes.getColor(200),themes.getColor(300),themes.getColor(400));

        });
        BlurUtil.draw(6);
        BlurUtil.drawBlur(6,()->{
            RectUtil.drawRound(mc.getWindow().getGuiScaledWidth() / 2 - 50, mc.getWindow().getGuiScaledHeight() / 2 - 120, 100, 20, 10,-1);
        });

         //RenderUtil.drawRound(mc.getWindow().getGuiScaledWidth() / 2 - 49, mc.getWindow().getGuiScaledHeight() / 2 - 119, 98, 18, 10, new Color(0, 0, 0, 165));
        fontManager.font16.drawCenteredString(p_230430_1_, "Выкинуть Все", mc.getWindow().getGuiScaledWidth() / 2, mc.getWindow().getGuiScaledHeight() / 2 - 116.5f, -1);
     }

    protected void renderBg(MatrixStack p_230450_1_, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
        RenderSystem.color4f(1, 1, 1, 1);
        minecraft.getTextureManager().bind(INVENTORY_LOCATION);
        int i = leftPos, j = topPos;
        blit(p_230450_1_, i, j, 0, 0, imageWidth, imageHeight);
        renderEntityInInventory(i + 51, j + 75, 30, (float) (i + 51) - xMouse, (float) (j + 75 - 50) - yMouse, minecraft.player);
    }

    public static void renderEntityInInventory(int p_228187_0_, int p_228187_1_, int p_228187_2_, float p_228187_3_, float p_228187_4_, LivingEntity p_228187_5_) {
        float f = (float) Math.atan(p_228187_3_ / 40);
        float f1 = (float) Math.atan(p_228187_4_ / 40);
        RenderSystem.pushMatrix();
        RenderSystem.translatef((float) p_228187_0_, (float) p_228187_1_, 1050);
        RenderSystem.scalef(1, 1, -1);
        MatrixStack matrixstack = new MatrixStack();
        matrixstack.translate(0, 0, 1000);
        matrixstack.scale((float) p_228187_2_, (float) p_228187_2_, (float) p_228187_2_);
        Quaternion quaternion = Vector3f.ZP.rotationDegrees(180);
        Quaternion quaternion1 = Vector3f.XP.rotationDegrees(f1 * 20);
        quaternion.mul(quaternion1);
        matrixstack.mulPose(quaternion);
        float f2 = p_228187_5_.yBodyRot, f3 = p_228187_5_.yRot, f4 = p_228187_5_.xRot, f5 = p_228187_5_.yHeadRotO, f6 = p_228187_5_.yHeadRot;
        p_228187_5_.yBodyRot = 180 + f * 20;
        p_228187_5_.yRot = 180 + f * 40;
        p_228187_5_.xRot = -f1 * 20;
        p_228187_5_.yHeadRot = p_228187_5_.yRot;
        p_228187_5_.yHeadRotO = p_228187_5_.yRot;
        EntityRendererManager entityrenderermanager = Minecraft.getInstance().getEntityRenderDispatcher();
        quaternion1.conj();
        entityrenderermanager.overrideCameraOrientation(quaternion1);
        entityrenderermanager.setRenderShadow(false);
        IRenderTypeBuffer.Impl irendertypebuffer$impl = Minecraft.getInstance().renderBuffers().bufferSource();
        RenderSystem.runAsFancy(() -> {
            entityrenderermanager.render(p_228187_5_, 0, 0, 0, 0, 1, matrixstack, irendertypebuffer$impl, 15728880);
        });
        irendertypebuffer$impl.endBatch();
        entityrenderermanager.setRenderShadow(true);
        p_228187_5_.yBodyRot = f2;
        p_228187_5_.yRot = f3;
        p_228187_5_.xRot = f4;
        p_228187_5_.yHeadRotO = f5;
        p_228187_5_.yHeadRot = f6;
        RenderSystem.popMatrix();
    }

    protected boolean isHovering(int p_195359_1_, int p_195359_2_, int p_195359_3_, int p_195359_4_, double p_195359_5_, double p_195359_7_) {
        return (!widthTooNarrow || !recipeBookComponent.isVisible()) && super.isHovering(p_195359_1_, p_195359_2_, p_195359_3_, p_195359_4_, p_195359_5_, p_195359_7_);
    }

    public boolean mouseClicked(double p_231044_1_, double p_231044_3_, int p_231044_5_) {
        if (MainScreen.ishover(mc.getWindow().getGuiScaledWidth() / 2 - 50, mc.getWindow().getGuiScaledHeight() / 2 - 120, 100, 20,p_231044_1_, p_231044_3_) && p_231044_5_ == 0) {
            for (int index = 0; index < mc.player.inventoryMenu.slots.size(); ++index) {
                mc.player.connection.send(new CCreativeInventoryActionPacket(index, mc.player.inventory.getItem(index)));
                mc.gameMode.handleInventoryMouseClick(0, index, 1, ClickType.THROW, mc.player);
            }
        }
        if (recipeBookComponent.mouseClicked(p_231044_1_, p_231044_3_, p_231044_5_)) {
            setFocused(recipeBookComponent);
            return true;
        } else {
            return (!widthTooNarrow || !recipeBookComponent.isVisible()) && super.mouseClicked(p_231044_1_, p_231044_3_, p_231044_5_);
        }
    }

    public boolean mouseReleased(double p_231048_1_, double p_231048_3_, int p_231048_5_) {
        if (buttonClicked) {
            buttonClicked = false;
            return true;
        } else {
            return super.mouseReleased(p_231048_1_, p_231048_3_, p_231048_5_);
        }
    }

    protected boolean hasClickedOutside(double p_195361_1_, double p_195361_3_, int p_195361_5_, int p_195361_6_, int p_195361_7_) {
        boolean flag = p_195361_1_ < (double) p_195361_5_ || p_195361_3_ < (double) p_195361_6_ || p_195361_1_ >= (double) (p_195361_5_ + imageWidth) || p_195361_3_ >= (double) (p_195361_6_ + imageHeight);
        return recipeBookComponent.hasClickedOutside(p_195361_1_, p_195361_3_, leftPos, topPos, imageWidth, imageHeight, p_195361_7_) && flag;
    }

    protected void slotClicked(Slot p_184098_1_, int p_184098_2_, int p_184098_3_, ClickType p_184098_4_) {
        super.slotClicked(p_184098_1_, p_184098_2_, p_184098_3_, p_184098_4_);
        recipeBookComponent.slotClicked(p_184098_1_);
    }

    public void recipesUpdated() {
        recipeBookComponent.recipesUpdated();
    }

    public void removed() {
        if (recipeBookComponentInitialized) {
            recipeBookComponent.removed();
        }
        super.removed();
    }

    public RecipeBookGui getRecipeBookComponent() {
        return recipeBookComponent;
    }
}
