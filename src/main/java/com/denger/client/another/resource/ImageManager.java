package com.denger.client.another.resource;

import com.denger.client.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.util.ResourceLocation;

import java.net.URL;
import java.util.HashMap;

public class ImageManager {

    // final NativeImage arrows;
    //public final NativeImage bubble;
    public static int anInt = 0;
    static HashMap<String, ResourceLocation> map = new HashMap<>();

    public ImageManager() {

        //particleHeart = getNativeImage("heart.png",false);
        //bubble = getNativeImage("bubble.png",false);
        //arrows = getNativeImage("arrow.png",false);
    }
    public static ResourceLocation getResource(String name) {
        if (map.get(name) != null) {
            return map.get(name);
        } else {
            try {
                map.put(name, Minecraft.getInstance().textureManager.register(name, new DynamicTexture(NativeImage.read(Utils.getResource("texture/img/"+name)))));
            }catch (Exception e){}
            return map.get(name);
        }
    }

    public static ResourceLocation getResourceDinamic(String url) {
        if (map.containsKey(url)) {
            return map.get(url);
        } else {
            try {
                map.put(url, Minecraft.getInstance().textureManager.register("dinamic_texture" + anInt,new DynamicTexture(NativeImage.read(new URL(url).openStream()))));
            }catch (Exception ignore){
            }
            anInt++;
            return map.get(url);
        }
    }
}
