package com.denger.client.modules.mods.misc;

import com.denger.client.another.hooks.forge.even.addevents.WorldUpdate;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import com.denger.client.utils.EntityUtil;
import com.denger.client.utils.MathUtils;
import com.denger.client.utils.Utils;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Blocks;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

import static com.denger.client.MainNative.getInstance;
import static com.denger.client.MainNative.mc;
import static com.denger.client.utils.MathUtils.getDistanceXZ;


@ModuleTarget(ModName = "TfyBvsb", category = Category.MISC)
public class SexAura extends Module {
    public static Module get;
    boolean processMove = false;
    boolean sex;
    static LivingEntity target;
    // вега за пенил если чо да

    @Override
    public void onDisable() {
        target = null;
    }

    @SubscribeEvent
    public void onAttack(AttackEntityEvent event) {
        if (event.getPlayer() == mc.player) {
            if (event.getTarget() instanceof LivingEntity) {
                target = (LivingEntity) event.getTarget();
            }
        }
    }

    private static PlayerEntity getMe() {
        return mc.player;
    }

    private static boolean entityIsCurrentToFilter(LivingEntity entity, double range) {
        if (entity == null || entity.getHealth() == 0.0f || EntityUtil.isInclude(entity) || entity instanceof ServerPlayerEntity || entity instanceof ArmorStandEntity || getInstance.getFriendManager().isFriend(entity))
            return false;
        assert getMe() != null;
        float[] rots = rotations(entity);
        return getMe().canSee(entity) && !entity.isInWater() && (!(entity instanceof PlayerEntity) || !((PlayerEntity) entity).isCreative()) /*&& !AntiBot.entityIsBotAdded(entity)*/ && SexAura.getMe().position().distanceTo(Utils.pickCustom(range + 2, rots[1], rots[0], false).getLocation()) <= range;
    }

    public static float[] rotations(Entity entity) {
        assert mc.player != null;
        double x = entity.getX() - mc.player.getX();
        double y = entity.getY() - (mc.player.getY() + mc.player.getEyeHeight()) + 1.5;
        double z = entity.getZ() - mc.player.getZ();

        double u = MathHelper.sqrt(x * x + z * z);

        float u2 = (float) (MathHelper.atan2(z, x) * (180D / Math.PI) - 90.0F);
        float u3 = (float) (-MathHelper.atan2(y, u) * (180D / Math.PI));

        return new float[]{u2, u3};

    }

    public static LivingEntity getCurrentTarget(float range) {
        LivingEntity base = null;
        assert mc.level != null;
        for (Entity o : mc.level.entitiesForRendering()) {
            LivingEntity living;
            if (!(o instanceof LivingEntity) || !SexAura.entityIsCurrentToFilter(living = (LivingEntity) o, range) || living.getHealth() == 0.0f || !(SexAura.getMe().distanceTo(living) <= range))
                continue;
            range = SexAura.getMe().distanceTo(living);
            base = living;
        }
        // return base;
        return target;
    }

    public static AbstractClientPlayerEntity getCurrentTarget(String name) {
        AbstractClientPlayerEntity base = null;
        assert mc.level != null;
        for (AbstractClientPlayerEntity o : mc.level.players()) {
            if (o.getName().getString().equals(name)) {
                base = o;
                break;
            }

        }
        return base;
    }

    double getSpeedToFollowCoord(Vector3d pos) {
        assert getMe() != null;
        double speed = MathUtils.clamp((float) (MathUtils.getBps(getMe()) * 1.1), 0.0F, 0.2499F) - (mc.player.tickCount % 2 == 0 ? 0.003 : 0.0);
        return MathUtils.clamp((float) speed, 0.0F, (float) (1.0 - getDistanceXZ(mc.player.position(), pos)));
    }

    Vector3d getVecButtomAtTarget(LivingEntity target, float setedRange) {
        return target.position().add(Math.sin(Math.toRadians(target.yBodyRot)) * (double) setedRange, 0.0, -Math.cos(Math.toRadians(target.yBodyRot)) * (double) setedRange);
    }

