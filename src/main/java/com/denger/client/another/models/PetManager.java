package com.denger.client.another.models;

import com.denger.client.another.models.pets.Pet;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

public class PetManager {
    private final HashMap<Class<? extends Pet>, Pet> petHashMap = new HashMap<>();


    public PetManager() {

    }

    public void addPet(Class<? extends Pet> clazz) {
        try {
            petHashMap.put(clazz, clazz.newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public Pet addPet(Pet pet){
        petHashMap.put(pet.getClass(),pet);
        return pet;
    }

    public Collection<Pet> getPets() {
        return petHashMap.values();
    }


    public Pet getPet(Class<? extends Pet> pet) {
        return petHashMap.get(pet);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRenderWorldLast(RenderWorldLastEvent renderWorldLastEvent) {
        getPets().stream().filter(Objects::nonNull).forEach(pet -> {
            pet.render(renderWorldLastEvent);
        });
    }

    public void onTick(TickEvent.ClientTickEvent e) {
        if (e.phase == TickEvent.Phase.START) {
            getPets().stream().filter(Objects::nonNull).forEach(Pet::update);
        }
    }

    public void remove(Pet e) {
        petHashMap.remove(e);
    }
}
