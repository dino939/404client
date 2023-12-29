package com.denger.client.another;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;

public class FriendManager {
    private ArrayList<String> names = new ArrayList<>();

    public boolean isFriend(LivingEntity playerEntity) {
        return names.contains(playerEntity.getName().getString());
    }

    public void toggle(PlayerEntity playerEntity) {
        if (isFriend(playerEntity)) {
            addFriend(playerEntity);
        } else {
            removeFriend(playerEntity);
        }
    }

    public void addFriend(PlayerEntity playerEntity) {
        names.add(playerEntity.getName().getString());
    }

    public void removeFriend(PlayerEntity playerEntity) {
        names.remove(playerEntity.getName().getString());
    }

}
