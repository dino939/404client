package com.denger.client.utils;

import net.minecraft.item.Item;

import static com.denger.client.MainNative.mc;


public class FindUtil {
    public static int findItemInInventory(Item item) {
        for (int index = 0; index < mc.player.inventoryMenu.slots.size(); index++) {
            if (mc.player.inventoryMenu.getSlot(index).getItem().getItem() == item) {
                return index;
            }
        }
        return -1;
    }

    public static int findItemInHotBar(Item item) {
        for (int index = 0; index < 9; index++) {
            if (mc.player.inventory.getItem(index).getItem().getItem() == item) {
                return index;
            }
        }
        return -1;
    }
}
