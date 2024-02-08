package com.denger.client.modules.mods.combat;

import com.denger.client.another.hooks.forge.even.addevents.EventRayPick;
import com.denger.client.another.hooks.forge.even.addevents.RotationEvent;
import com.denger.client.another.hooks.forge.even.addevents.WorldUpdate;
import com.denger.client.another.settings.SettingTarget;
import com.denger.client.another.settings.sett.BoolSetting;
import com.denger.client.another.settings.sett.FloatSetting;
import com.denger.client.another.settings.sett.ModSetting;
import com.denger.client.another.settings.sett.MultiBoolSetting;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import com.denger.client.utils.anims.Animation;
import com.denger.client.utils.MathUtils;
import com.denger.client.utils.PlayerUtil;
import com.denger.client.utils.Utils;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.network.play.client.CHeldItemChangePacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.denger.client.Main.getInstance;
import static com.denger.client.Main.mc;
import static com.denger.client.utils.MathUtils.clamp;
import static com.denger.client.utils.MathUtils.wrapDegrees;
import static java.lang.Math.*;
import static net.minecraft.util.math.MathHelper.atan2;

@ModuleTarget(ModName = "Bvsb", category = Category.COMBAT)
public class Aura extends Module {
    @SettingTarget(name = "Сервер Дистанция")
    BoolSetting custDist = new BoolSetting().setBol(false);
    @SettingTarget(name = "Дистанция")
    FloatSetting distanciya = (FloatSetting) new FloatSetting().setMin(1).setMax(6).setVal(3.3f).setVisible(() -> !custDist.getState());
    @SettingTarget(name = "Крит режим")
    private ModSetting сrit = new ModSetting().setMods("умный", "толко", "без").setCurent("умный");
    @SettingTarget(name = "Ротации")
    private ModSetting rots = new ModSetting().setMods("Matrix", "NCP", "Vulcan", "Test").setCurent("Vulcan");
    public static Entity target;
    @SettingTarget(name = "Таргет")
    public ModSetting getTarg = new ModSetting().setMods("RayTrace", "Сортировка").setCurent("Сортировка");
    //
    @SettingTarget(name = "Игроков", toAdd = false)
    BoolSetting players = new BoolSetting().setBol(true);
    @SettingTarget(name = "Животных", toAdd = false)
    BoolSetting entiti = new BoolSetting();
    @SettingTarget(name = "Таргетица на")
    MultiBoolSetting bools = (MultiBoolSetting) new MultiBoolSetting().addBools(players, entiti).setVisible(() -> getTarg.getCurent().equals("Сортировка"));
    //
    @SettingTarget(name = "Здоровью", toAdd = false)
    BoolSetting poHp = new BoolSetting().setBol(true);
    @SettingTarget(name = "Дистанции", toAdd = false)
    BoolSetting poDist = new BoolSetting().setBol(true);
    @SettingTarget(name = "Fov", toAdd = false)
    BoolSetting poFov = new BoolSetting().setBol(true);
    @SettingTarget(name = "Сортировка по")
    MultiBoolSetting sortPo = (MultiBoolSetting) new MultiBoolSetting().addBools(poHp, poDist, poFov).setVisible(() -> getTarg.getCurent().equals("Сортировка"));
    Animation yawAnim = new Animation(0.0f, 0.0f, 0.1f), pitchAnim = new Animation(0.0f, 0.0f, 0.1f);
    float xRotC = 0, yRotC = 0;
    Random random = new Random();
    boolean critflag;

    public Aura() {
        setModSetting(rots);
    }

    @SubscribeEvent
    public void onUpdate(WorldUpdate e) {
        setTarget();

        if (target != null) {

            rotation(target);
            xRotC = pitchAnim.getAnim();
            yRotC = yawAnim.getAnim();
            assert mc.player != null;
            if (mc.player.getAttackStrengthScale(0.85f) == 1 && mc.player.distanceTo(target) < getDist() && PlayerUtil.checkFall()) {
                int axe = 0;
                for (int index = 0; index < 9; ++index) {
                    if (mc.player.inventory.getItem(index).getItem() instanceof AxeItem) {
                        axe = index;
                    }
                }
                if (target instanceof PlayerEntity) {
                    if (axe != 0 && ((PlayerEntity) target).isBlocking()) {
                        mc.player.connection.send(new CHeldItemChangePacket(axe));
                        attackModule();
                        mc.player.connection.send(new CHeldItemChangePacket(mc.player.inventory.selected));
                    } else {
                        attackModule();
                    }
                } else {
                    attackModule();
                }

            }
        }
    }

    @SubscribeEvent
    public void pickEvent(EventRayPick eventRayPick) {
        if (target != null) {
            eventRayPick.setResult(new EntityRayTraceResult(target));
        }
    }

