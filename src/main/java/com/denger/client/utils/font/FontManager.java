package com.denger.client.utils.font;

import net.minecraft.world.Init;


public class FontManager {

    public GlyphPageFontRenderer font20;
    public GlyphPageFontRenderer font24;
    public GlyphPageFontRenderer font28;
    public GlyphPageFontRenderer font16;
    public GlyphPageFontRenderer font12;
    public GlyphPageFontRenderer font10;
    public GlyphPageFontRenderer code16;
    public GlyphPageFontRenderer code20;

    public GlyphPageFontRenderer Gui1;
    public GlyphPageFontRenderer Gui2;

    public GlyphPageFontRenderer ico40;

    public GlyphPageFontRenderer ico28;
    public GlyphPageFontRenderer ico16;

    public GlyphPageFontRenderer mainmenu;

    public FontManager(){
        font20 = GlyphPageFontRenderer.createFromBytes(Init.font1(), 20, false, false, false);
        font24 = GlyphPageFontRenderer.createFromBytes(Init.font1(), 24, false, false, false);
        Gui1 = GlyphPageFontRenderer.createFromBytes(Init.font1(), 28, false, false, false);
        Gui2 = GlyphPageFontRenderer.createFromBytes(Init.font1(), 12, false, false, false);
        mainmenu = GlyphPageFontRenderer.createFromBytes(Init.font2(), 80, false, false, false);
        font28 = GlyphPageFontRenderer.createFromBytes(Init.font2(), 28, false, false, false);
        ico40 = GlyphPageFontRenderer.createFromBytes(Init.font3(), 40, false, false, false);
        ico28 = GlyphPageFontRenderer.createFromBytes(Init.font3(), 28, false, false, false);
        ico16 = GlyphPageFontRenderer.createFromBytes(Init.font3(), 16, false, false, false);
        code16 = GlyphPageFontRenderer.createFromBytes(Init.font4(), 16, false, false, false);
        code20 = GlyphPageFontRenderer.createFromBytes(Init.font4(), 20, false, false, false);
        font16 = GlyphPageFontRenderer.createFromBytes(Init.font1(), 16, false, false, false);
        font12 = GlyphPageFontRenderer.createFromBytes(Init.font1(), 12, false, false, false);
        font10 = GlyphPageFontRenderer.createFromBytes(Init.font1(), 10, false, false, false);

    }



}
