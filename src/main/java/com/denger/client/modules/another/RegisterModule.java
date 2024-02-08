package com.denger.client.modules.another;

import com.denger.client.another.settings.Setting;
import com.denger.client.another.settings.SettingTarget;
import com.denger.client.modules.Module;
import com.denger.client.modules.ShineModule;
import com.denger.client.modules.mods.combat.*;
import com.denger.client.modules.mods.hud.Design;
import com.denger.client.modules.mods.hud.ModuleList;
import com.denger.client.modules.mods.hud.Overlay;
import com.denger.client.modules.mods.misc.*;
import com.denger.client.modules.mods.render.*;
import com.denger.client.utils.Crypt;
import net.minecraft.world.Init;

import java.lang.reflect.Field;
import java.util.*;

import static com.denger.client.Main.getInstance;

public class RegisterModule {
    private ArrayList<Module> modules;
    private ArrayList<ShineModule> modulesShine;
    private HashMap<Class<? extends Module>, Module> modulesInstance = new HashMap<>();

    public RegisterModule() {
        modules = new ArrayList<>();
        modulesShine = new ArrayList<>();
        addModules(
                NoHurtCam.class,
                Design.class,
                TriggerBot.class,
                CustomCape.class,
                ModuleList.class,
                Trails.class,
                FogColor.class,
                SwingAnimation.class,
                ViewModel.class,
                NoFireOverlay.class,
                InventoryThrow.class,
                DethMove.class,
                DashParticle.class,
                Velocity.class,
                NoEffect.class,
                FreeCam.class,
                AutoSprint.class,
                SelfDestruct.class,
                HitBox.class,
                NoHitBoxes.class,
                AutoTotem.class,
                MiddleClickPearl.class,
                GlowEsp.class,
                ShieldBreak.class,
                NoPush.class,
                ItemSroller.class,
                FullBright.class,
                NoBreakDelay.class,
                FastUse.class,
                GuiWalk.class,
                XRay.class,
                XRayBypass.class,
                WorldTime.class,
                Tracers.class,
                ElytraSwap.class,
                AutoRespawn.class,
                NoJumpDelay.class,
                ChestStealer.class,
                NameTag2.class,
                FakePlayer.class,
                AntiAfk.class,
                Scaffold.class,
                HitBubble.class,
                Aura.class,
                TargetEsp.class,
                AutoGapple.class,
                NoPacket.class,
                NoCameraClip.class,
                Hat.class,
                Criticals.class,
                SystemSounds.class,
                AutoFish.class,
                DripMode.class,
                CustomHand.class,
                NoInteract.class,
                NameProtect.class
        );
        if (Init.UID().equals("2")) {
            addModules(
                    Overlay.class,
                    DethCoords.class,
                    NoAttackFriends.class,
                    MiddleClickFriend.class,
                    AutoPotion.class

            );
        }
        modules.sort((person1, person2) -> {
            String firstLetter1 = Crypt.decrypt(String.valueOf(person1.getName().charAt(0)));
            String firstLetter2 = Crypt.decrypt(String.valueOf(person2.getName().charAt(0)));
            return Character.compare(firstLetter1.charAt(0), firstLetter2.charAt(0));
        });

    }

    public ArrayList<ShineModule> getModulesShine() {
        return modulesShine;
    }

    public <T extends Module> T getModule(Class<T> module) {
        if (modulesInstance.containsKey(module)) {
            return (T) modulesInstance.get(module);
        } else {
            modulesInstance.put(module, modules.stream().filter(module1 -> {
                return module == module1.getClass();
            }).findFirst().get());
            return (T) modulesInstance.get(module);
        }
    }

    public boolean isEnable(Class<? extends Module> module) {
        try {
            if (modulesInstance.containsKey(module)) {
                return modulesInstance.get(module).getState();
            } else {
                modulesInstance.put(module, modules.stream().filter(Objects::nonNull).filter(module1 -> {
                    return module == module1.getClass();
                }).findFirst().get());
                return modulesInstance.get(module).getState();
            }
        } catch (NoSuchElementException e) {
            return false;
        }

    }

    @SafeVarargs
    public final void addModules(Class<? extends Module>... moduleClass) {
        Arrays.asList(moduleClass).forEach(m -> {
            Module mod = createModule(m);
            modules.add(mod);
            if (mod instanceof ShineModule) {
                modulesShine.add((ShineModule) mod);
            }
        });
    }

    private Module createModule(Class<? extends Module> clazz) {
        Module module;
        try {
            try {
                module = clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new Exception(e.getMessage());
            }


            ModuleTarget annotation = clazz.getAnnotation(ModuleTarget.class);
            if (annotation == null) return null;

            module.setName(annotation.ModName());
            module.setDescription(annotation.description());
            module.setKeycode(annotation.bind());
            module.setCategory(annotation.category());
            module.setBeta(annotation.beta());
            module.setCooldown(annotation.cooldown());
            if (annotation.enable()) {
                module.setState(true);
            }


        } catch (Exception e) {
            System.out.println("Error on bild class:" + clazz.getName());
            System.out.println("Error:" + e.getMessage());
            return null;
        }
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            try {
                SettingTarget annotat = field.getAnnotation(SettingTarget.class);
                if (annotat != null) {
                    Setting setting = (Setting) field.get(module);
                    setting.setName(annotat.name());
                    setting.setModule(module);
                    module.getSettings().add(setting);
                    getInstance.getSettingManager().addSett(setting);
                    setting.setToRender(annotat.toAdd());

                }
            } catch (IllegalAccessException e) {

            }
        }
        return module;
    }

    public ArrayList<Module> getModules() {
        return modules;
    }

    public void panic() {
        modules = new ArrayList<>();
        modulesInstance = new HashMap<>();
    }
}
