package com.denger.client.another.retransform;

import com.denger.client.another.retransform.hook.PlayerControllerHook;
import com.denger.client.another.retransform.types.reWrite;
import net.minecraft.client.multiplayer.PlayerController;

import java.util.ArrayList;
import java.util.List;

public class TransformManager {
    List<AbstractTransformer> transformers = new ArrayList<>();
    public TransformManager(){
    transformers.add(new reWrite(PlayerController.class, PlayerControllerHook.class,2));
    //    String targetName = PlayerController.class.getName().replace(".", "/");
    //    File f = new File(mc.gameDirectory,"choooo.class");
    //    try {
    //        a.redefineClass(targetName,a.readAllBytes(f.toURL().openStream()));
    //    } catch (IOException e) {
    //        throw new RuntimeException(e);
    //    }

        transformers.forEach(AbstractTransformer::transform);
    }


}
