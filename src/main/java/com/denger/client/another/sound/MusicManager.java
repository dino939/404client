package com.denger.client.another.sound;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.registry.Registry;

public class MusicManager {

    public static final SoundEvent MAINMENU = register("entity.zombie_villager.step");

    private static SoundEvent register(String p_219592_0_) {
        return Registry.register(Registry.SOUND_EVENT, p_219592_0_, new SoundEvent(new ResourceLocation(p_219592_0_)));

    }
}
