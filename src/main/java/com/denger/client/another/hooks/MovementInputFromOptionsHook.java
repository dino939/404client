package com.denger.client.another.hooks;

 import net.minecraft.client.GameSettings;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.MovementInputFromOptions;

import static com.denger.client.Main.mc;

public class MovementInputFromOptionsHook extends MovementInputFromOptions {

    private final GameSettings options;

    public MovementInputFromOptionsHook(GameSettings p_i1237_1_) {
        super(p_i1237_1_);
        this.options = p_i1237_1_;
    }

    public void tick(boolean p_225607_1_) {
        this.up = isDown(options.keyUp.getKey().getValue());
        this.down = isDown(options.keyDown.getKey().getValue());
        this.left = isDown(options.keyLeft.getKey().getValue());
        this.right = isDown(options.keyRight.getKey().getValue());
        this.forwardImpulse = this.up == this.down ? 0.0F : (this.up ? 1.0F : -1.0F);
        this.leftImpulse = this.left == this.right ? 0.0F : (this.left ? 1.0F : -1.0F);
        this.jumping = isDown(options.keyJump.getKey().getValue());
        this.shiftKeyDown = this.options.keyShift.isDown();
        if (p_225607_1_) {
            this.leftImpulse = (float) ((double) this.leftImpulse * 0.3D);
            this.forwardImpulse = (float) ((double) this.forwardImpulse * 0.3D);
        }

    }

    private boolean isDown(int a) {
        return !(mc.screen instanceof ChatScreen) && InputMappings.isKeyDown(mc.getWindow().getWindow(), a);
    }
}