    boolean movedToProcess(final Vector3d vec, final float checkR, final LivingEntity target) {
        if (vec.distanceTo(mc.player.position()) < 1.5f) {
            mc.options.keyUp.setDown(GLFW.glfwGetKey(mc.getWindow().getWindow(), mc.options.keyUp.getKey().getValue()) == 1);
            return false;
        }
        boolean b = false;
        Label_0051:
        {
            if (getDistanceXZ(mc.player.position(), vec) <= checkR) {
                if (MathUtils.getDifferenceOf(mc.player.getY(), vec.y()) < 1.0) {
                    b = true;
                    break Label_0051;
                }
            }
            b = false;
        }
        if (mc.player.isInWater() || mc.player.isInLava()) {
            mc.options.keyJump.setDown(true);
        } else mc.options.keyJump.setDown(GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_SPACE) == 1);
        final boolean moved = b;
        final float rot = rotations(target)[0];
        if (!moved) {
            final float num1 = rot;
            if (MathUtils.getDifferenceOf(num1, mc.player.yRot) > 6.0) {

                mc.player.yRot = rot;
            }
            mc.options.keyUp.setDown(true);
            mc.options.keyRight.setDown(false);
            mc.options.keyDown.setDown(false);
            //final double speed = this.getSpeedToFollowCoord(vec);
            final Vector3d positionVector = mc.player.position();
            final double x = -Math.sin(Math.toRadians(mc.player.yRot)) * 0.8;
            final double y = 0.0;
            final Vector3d addSelfYaw = positionVector.add(x, y, Math.cos(Math.toRadians(mc.player.yRot)) * 0.8);
            Label_0378:
            {
                Label_0307:
                {
                    if (mc.player.isOnGround()) {
                        if (getDistanceXZ(mc.player.position(), vec) > 4.0) {
                            break Label_0307;
                        }
                    }
                    if (!posBlock(addSelfYaw.x, addSelfYaw.y + 0.3, addSelfYaw.z) || posBlock(addSelfYaw.x, addSelfYaw.y + 1.3, addSelfYaw.z)) {
                        mc.options.keyJump.setDown(false);
                        break Label_0378;
                    } else {
                        mc.options.keyJump.setDown(true);
                    }
                }
                final KeyBinding keyBindJump = mc.options.keyJump;
                boolean pressed;
                Label_0359:
                {
                    if (mc.player.isOnGround()) {
                        if (getDistanceXZ(mc.player.position(), vec) > 1.7) {
                            pressed = true;
                            break Label_0359;
                        }
                    }
                    pressed = false;
                }
                keyBindJump.setDown(pressed);
            }
            this.processMove = true;
        } else if (this.processMove) {
            if (!mc.player.isAutoJumpEnabled()) {
                mc.options.keyJump.setDown(false);
            }
            mc.options.keyUp.setDown(false);
            if (mc.player.position().distanceTo(vec) < 0.2) {
                final ClientPlayerEntity player = mc.player;
                final double xCoord = vec.x;
                player.setPos(xCoord, mc.player.getY(), vec.z());
                //mc.player.multiplyMotionXZ(0.0f);
            }
            this.processMove = false;
        }
        final float num2 = rot;
        if (MathUtils.getDifferenceOf(num2, mc.player.yRot) > 6.0) {
            mc.player.yRot = rot;
        }
        return moved;
    }

    void doSex(boolean DO) {
        if (DO) {
            assert mc.player != null;
            // mc.options.keyShift.setDown(mc.player.tickCount % 2 == 0);
            if (!this.sex) {
                this.sex = true;
            }
        } else if (this.sex) {
            mc.options.keyShift.setDown(false);
            this.sex = false;
        }
    }

    public static boolean posBlock(final double x, final double y, final double z) {
        return mc.level.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.AIR && mc.level.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.WATER && mc.level.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.LAVA && !(mc.level.getBlockState(new BlockPos(x, y, z)).getBlock() instanceof BedBlock) && mc.level.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.CAKE && mc.level.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.TALL_GRASS && mc.level.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.STONE_BUTTON && mc.level.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.JUNGLE_BUTTON && mc.level.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.FLOWER_POT && mc.level.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.CHORUS_FLOWER && mc.level.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.VINE && mc.level.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.ACACIA_FENCE && mc.level.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.ACACIA_FENCE_GATE && mc.level.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.BIRCH_FENCE && mc.level.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.BIRCH_FENCE_GATE && mc.level.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.DARK_OAK_FENCE && mc.level.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.DARK_OAK_FENCE_GATE && mc.level.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.JUNGLE_FENCE && mc.level.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.JUNGLE_FENCE_GATE && mc.level.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.NETHER_BRICK_FENCE && mc.level.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.OAK_FENCE && mc.level.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.OAK_FENCE_GATE && mc.level.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.SPRUCE_FENCE && mc.level.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.SPRUCE_FENCE_GATE && mc.level.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.ENCHANTING_TABLE && mc.level.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.END_PORTAL_FRAME && mc.level.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.WARPED_WALL_SIGN && mc.level.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.SKELETON_SKULL && mc.level.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.DAYLIGHT_DETECTOR && mc.level.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.PURPUR_SLAB && mc.level.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.STONE_SLAB && mc.level.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.REDSTONE_WIRE && mc.level.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.REDSTONE_TORCH && mc.level.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.TORCH && mc.level.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.REDSTONE_WIRE && mc.level.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.WATER && mc.level.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.SNOW;
    }

    @SubscribeEvent
    public void onUpdate(WorldUpdate e) {
        LivingEntity target = SexAura.getCurrentTarget(35.0f);

        if (target != null) {

            this.doSex(this.movedToProcess(this.getVecButtomAtTarget(target, 0.3f), MathUtils.clamp(target.getBbWidth() / 2.0f, 0.1f, 2.0f), target));
        }
    }


    @Override
    public void setState(boolean state) {
        //this.doSex(false);
        super.setState(state);
    }
}
