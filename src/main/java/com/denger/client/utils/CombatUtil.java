package com.denger.client.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;

import static com.denger.client.Main.getInstance;
import static com.denger.client.Main.mc;

public class CombatUtil {

    public static boolean rayTraceSingleEntity(Vector2f rotation, double distance, Entity entity) {
        Vector3d eyeVec = mc.player.getEyePosition(1.0F);
        Vector3d lookVec =  calculateViewVector(rotation.x, rotation.y);
        Vector3d extendedVec = eyeVec.add(lookVec.scale(distance));

        AxisAlignedBB AABB = entity.getBoundingBox();

        return AABB.contains(eyeVec) || AABB.clip(eyeVec, extendedVec).isPresent();
    }
    public static RayTraceResult pickCustom(double distance, float xRot, float yRot, boolean blocks) {
        try {
            float p_78473_1_ = getInstance.timer.partialTick;
            Entity entity = mc.player;
            RayTraceResult result = null;
            if (entity != null) {
                if (mc.level != null) {
                    double d0 = distance;
                    result = pickCustom(xRot, yRot, d0, p_78473_1_, true);

                    Vector3d vector3d = entity.getEyePosition(p_78473_1_);
                    boolean flag = false;
                    double d1 = d0;
                    if (true) {
                        d1 = distance;
                        d0 = d1;
                    } else {
                        if (d0 > 3.0D) {
                            flag = true;
                        }
                    }

                    d1 = d1 * d1;
                    if (result != null) {
                        d1 = result.getLocation().distanceToSqr(vector3d);
                    }

                    Vector3d vector3d1 = calculateViewVector(xRot,yRot);
                    Vector3d vector3d2 = vector3d.add(vector3d1.x * d0, vector3d1.y * d0, vector3d1.z * d0);
                    AxisAlignedBB axisalignedbb = entity.getBoundingBox().expandTowards(vector3d1.scale(d0)).inflate(1.0D, 1.0D, 1.0D);
                    EntityRayTraceResult entityraytraceresult = ProjectileHelper.getEntityHitResult(entity, vector3d, vector3d2, axisalignedbb, (p_215312_0_) -> {
                        return !p_215312_0_.isSpectator() && p_215312_0_.isPickable();
                    }, d1);

                    if (entityraytraceresult != null) {
                        Vector3d vector3d3 = entityraytraceresult.getLocation();
                        double d2 = vector3d.distanceToSqr(vector3d3);
                        if (flag && d2 > 9.0D && blocks) {
                            result = BlockRayTraceResult.miss(vector3d3, Direction.getNearest(vector3d1.x, vector3d1.y, vector3d1.z), new BlockPos(vector3d3));
                        } else if (d2 < d1 || result == null) {
                            result = entityraytraceresult;
                        }
                    }

                }
            }
            return result;
        } catch (Exception ignored) {
            return null;
        }

    }
    public static final Vector3d calculateViewVector(float p_174806_1_, float p_174806_2_) {
        float f = p_174806_1_ * ((float)Math.PI / 180F);
        float f1 = -p_174806_2_ * ((float)Math.PI / 180F);
        float f2 = MathHelper.cos(f1);
        float f3 = MathHelper.sin(f1);
        float f4 = MathHelper.cos(f);
        float f5 = MathHelper.sin(f);
        return new Vector3d(f3 * f4, -f5, f2 * f4);
    }
    public static RayTraceResult pickCustom(float xRot, float yRot, double p_213324_1_, float p_213324_3_, boolean p_213324_4_) {
        Vector3d vector3d = mc.player.getEyePosition(p_213324_3_);
        Vector3d vector3d1 = calculateViewVectorPro(xRot, yRot);
        Vector3d vector3d2 = vector3d.add(vector3d1.x * p_213324_1_, vector3d1.y * p_213324_1_, vector3d1.z * p_213324_1_);
        return mc.player.level.clip(new RayTraceContext(vector3d, vector3d2, RayTraceContext.BlockMode.OUTLINE, p_213324_4_ ? RayTraceContext.FluidMode.ANY : RayTraceContext.FluidMode.NONE, mc.player));
    }

    public static Vector3d calculateViewVectorPro(float xRot, float yRot) {
        float f = xRot * ((float) Math.PI / 180F);
        float f1 = -yRot * ((float) Math.PI / 180F);
        float f2 = MathHelper.cos(f1);
        float f3 = MathHelper.sin(f1);
        float f4 = MathHelper.cos(f);
        float f5 = MathHelper.sin(f);
        return new Vector3d((f3 * f4), (-f5), (f2 * f4));
    }
    public static boolean isPlayerFalling(boolean onlyCrit, boolean onlySpace) {

        boolean cancelReason = mc.player.isInWater() && mc.player.isEyeInFluid(FluidTags.WATER)
                || mc.player.isInLava()
                || mc.player.onClimbable() || mc.player.isPassenger()
                || mc.player.abilities.flying;
        boolean onSpace = !mc.options.keyJump.isDown() && mc.player.isOnGround() && onlySpace;

        float attackStrength = mc.player.getAttackStrengthScale( 1.0f);

        if (attackStrength < 0.92) return false;

        if (!cancelReason && onlyCrit) {
            return onSpace || !mc.player.isOnGround() && mc.player.fallDistance > 0;
        }

        return true;
    }
    public static Vector3d getVecTarget(LivingEntity target, double distance) {
        return target.position().add(new Vector3d(0, MathHelper.clamp(target.getEyeHeight() * (mc.player.distanceTo(target) / (distance + target.getBbWidth())), mc.player.getEyeHeight() / 2, mc.player.getEyeHeight()), 0));
    }
}
