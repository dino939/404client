package com.denger.client.another.resource;

import by.radioegor146.nativeobfuscator.Native;
import com.denger.client.utils.Utils;
import net.minecraft.util.ResourceLocation;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

@Native
public class GifManager {
    static HashMap<String, Gif> map = new HashMap<>();
    private static InputStream getPath(String file){
        return Utils.getResource("texture/gifs/"+file+".gif");
    }

    public static Gif getResource(String name) {
        if (map.get(name) != null) {
            return map.get(name);
        } else {
            try {
                map.put(name, new Gif(getPath(name)));
            }catch (Exception e){}
            return map.get(name);
        }
    }
}
