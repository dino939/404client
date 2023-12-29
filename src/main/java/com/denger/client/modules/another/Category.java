package com.denger.client.modules.another;

public enum Category {
    COMBAT("Combat"),
    RENDER("Render"),
    HUD("Hud"),
    MISC("Misc");
    public final String name;
    Category(String name) {
        this.name = name;
    }
}