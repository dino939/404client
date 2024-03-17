package com.denger.client;


import by.radioegor146.nativeobfuscator.Native;
import com.denger.client.another.Events;
import com.denger.client.another.FriendManager;
import com.denger.client.another.Themes;
import com.denger.client.another.config.ConfigManager;
import com.denger.client.another.hooks.forge.even.addevents.EventManagerForge;
import com.denger.client.another.models.PetManager;
import com.denger.client.another.networkutills.EventsHandlerUtil;
import com.denger.client.another.resource.GifManager;
import com.denger.client.another.resource.ImageManager;
import com.denger.client.another.retransform.TransformManager;
import com.denger.client.another.settings.SettingManager;
import com.denger.client.another.sound.SoundManager;
import com.denger.client.modules.another.RegisterModule;
import com.denger.client.screens.main.MainScreen;
import com.denger.client.utils.font.FontManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Timer;

@Native
public class Main {
    public static Main getInstance;
    public static Minecraft mc = Minecraft.getInstance();
    public static FontManager fontManager;
    public static EventManagerForge eventManager;
    public Timer timer;
    public Execute execute;
    private RegisterModule registerModule;
    private SettingManager settingManager;
    private MainScreen mainScreen;
    private SoundManager soundManager;
    private FriendManager friendManager;
    private TransformManager transformManager;
    private long initTime;
    public boolean render = true;
    public boolean beta;
    public boolean panic = false;
    private GifManager gifManager;
    private PetManager petManager;
    public static boolean LShift;

    public Themes theme = Themes.Bloody;
    private Events events;
    private ConfigManager configManager;
    public static boolean optifine = false;

    public void init() {
        getInstance = this;
        initTime = System.currentTimeMillis();
        execute = new Execute(this);
        eventManager = new EventManagerForge(execute);
        fontManager = new FontManager();
        soundManager = new SoundManager();
        settingManager = new SettingManager();
        registerModule = new RegisterModule();
        mainScreen = new MainScreen();
        friendManager = new FriendManager();
        events = new Events();
        configManager = new ConfigManager();
        execute.hook();
        eventManager.register(events);
        eventManager.register(new EventsHandlerUtil());
        transformManager = new TransformManager();
        execute.start();
    }

    public void panic() {
        eventManager.unregister(this);
        eventManager.unregister(events);
        registerModule.getModules().forEach(module -> {
            if (module.getState()) {
                module.toggleWithOut();
            }
        });
        eventManager.unregister(registerModule);
        mc.screen = null;
        execute.panic();
        registerModule.panic();
        execute = null;
        friendManager = null;
        fontManager = null;
        soundManager = null;
        mainScreen = null;
        settingManager = null;
        //registerModule = null;
        //getInstance = null;
        System.gc();
        //Init.mainTheard.interrupt();
    }

    public SettingManager getSettingManager() {
        return settingManager;
    }
    public RegisterModule getRegisterModule() {
        return registerModule;
    }
    public MainScreen getMainScreen() {
        return mainScreen;
    }
    public SoundManager getSoundManager() {
        return soundManager;
    }
    public FriendManager getFriendManager() {
        return friendManager;
    }
    public Execute getExecute() {
        return execute;
    }
    public PetManager getPetManager() {
        return petManager;
    }
    public ConfigManager getConfigManager() {
        return configManager;
    }
    public long getInitTime() {
        return initTime;
    }

}
