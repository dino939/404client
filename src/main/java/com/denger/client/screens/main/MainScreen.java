package com.denger.client.screens.main;

import com.denger.client.another.resource.NativeManager;
import com.denger.client.modules.another.Category;
import com.denger.client.screens.main.comp.Button;
import com.denger.client.screens.main.comp.GuiColors;
import com.denger.client.screens.main.comp.Scene;
import com.denger.client.screens.main.comp.SettComp;
import com.denger.client.screens.main.scenes.ModuleScene;
import com.denger.client.utils.AnimationUtil;
import com.denger.client.utils.ColorUtil;
import com.denger.client.utils.rect.BlurUtil;
import com.denger.client.utils.rect.RectUtil;
import com.denger.client.utils.rect.RenderUtil;
import com.denger.client.utils.rect.StencilUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.IRenderCall;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

import static com.denger.client.MainNative.*;

public class MainScreen extends Screen {
    private float x, y, w, h, mx, my, sx, sy, scale;
    AnimationUtil animX, animY, animation, animation2;
    ArrayList<Button> buttons;
    private boolean Dragging, bool;
    private Scene curScene;
    private Button curetBut;
    private Runnable info;

    public MainScreen() {
        super(ITextComponent.nullToEmpty("MainScreen"));
        scale = 0;
        x = 80;
        y = 50;
        w = 500;
        h = 300;
        buttons = new ArrayList<>();
        animX = new AnimationUtil(x, x, 350);
        animY = new AnimationUtil(y, y, 350);
        animation = new AnimationUtil(0, 0, 1000);
        animation2 = new AnimationUtil(0, 0, 1000);
        Arrays.asList(Category.values()).forEach((category -> {
            buttons.add(new Button(category.name, new ModuleScene(category)));
        }));
        //buttons.add(new Button("Config", new Scene()));
        setCuretBut(buttons.get(0));
    }

    @Override
    public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        SettComp.UpdateMouse(p_230430_2_, p_230430_3_);
        sx = x;
        sy = y + 40;
        scale = animation.getAnim();
        if (info != null) {
            info.run();
            info = null;
        }
        IRenderCall runnable = () -> {
            RenderUtil.scale(x, y, w, h, (bool ? animation2.getAnim() : animation.getAnim()), () -> {
                RectUtil.drawRound(x, y, w, h, 6, ColorUtil.swapAlpha(Color.BLACK.getRGB(), scale * 150));
            });
        };
        runnable.execute();
        BlurUtil.registerRenderCall(runnable);
        BlurUtil.draw(6);

        RenderUtil.scale(x, y, w, h, (bool ? animation2.getAnim() : animation.getAnim()), () -> {
            float icoX = 20, iocY = 7;

            RectUtil.drawRoundedTexture(NativeManager.getResource("logo", getInstance.getNativeManager().logo), x + icoX, y + iocY, 25, 25, 4);

            RectUtil.drawline(x, w, y + 40, 1, GuiColors.White.getColor());
            //отрисовка сцены
            if (curScene != null) {
                StencilUtil.initStencilToWrite();
                RectUtil.drawRound(sx, sy, w, 260, 6, -1);
                StencilUtil.readStencilBuffer(1);
                curScene.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
                StencilUtil.uninitStencilBuffer();
            }
            int prev = 14;
            //отрисовка кнопок для открытия сцены
            for (Button button : buttons) {
                button.render(p_230430_1_, x + 100 + prev, y + 12);
                prev += fontManager.font24.getStringWidth(button.getName()) + 20;
            }
        });


        // драг система
        if (!Dragging) {
            mx = (p_230430_2_ - x);
            my = (p_230430_3_ - y);
        } else {
            animX.setTo((p_230430_2_ - mx));
            animY.setTo((p_230430_3_ - my));
        }
        x = animX.getAnim();
        y = animY.getAnim();
        if (scale <= 0.05 && !bool) {
           getInstance.getConfigManager().saveConfig("32423r23febfbfjhbsmfb32");
            mc.setScreen(null);
        }

    }


    @Override
    public boolean mouseClicked(double p_231044_1_, double p_231044_3_, int p_231044_5_) {
        Dragging = ishover(x, y, 100, 45, p_231044_1_, p_231044_3_) && p_231044_5_ == 0;
        buttons.forEach(button -> {
            button.onClick(p_231044_1_, p_231044_3_, p_231044_5_);
        });

        if (curScene != null && ishover(x, y + 40, w, 260, p_231044_1_, p_231044_3_))
            curScene.mouseClicked(p_231044_1_, p_231044_3_, p_231044_5_);
        return super.mouseClicked(p_231044_1_, p_231044_3_, p_231044_5_);
    }

    @Override
    public boolean mouseReleased(double p_231048_1_, double p_231048_3_, int p_231048_5_) {
        Dragging = false;

        if (curScene != null) curScene.mouseReleased(p_231048_1_, p_231048_3_, p_231048_5_);
        return super.mouseReleased(p_231048_1_, p_231048_3_, p_231048_5_);
    }

    public static boolean ishover(float xx, float yy, float width, float height, double mouseX, double mouseY) {
        if (mouseX > xx && mouseX < width + xx && mouseY > yy && mouseY < yy + height) {
            return true;
        }
        return false;
    }

    @Override
    public boolean keyPressed(int p_231046_1_, int p_231046_2_, int p_231046_3_) {
        if (curScene != null) curScene.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_);
        return super.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_);
    }

    @Override
    public boolean mouseScrolled(double p_231043_1_, double p_231043_3_, double p_231043_5_) {
        if (curScene != null) curScene.scrole((float) p_231043_5_);
        return super.mouseScrolled(p_231043_1_, p_231043_3_, p_231043_5_);
    }

    @Override
    public void onClose() {
        bool = false;
        animation.goTo(0);
        animation2.setTo(2);
        if (curScene != null) curScene.onClose();
        if (getInstance.panic) {
        }

    }

    @Override
    protected void init() {
        bool = true;
        animation.goTo(1);
        animation2.goTo(1);

    }

    public void setScene(Scene curScene) {
        if (this.curScene != null) this.curScene.onClose();
        this.curScene = curScene;
    }

    public float getSx() {
        return sx;
    }

    public float getSy() {
        return sy;
    }

    public void setCuretBut(Button curetBut) {
        if (this.curetBut != null) {
            this.curetBut.close();

        }
        this.curetBut = curetBut;
        this.curetBut.init();
        setScene(curetBut.getScene());
    }

    public float getScale() {
        return scale;
    }

    public void setInfo(Runnable info) {
        this.info = info;
    }
}
