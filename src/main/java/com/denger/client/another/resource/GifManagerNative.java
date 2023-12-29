package com.denger.client.another.resource;

import by.radioegor146.nativeobfuscator.Native;

import java.util.ArrayList;

@Native
public class GifManagerNative {

    ArrayList<Gif> gifs = new ArrayList<>();
    public Gif dash_X;
    public Gif dash_infinity;
    public Gif dash_line;
    public Gif dash_BX;
    public Gif dash_cirle;

    public void init() {
        try {
            gifs.add(new Gif("https://i.imgur.com/VhZIC3H.gif"));
            gifs.add(new Gif("https://i.imgur.com/OUTtw6d.gif", 25));
            dash_X = new Gif("https://i.imgur.com/cEmzLdG.gif");
            dash_infinity = new Gif("https://i.imgur.com/n1oRYKa.gif");
            dash_line = new Gif("https://i.imgur.com/yn2XbRg.gif");
            dash_BX = new Gif("https://i.imgur.com/daBF8Yl.gif");
            dash_cirle = new Gif("https://i.imgur.com/ysjmLYu.gif");

        } catch (Exception ignored) {
        }
    }

    public ArrayList<Gif> getGifs() {
        return gifs;
    }
}
