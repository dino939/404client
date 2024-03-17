package com.denger.client.another.resource;

import com.denger.client.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.util.ResourceLocation;

import java.net.URL;
import java.util.HashMap;

public class ImageManager {
    static HashMap<String, ResourceLocation> map = new HashMap<>();

    public static ResourceLocation getResource(String name) {
        if (map.containsKey(name)) {
            return map.get(name);
        } else {
            try {
                if (name.contains("http"))
                    map.put(name, Minecraft.getInstance().textureManager.register(name, new DynamicTexture(NativeImage.read(new URL(name).openStream()))));
                else
                    map.put(name, Minecraft.getInstance().textureManager.register(name, new DynamicTexture(NativeImage.read(Utils.getResource("texture/img/" + name)))));

            } catch (Exception err) {
                throw new RuntimeException(err);
            }
            return map.get(name);
        }
    }

}
