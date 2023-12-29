package com.denger.client.modules.mods.misc;


import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;
import com.denger.client.utils.Utils;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import static com.denger.client.MainNative.mc;
@ModuleTarget(ModName = "FmzusbTxbq",category = Category.MISC)
public class ElytraSwap extends Module {

    @Override
    public void onEnable() {
        int elytra = Utils.findItem(Items.ELYTRA);

        if (elytra != -1) {
            if (elytra == 6) {
                mc.gameMode.handleInventoryMouseClick(0, 6, 0, ClickType.PICKUP, mc.player);
                mc.gameMode.handleInventoryMouseClick(0, chestplate(), 0, ClickType.PICKUP, mc.player);
                mc.gameMode.handleInventoryMouseClick(0, 6, 0, ClickType.PICKUP, mc.player);
            } else if (chestplate() == 6) {
                mc.gameMode.handleInventoryMouseClick(0, elytra, 0, ClickType.PICKUP, mc.player);
                mc.gameMode.handleInventoryMouseClick(0, 6, 0, ClickType.PICKUP, mc.player);
                mc.gameMode.handleInventoryMouseClick(0, elytra, 0, ClickType.PICKUP, mc.player);
            }
        }
        toggle();
    }

    int chestplate() {
        for (int index = 0; index < mc.player.inventoryMenu.slots.size(); index++) {
            Item item = mc.player.inventoryMenu.getSlot(index).getItem().getItem();
            if (item == Items.NETHERITE_CHESTPLATE || item == Items.DIAMOND_CHESTPLATE || item == Items.GOLDEN_CHESTPLATE || item == Items.IRON_CHESTPLATE || item == Items.CHAINMAIL_CHESTPLATE || item == Items.LEATHER_CHESTPLATE) {
                return index;
            }
        }
        return 0;
    }


}

