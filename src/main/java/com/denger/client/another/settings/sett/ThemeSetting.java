package com.denger.client.another.settings.sett;

import com.denger.client.another.settings.Setting;
import com.denger.client.another.Themes;

import static com.denger.client.Main.getInstance;

public class ThemeSetting extends Setting {
    private Themes theme;
    public ThemeSetting setTheme(Themes theme){
        this.theme = theme;
        return this;
    }
    public void setThisTheme(){
        getInstance.theme = theme;
    }

    public Themes getTheme() {
        return theme;
    }
}
