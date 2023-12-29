package com.denger.client.utils;

import net.minecraft.entity.Entity;

import java.util.ArrayList;

public class EntityUtil {

    private static ArrayList<Entity> entitiesIgnore = new ArrayList<>();

    public static void clearIgnEntitys() {
        entitiesIgnore.clear();
    }

    public static void addEntity(Entity e) {
        if (!entitiesIgnore.contains(e)) entitiesIgnore.add(e);
    }

    public static boolean isInclude(Entity e) {
        return entitiesIgnore.contains(e);
    }

    public static void removeEntity(Entity e) {
        entitiesIgnore.add(e);
    }

}
