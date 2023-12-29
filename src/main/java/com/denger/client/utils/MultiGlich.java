package com.denger.client.utils;

import java.util.ArrayList;

public class MultiGlich {
    ArrayList<GlichUtils> arrayList;
    public MultiGlich(int count){
        arrayList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            arrayList.add(new GlichUtils());
        }
    }
    public MultiGlich(int count,long time){
        arrayList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            arrayList.add(new GlichUtils(time));
        }
    }

    public void render(ArrayList<RenderInterface> renderInterfaces, float posYY, float height, float maxHight){
        for (GlichUtils glichUtils : arrayList){
            glichUtils.render(renderInterfaces,posYY,height,maxHight, arrayList.indexOf(glichUtils) == 0);
        }
    }
}
