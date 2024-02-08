package com.denger.client.modules.mods.misc;

import com.denger.client.another.networkutills.ConnectionUtil;
import com.denger.client.another.settings.SettingTarget;
import com.denger.client.another.settings.sett.BoolSetting;
import com.denger.client.another.settings.sett.MultiBoolSetting;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.network.play.client.CPlayerTryUseItemOnBlockPacket;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;

import static com.denger.client.Main.mc;

@ModuleTarget(ModName = "OpJoufsbdu", category = Category.MISC)
public class NoInteract extends Module {
    @SettingTarget(name = "стенды", toAdd = false)
    private BoolSetting stend = new BoolSetting();
    @SettingTarget(name = "сундуки", toAdd = false)
    private BoolSetting chest = new BoolSetting();
    @SettingTarget(name = "двери", toAdd = false)
    private BoolSetting door = new BoolSetting();
    @SettingTarget(name = "воронки", toAdd = false)
    private BoolSetting voronca = new BoolSetting();
    @SettingTarget(name = "кнопки", toAdd = false)
    private BoolSetting buttons = new BoolSetting();
    @SettingTarget(name = "раздатчики", toAdd = false)
    private BoolSetting razdachik = new BoolSetting();
    @SettingTarget(name = "нотные блоки", toAdd = false)
    private BoolSetting music = new BoolSetting();
    @SettingTarget(name = "верстаки", toAdd = false)
    private BoolSetting craft_table = new BoolSetting();
    @SettingTarget(name = "люки", toAdd = false)
    private BoolSetting traps = new BoolSetting();
    @SettingTarget(name = "печки", toAdd = false)
    private BoolSetting pech = new BoolSetting();
    @SettingTarget(name = "калитки", toAdd = false)
    private BoolSetting kalitki = new BoolSetting();
    @SettingTarget(name = "наковальни", toAdd = false)
    private BoolSetting nakovalni = new BoolSetting();
    @SettingTarget(name = "рычаги", toAdd = false)
    private BoolSetting level = new BoolSetting();
    @SettingTarget(name = "Обьекты")
    private MultiBoolSetting multiBoolSetting = new MultiBoolSetting().addBools(
            stend, chest, door, voronca, buttons, music, craft_table,
            traps, pech, kalitki, nakovalni, level, razdachik
    );

    @Override
    public boolean onPacket(Object packet, ConnectionUtil.Side side) {
        if (packet instanceof CPlayerTryUseItemOnBlockPacket){
            if (mc.hitResult.getType() == RayTraceResult.Type.BLOCK
            ) {
                BlockRayTraceResult result = (BlockRayTraceResult) mc.hitResult;
                if (mc.level.getBlockState(result.getBlockPos()).getBlock() == Blocks.CRAFTING_TABLE && craft_table.getState()) {
                        return false;
                }
                if (mc.level.getBlockState(result.getBlockPos()).getBlock().getDescriptionId().contains("anvil") && nakovalni.getState()) {
                        return false;
                }
                if (mc.level.getBlockState(result.getBlockPos()).getBlock().getDescriptionId().contains("trapdoor") && traps.getState()) {
                        return false;
                }
                if (mc.level.getBlockState(result.getBlockPos()).getBlock() == Blocks.LEVER && level.getState()) {
                        return false;
                }
                if (mc.level.getBlockState(result.getBlockPos()).getBlock() == Blocks.FURNACE && pech.getState()) {
                        return false;
                }
                if (mc.level.getBlockState(result.getBlockPos()).getBlock().getDescriptionId().contains("fence") && kalitki.getState()) {
                        return false;
                }
                if (mc.level.getBlockState(result.getBlockPos()).getBlock() == Blocks.NOTE_BLOCK && music.getState()) {
                        return false;
                }
                if (mc.level.getBlockState(result.getBlockPos()).getBlock() == Blocks.DISPENSER && razdachik.getState()) {
                        return false;
                }
                if (mc.level.getBlockState(result.getBlockPos()).getBlock().getDescriptionId().contains("button") && buttons.getState()) {
                        return false;
                }
                if (mc.level.getBlockState(result.getBlockPos()).getBlock() == Blocks.HOPPER && voronca.getState()) {
                        return false;
                }
                if (mc.level.getBlockState(result.getBlockPos()).getBlock().getDescriptionId().contains("door") && door.getState()) {
                        return false;
                }
                if (mc.level.getBlockState(result.getBlockPos()).getBlock().getDescriptionId().contains("chest") && chest.getState()) {
                        return false;
                }
            }else if (mc.hitResult.getType() == RayTraceResult.Type.ENTITY && stend.getState() && ((EntityRayTraceResult)(mc.hitResult)).getEntity() instanceof ArmorStandEntity){
                return false;

            }
        }
        return super.onPacket(packet, side);
    }

}