    @SubscribeEvent
    public void rotationEvent(RotationEvent e) {
        if (target == null) return;
        e.setStepCancel(false);
        e.setyRot(yRotC);
        e.setxRot(xRotC);

    }

    public void rotation(Entity t) {

        switch (rots.getCurent()) {
            case "Matrix":
                rotMatrix(t);
                break;
            case "NCP":
                rotNCP(t);
                break;
            case "Vulcan":
                rotVulcan(t);
                break;
            case "Test":
                rotTest(t);
                break;
        }
    }

    public void rotMatrix(Entity t) {
        float speed = 0.05f;
        assert mc.player != null;

        float yo = MathUtils.randomFloat(0.9f, 1.25f);
        float[] rot = Utils.rotationsPro(t, yo);

        this.yawAnim.speed = speed;
        this.pitchAnim.speed = speed;
        this.yawAnim.to = rot[0] + MathUtils.randomFloat(-3f, 3f);
        this.pitchAnim.to = rot[1];

    }

    public void rotNCP(Entity t) {
        float speed = 0.05f;
        float yo = mc.player.distanceTo(t) / getDist() * 1.2f;
        yo = MathUtils.clamp(yo, 0.0f, 1.2f);
        float[] rot = Utils.rotationsPro(t, yo);
        this.yawAnim.speed = speed;
        this.pitchAnim.speed = speed;
        this.yawAnim.to = rot[0];
        this.pitchAnim.to = rot[1];
    }

    public void rotVulcan(Entity t) {
        float speed = 0.2f;

        float[] rot = Utils.rotationsPro(t, 0.5f);
        float[] rot2 = Utils.rotationsPro(t, 0.0f);
        this.yawAnim.speed = Math.abs(rot[0] - mc.player.yHeadRot) <= t.getBbWidth() * 30.0f / mc.player.distanceTo(t) ? 0.0015f : speed;
        this.pitchAnim.speed = Math.abs(rot[1] - this.xRotC) <= t.getBbHeight() * 10.0f / mc.player.distanceTo(t) ? 0.0015f : speed;
        this.yawAnim.to = rot[0];
        this.pitchAnim.to = rot2[1];
    }

    public void rotTest(Entity t) {
        float speed = 0.5f;
        float speedAnim = 0.2f;
        if (isInHit(mc.player, t)) {
            Vector3d vec3d = getVector3d(mc.player, t);

            float rawYaw = wrapDegrees((float) (toDegrees(atan2(vec3d.z, vec3d.x)) - 90));
            float rawPitch = wrapDegrees((float) toDegrees(-atan2(vec3d.y, hypot(vec3d.x, vec3d.z))));

            float yawDelta = wrapDegrees(rawYaw - mc.player.yRot);
            float pitchDelta = wrapDegrees(rawPitch - mc.player.xRot);

            if (abs(yawDelta) > 180) yawDelta -= signum(yawDelta) * 360;

            float additionYaw = yawDelta;
            float additionPitch = pitchDelta;
            this.yawAnim.speed = speedAnim;
            this.pitchAnim.speed = speedAnim;
            float yaw = mc.player.yRot + additionYaw + random.nextInt(3) - 1;
            float pitch = mc.player.xRot + additionPitch + random.nextInt(3) - 1;
            this.yawAnim.to = getSensitivity(yaw);
            this.pitchAnim.to = getSensitivity(clamp(pitch, -89, 89));
        }
    }

    public float getDist() {
        return custDist.getState() ? mc.gameMode.getPickRange() : distanciya.getVal();
    }

    private Vector3d getVector3d(LivingEntity me, Entity to) {
        double wHalf = to.getBbWidth() / 2;

        double yExpand = MathHelper.clamp(me.getEyeY() - to.getY(), 0, to.getBbHeight() * (mc.player.distanceTo(to) / getDist()));

        double xExpand = MathHelper.clamp(mc.player.getX() - to.getX(), -wHalf, wHalf);
        double zExpand = MathHelper.clamp(mc.player.getZ() - to.getZ(), -wHalf, wHalf);

        return new Vector3d(
                to.getX() - me.getX() + xExpand,
                to.getY() - me.getEyeY() + yExpand,
                to.getZ() - me.getZ() + zExpand
        );
    }

    private boolean isInHit(LivingEntity me, Entity to) {
        double wHalf = to.getBbWidth() / 2;

        double yExpand = MathHelper.clamp(me.getEyeY() - to.getY(), 0, to.getBbHeight());

        double xExpand = MathHelper.clamp(mc.player.getX() - to.getX(), -wHalf, wHalf);
        double zExpand = MathHelper.clamp(mc.player.getZ() - to.getZ(), -wHalf, wHalf);

        return new Vector3d(
                to.getX() - me.getX() + xExpand,
                to.getY() - me.getEyeY() + yExpand,
                to.getZ() - me.getZ() + zExpand
        ).length() != 0;
    }

