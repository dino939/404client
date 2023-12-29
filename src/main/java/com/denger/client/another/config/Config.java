package com.denger.client.another.config;

import com.denger.client.modules.Module;
import com.denger.client.modules.mods.misc.SelfDestruct;
import com.google.gson.JsonObject;

import java.io.File;

import static com.denger.client.MainNative.getInstance;


public final class Config
        implements ConfigUpdater {
    private final String name;
    private final File file;

    public Config(String name) {
        this.name = name;
        this.file = new File(ConfigManager.configDirectory, name + ".json");

        if (!this.file.exists()) {
            try {
                this.file.createNewFile();
            } catch (Exception exception) {
            }
        }
    }


    public File getFile() {
        return this.file;
    }

    public String getName() {
        return this.name;
    }


    public JsonObject save() {
        JsonObject jsonObject = new JsonObject();
        JsonObject modulesObject = new JsonObject();

        for (Module module : getInstance.getRegisterModule().getModules()) {
            modulesObject.add(module.getName(), module.save());
        }

        jsonObject.add("Modules", modulesObject);

        return jsonObject;
    }


    public void load(JsonObject object) {
        if (object.has("Modules")) {
            JsonObject modulesObject = object.getAsJsonObject("Modules");
            for (Module module : getInstance.getRegisterModule().getModules()) {
                if (module.getClass() == SelfDestruct.class)continue;
                module.setState(false);
                module.load(modulesObject.getAsJsonObject(module.getName()));
            }
        }
    }
}


