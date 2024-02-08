package com.denger.client.modules.mods.render;

import com.denger.client.another.models.pets.FirstPet;
import com.denger.client.another.models.pets.Pet;
import com.denger.client.modules.Module;
import com.denger.client.modules.another.Category;
import com.denger.client.modules.another.ModuleTarget;

import static com.denger.client.Main.getInstance;
import static com.denger.client.Main.mc;
@ModuleTarget(ModName = "",category = Category.RENDER)
public class Petst extends Module {

    Pet pet;


    @Override
    public void onEnable() {
        super.onEnable();
        pet = getInstance.getPetManager().addPet(new FirstPet(mc.player));
    }

    @Override
    public void onDisable() {
        getInstance.getPetManager().remove(pet);
        pet = null;
    }
}
