package com.denger.client.another.models.pets;

import net.minecraft.entity.Entity;
import net.minecraft.util.Timer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import static com.denger.client.Main.getInstance;
import static com.denger.client.Main.mc;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTranslatef;

public class Pet {
    private Entity targetEntity, oldTarget;
    private double xPos, yPos, zPos, OldX, OldY, OldZ;
    private float xRot, yRot, OXRot, OYRot;
    public long tickCount;
    private long lastMoveTick = 0;
    static Timer timer = getInstance.timer;
    private AxisAlignedBB hitbox;
    private boolean afk, moving;

    public Pet(Vector3d pos, Vector2f rot, Entity targetEntity, float size) {
        xPos = OldX = pos.x;
        yPos = OldY = pos.y;
        zPos = OldZ = pos.z;
        xRot = OXRot = rot.x;
        yRot = OYRot = rot.y;
        hitbox = new AxisAlignedBB(pos.x - size, pos.y - size, pos.z - size, pos.x + size, pos.y + size, pos.z + size);
        this.targetEntity = oldTarget = targetEntity;
    }

    public Pet(double x, double y, double z, float xrot, float yrot, Entity entity, float size) {
        this(new Vector3d(x, y, z), new Vector2f(xrot, yrot), entity, size);
    }

    public Pet(Entity entity) {
        this(entity.position(), new Vector2f(entity.xRot, entity.yRot), entity, 0.4f);
    }

    public Pet() {
        this(0, 0, 0, 0, 0, null, 0.4f);
    }

    public void update() {
        tickCount++;
        moving = !(xPos != OldX && yPos != OldY && zPos != OldZ && xRot != OXRot && yRot != OYRot);
        if (moving) lastMoveTick = tickCount;
        afk = (tickCount - lastMoveTick) < 600;
        OldX = xPos;
        OldY = yPos;
        OldZ = zPos;
        OXRot = xRot;
        OYRot = yRot;
    }

    public void render(RenderWorldLastEvent renderWorldLastEvent) {
        if (mc.options.reducedDebugInfo) {
           // RenderUtil.
        }
        glTranslatef(0.0f, 0.0f, -5.0f);
        glRotatef(xRot, 1.0f, 0.0f, 0.0f);
        glRotatef(yRot, 0.0f, 1.0f, 0.0f);

    }

    public double getXpos() {
        return xPos;
    }

    public double getYpos() {
        return yPos;
    }

    public double getZpos() {
        return zPos;
    }

    public Vector3d position() {
        return new Vector3d(xPos, yPos, zPos);
    }

    public Vector2f rots() {
        return new Vector2f(xRot, yRot);
    }

    public void setSize(float size) {
        hitbox = new AxisAlignedBB(xPos - size, yPos - size, zPos - size, xPos + size, yPos + size, zPos + size);
    }

    public double getXRotRender() {
        return OldX + (xPos - OldX) * (double) timer.partialTick - mc.getEntityRenderDispatcher().camera.getPosition().x;
    }

    public double getYRotRender() {
        return OldY + (yPos - OldY) * (double) timer.partialTick - mc.getEntityRenderDispatcher().camera.getPosition().y;
    }

    public double getZRotRender() {
        return OldZ + (zPos - OldZ) * (double) timer.partialTick - mc.getEntityRenderDispatcher().camera.getPosition().z;
    }

    public boolean isAfk() {
        return afk;
    }

    public AxisAlignedBB getHitbox() {
        return hitbox;
    }

    public boolean isMoving() {
        return moving;
    }

    public Entity getTargetEntity() {
        return targetEntity;
    }

    public void setTargetEntity(Entity targetEntity) {
        oldTarget = this.targetEntity;
        this.targetEntity = targetEntity;


    }
}
