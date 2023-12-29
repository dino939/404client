package com.denger.client.another.models.pets;

import net.minecraft.entity.Entity;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class FirstPet extends Pet{

    public FirstPet(Entity e){
        super(e);
    }

    @Override
    public void render(RenderWorldLastEvent renderWorldLastEvent) {
        super.render(renderWorldLastEvent);

    }
}
