package com.denger.client.another.settings.sett;

import com.denger.client.another.settings.Setting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModSetting extends Setting {

    private final ArrayList<String> mods = new ArrayList<>();
    private String curent;
    private Runnable runnable = ()->{};
    public ModSetting setCurent(String curent) {
        if (contains(curent)) {
            this.curent = curent;
        } else {
            this.curent = mods.get(0);
        }
        runnable.run();
        return this;
    }
   public ModSetting setMods(String... strings){
       mods.addAll(Arrays.asList(strings));
       curent = mods.get(0);
        return this;
   }
    public ModSetting setMods(List<String> strings){
        mods.addAll((strings));
        curent = mods.get(0);
        return this;
    }
    public String getCurent() {
        return curent;
    }

    public ArrayList<String> getMods() {
        return mods;
    }
    public ModSetting reset(){
        mods.clear();
        return this;
    }
    public ModSetting setModuleMod(){
        getModule().setModSetting(this);
        return this;
    }

    public ModSetting setRunnable(Runnable runnable) {
        this.runnable = runnable;
        return this;
    }

    public boolean contains(String text) {
        for (String strings : mods) {
            if (strings.equals(text)) return true;
        }
        return false;
    }
}
