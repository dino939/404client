package com.denger.client.modules.mods.combat;

import com.denger.client.another.hooks.forge.even.addevents.RotationEvent;
import com.denger.client.another.settings.SettingTarget;
import com.denger.client.another.settings.sett.BoolSetting;
import com.denger.client.another.settings.sett.FloatSetting;
import com.denger.client.another.settings.sett.ModSetting;
import com.denger.client.another.settings.sett.MultiBoolSetting;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import com.denger.client.utils.CombatUtil;
import com.denger.client.utils.anims.Animation;
import com.denger.client.utils.MathUtils;
import com.denger.client.utils.Utils;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.denger.client.Main.getInstance;
import static com.denger.client.Main.mc;

@ModuleTarget(ModName = "LjmmBvsb", category = Category.COMBAT)
public class KillAura extends Module {

    @SettingTarget(name = "Сервер Дистанция")
    BoolSetting custDist = new BoolSetting().setBol(false);
    @SettingTarget(name = "Дистанция")
    FloatSetting distanciya = (FloatSetting) new FloatSetting().setMin(1).setMax(6).setVal(3.3f).setVisible(() -> !custDist.getState());
    @SettingTarget(name = "Поле зрения")
    private BoolSetting visibles = new BoolSetting().setBol(true);
    @SettingTarget(name = "Только криты")
    private BoolSetting сrit = new BoolSetting().setBol(true);
    @SettingTarget(name = "Умные криты")
    private BoolSetting сritSmart = new BoolSetting().setBol(true);
    @SettingTarget(name = "Ротации")
    private ModSetting rots = new ModSetting().setMods("Matrix", "NCP", "Vulcan", "Test").setCurent("Vulcan");

    @SettingTarget(name = "Таргет")
    public ModSetting getTarg = new ModSetting().setMods("Сортировка","RayTrace").setCurent("Сортировка");
    //"RayTrace",
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

    public static LivingEntity target;







    public void setTarget() {
        switch (getTarg.getCurent()) {
            case "Сортировка":
                target = getTarget();
                break;
            case "RayTrace":
                RayTraceResult traceResult = CombatUtil.pickCustom(getDist(), mc.player.xRot, mc.player.yRot, true);
                if (traceResult instanceof EntityRayTraceResult && ((EntityRayTraceResult) traceResult).getEntity() instanceof LivingEntity) {
                    target = (LivingEntity) ((EntityRayTraceResult) traceResult).getEntity();
                } else {
                    target = null;
                }
                break;
        }
    }

    private LivingEntity getTarget() {
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
    public float getDist() {
        return custDist.getState() ? mc.gameMode.getPickRange() : distanciya.getVal();
    }
}
