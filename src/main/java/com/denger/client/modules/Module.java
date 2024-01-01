package com.denger.client.modules;

import com.denger.client.another.networkutills.ConnectionUtil;
import com.denger.client.another.settings.Setting;
import com.denger.client.another.settings.sett.*;
import com.denger.client.modules.another.Category;
import com.denger.client.utils.Utils;
import com.google.gson.JsonObject;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;

import java.awt.*;
import java.util.ArrayList;
import java.util.function.Supplier;

import static com.denger.client.MainNative.eventManager;
import static com.denger.client.MainNative.getInstance;

public abstract class Module {

    private String name, description;
    private int keycode;
    private Category category;
    private boolean state, beta;

    private Supplier<Boolean> visible = () -> true;
    private ArrayList<Setting> settings = new ArrayList<>();
    private int cooldown = 0;
    private ModSetting modSetting;
    protected static Tessellator tessellator = Tessellator.getInstance();
    protected static BufferBuilder bufferbuilder = tessellator.getBuilder();

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void preDisable() {
    }

    public void toggle() {
        state = !state;
        setState(state);
    }

    public void toggleWithOut() {
        state = !state;
        setState(state);
    }

    public boolean onPacket(Object packet, ConnectionUtil.Side side) {
        return true;
    }

    public void setState(boolean state) {
        this.state = state;
        if (state) {
            onEnable();
            eventManager.register(this);
        } else {
            preDisable();
            Utils.sleepVoid(() -> {
                if (this.state) return;
                eventManager.unregister(this);
                try {
                    onDisable();
                } catch (Exception ignore) {
                }

            }, cooldown);

        }
    }


    public JsonObject save() {
        JsonObject object = new JsonObject();
        if (this.state)
            object.addProperty("enable", Boolean.valueOf(this.state));
        if (this.keycode > -1)
            object.addProperty("keyIndex", Integer.valueOf(this.keycode));
        JsonObject propertiesObject = new JsonObject();
        for (Setting set : getSettings()) {
            if (set instanceof BoolSetting) {
                propertiesObject.addProperty(set.getName(), ((BoolSetting) set).getState());
                continue;
            }
            if (set instanceof ModSetting) {
                propertiesObject.addProperty(set.getName(), ((ModSetting) set).getCurent());
                continue;
            }
            if (set instanceof FloatSetting) {
                propertiesObject.addProperty(set.getName(), ((FloatSetting) set).getVal());
                continue;
            }
            if (set instanceof ThemeSetting) {
                propertiesObject.addProperty(set.getName(), getInstance.theme == ((ThemeSetting) set).getTheme());
                continue;
            }
            if (set instanceof ColorSetting) {
                propertiesObject.addProperty(set.getName(), ((ColorSetting) set).getColor().getRGB());
                continue;
            }
        }
        object.add("Settings", propertiesObject);
        return object;
    }


    public void load(JsonObject object) {
        if (object != null) {
            if (object.has("enable")) {
                setState(object.get("enable").getAsBoolean());
            }

            if (object.has("keyIndex")) {
                this.keycode = object.get("keyIndex").getAsInt();
            }

            for (Setting set : getSettings()) {
                JsonObject propertiesObject = object.getAsJsonObject("Settings");
                if (set == null)
                    continue;
                if (propertiesObject == null)
                    continue;
                if (!propertiesObject.has(set.getName()))
                    continue;
                if (set instanceof BoolSetting) {
                    ((BoolSetting) set).setBol(propertiesObject.get(set.getName()).getAsBoolean());
                    continue;
                }
                if (set instanceof ModSetting) {
                    ((ModSetting) set).setCurent(propertiesObject.get(set.getName()).getAsString());
                    continue;
                }
                if (set instanceof FloatSetting) {
                    ((FloatSetting) set).setVal(propertiesObject.get(set.getName()).getAsFloat());
                    continue;
                }
                if (set instanceof ThemeSetting) {
                    if (propertiesObject.get(set.getName()).getAsBoolean()) {
                        getInstance.theme = ((ThemeSetting) set).getTheme();
                    }
                    continue;
                }
                if (set instanceof ColorSetting) {
                    ((ColorSetting) set).setColor(new Color(propertiesObject.get(set.getName()).getAsInt()));
                    continue;
                }
            }
        }
    }


    public void setCategory(Category category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setKeycode(int keycode) {
        this.keycode = keycode;
    }

    public void setBeta(boolean beta) {
        this.beta = beta;
    }

    public void setVisible(boolean visible) {
        this.visible = () -> visible;
    }

    public void setVisible(Supplier<Boolean> visible) {
        this.visible = visible;
    }

    public void setModSetting(ModSetting modSetting) {
        this.modSetting = modSetting;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public ModSetting getModSetting() {
        return modSetting;
    }

    public boolean getVisible() {
        return beta ? getInstance.beta ? visible.get() : false : visible.get();
    }

    public int getKeycode() {
        return keycode;
    }

    public ArrayList<Setting> getSettings() {
        return settings;
    }

    public Category getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean getState() {
        return state;
    }

    public boolean isBeta() {
        return beta;
    }
}
