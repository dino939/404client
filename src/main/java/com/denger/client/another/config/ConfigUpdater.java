package com.denger.client.another.config;

import com.google.gson.JsonObject;

public interface ConfigUpdater {
    JsonObject save();

    void load(JsonObject paramJsonObject);
}

 