package com.denger.client.another.settings.sett;

import com.denger.client.another.settings.Setting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MultiBoolSetting extends Setting {
    private List<BoolSetting> boolSettings = new ArrayList<>();

    public MultiBoolSetting addBools(BoolSetting... boolSettings2) {
        boolSettings.addAll(Arrays.asList(boolSettings2));
        return this;
    }
    public List<BoolSetting> getBoolSettings() {
        return boolSettings;
    }
}