    public void simpleAttack(Entity entity) {
        assert mc.player != null;
        assert mc.gameMode != null;
        mc.gameMode.attack(mc.player, entity);
        mc.player.swing(Hand.MAIN_HAND);
    }

    public static float getSensitivity(float rot) {
        return getDeltaMouse(rot) * getGCDValue();
    }

    public static float getGCDValue() {
        return (float) (getGCD() * 0.15);
    }

    public static float getGCD() {
        float f1;
        return (f1 = (float) (mc.options.sensitivity * 0.6 + 0.2)) * f1 * f1 * 8;
    }

    public static float getDeltaMouse(float delta) {
        return Math.round(delta / getGCDValue());
    }

    public void setTarget() {
        switch (getTarg.getCurent()) {
            case "Сортировка":
                target = getTarget();
                break;
            case "RayTrace":
                RayTraceResult traceResult = Utils.pickCustom(getDist(), mc.player.xRot, mc.player.yRot, true);
                if (traceResult instanceof EntityRayTraceResult) {
                    target = ((EntityRayTraceResult) traceResult).getEntity();
                } else {
                    target = null;
                }
                break;
        }
    }

    private Entity getTarget() {
        if (mc.level == null || mc.player == null) return null;

        boolean players = this.players.getState();
        boolean mobs = this.entiti.getState();

        List<LivingEntity> entities = StreamSupport.stream(mc.level.entitiesForRendering().spliterator(), false)
                .filter(entity -> entity instanceof LivingEntity)
                .map(entity -> (LivingEntity) entity)
                .filter(entity -> {
                    if (entity instanceof PlayerEntity) {
                        return players;
                    } else if (entity instanceof MobEntity) {
                        return mobs;
                    }
                    return true;
                })
                .filter(player ->
                        player != mc.player &&
                                mc.gameMode.getPickRange() > Objects.requireNonNull(mc.player).distanceTo(player) &&
                                player.canSee(mc.player) &&
                                !player.isDeadOrDying() &&
                                !getInstance.getFriendManager().isFriend(player)
                )
                .collect(Collectors.toList());

        if (entities.isEmpty()) return null;

        if (poHp.getState()) {
            entities.sort(Comparator.comparingDouble(LivingEntity::getHealth));
        }

        if (poDist.getState()) {
            entities.sort(Comparator.comparingDouble(entity -> entity.distanceTo(mc.player)));
        }
        if (poFov.getState()) {
            entities.sort(Comparator.comparingDouble(this::getFOVAngle));
        }

        return entities.get(0);
    }

    private float getFOVAngle(LivingEntity e) {
        double difX = e.getX() - mc.player.getX();
        double difZ = e.getZ() - mc.player.getZ();
        float yaw = (float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(difZ, difX)) - 90.0);
        return Math.abs(yaw - MathHelper.wrapDegrees(mc.player.yRot));
    }

    public void attackModule() {
        RayTraceResult result = Utils.pickCustom(mc.gameMode.getPickRange(), pitchAnim.getAnim(), yawAnim.getAnim(), false);
        if (!(result instanceof EntityRayTraceResult) || ((EntityRayTraceResult) result).getEntity() != target) {
            return;
        }
        if (mc.level == null) {
            return;
        }

        boolean flag = mc.player.getAttackStrengthScale(0.5f) > 0.9f;
        boolean flag2 = flag && mc.player.fallDistance != 0.0f &&
                !mc.player.isOnGround() &&
                !mc.player.isRidingJumpable();

        boolean ff1 = flag2 && this.critflag;
        boolean ff = mc.player.isOnGround() || mc.player.isInWater() ||
                mc.level.getBlockState(new BlockPos(mc.player.getX(), mc.player.getY() + 0.10000000149011612, mc.player.getZ())).getBlock() != Blocks.AIR;
        boolean f = true;
        switch (сrit.getCurent()) {
            case "smart":
                f = ff;
                break;
            case "none":
                f = true;
                break;
            case "only":
                f = ff1;
                break;
        }

        float attackStrength = mc.player.getAttackStrengthScale(0.0f);
        float rand = MathUtils.randomFloat(0.80f, 0.9f);
        if (attackStrength > rand && f) {
            mc.gameMode.attack(mc.player, ((EntityRayTraceResult) mc.hitResult).getEntity());
            mc.player.swing(Hand.MAIN_HAND);
        }

        this.critflag = flag2;
    }
}
