package com.denger.client.another.settings;



import java.util.ArrayList;
import java.util.Arrays;

public class SettingManager {
    private final ArrayList<Setting> settings;
    public SettingManager(){
        settings = new ArrayList<>();
    }
    public ArrayList<Setting> getSettings() {
        return settings;
    }

    public void addSett(Setting... settings2) {
        settings.addAll(Arrays.asList(settings2));
    }

}
