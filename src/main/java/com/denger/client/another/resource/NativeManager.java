package com.denger.client.another.resource;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.util.ResourceLocation;

import java.net.URL;
import java.util.HashMap;

public class NativeManager {
    public final NativeImage capeNative;
    public final NativeImage capeNative2;
    public final NativeImage logo;
    public final NativeImage image;
    public final NativeImage particleHeart;
    public final NativeImage arrows;
    public final NativeImage bubble;
    public final NativeImage extazyy;
    public static int anInt = 0;
    static HashMap<String, ResourceLocation> map = new HashMap<>();

    public NativeManager() {
        capeNative = getNativeImage("https://i.imgur.com/JPpaPlS.png");
        capeNative2 = getNativeImage("https://i.imgur.com/OJwxt12.png");
        logo = getNativeImage("https://i.imgur.com/wu6Z7NC.png");
        image = getNativeImage("https://i.imgur.com/ODRtAOH.png");
        particleHeart = getNativeImage("https://i.imgur.com/lQU5k2f.png");
        bubble = getNativeImage("https://i.imgur.com/Rk73tcJ.png");
        arrows = getNativeImage("https://i.imgur.com/ZWcasgr.png");
        extazyy = getNativeImage("https://i.imgur.com/iCktLC8.png");
    }

    public NativeImage getNativeImage(String url) {
        try {
            return NativeImage.read(new URL(url).openStream());

        } catch (Exception ignored) {
            return null;
        }

    }

    public static ResourceLocation getResource(String name, NativeImage nativeImage) {
        if (map.get(name) != null) {
            return map.get(name);
        } else {
            map.put(name, Minecraft.getInstance().textureManager.register(name, new DynamicTexture(nativeImage)));
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
