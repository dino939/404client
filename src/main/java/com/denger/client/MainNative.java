package com.denger.client;


import by.radioegor146.nativeobfuscator.Native;
import com.denger.client.another.Events;
import com.denger.client.another.FriendManager;
import com.denger.client.another.Themes;
import com.denger.client.another.config.ConfigManager;
import com.denger.client.another.hooks.forge.even.addevents.EventInit;
import com.denger.client.another.hooks.forge.even.addevents.EventManagerForge;
import com.denger.client.another.models.PetManager;
import com.denger.client.another.networkutills.EventsHandlerUtil;
import com.denger.client.another.resource.GifManagerNative;
import com.denger.client.another.resource.NativeManager;
import com.denger.client.another.settings.SettingManager;
import com.denger.client.another.sound.SoundManager;
import com.denger.client.modules.another.RegisterModule;
import com.denger.client.screens.main.MainScreen;
import com.denger.client.utils.font.FontManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Timer;
import net.minecraftforge.common.MinecraftForge;

@Native
public class MainNative {
    public static MainNative getInstance;
    public static Minecraft mc = Minecraft.getInstance();
    public static FontManager fontManager;
    public static EventManagerForge eventManager;
    public Timer timer;
    public ExecuteNative executeNative;
    private RegisterModule registerModule;
    private NativeManager nativeManager;
    private SettingManager settingManager;
    private MainScreen mainScreen;
    private SoundManager soundManager;
    private FriendManager friendManager;
    public boolean render = true;
    public boolean beta;
    public boolean panic = false;
    private GifManagerNative gifManager;
    public static boolean LShift;
    private PetManager petManager;
    private EventsHandlerUtil handlerUtil;
    public Themes theme = Themes.Weigh;

    private Events events;
    private ConfigManager configManager;

    public void init() {
        getInstance = this;
        executeNative = new ExecuteNative(this);
        eventManager = new EventManagerForge(this);
        fontManager = new FontManager();
        nativeManager = new NativeManager();
        gifManager = new GifManagerNative();
        gifManager.init();
        soundManager = new SoundManager();
        settingManager = new SettingManager();
        registerModule = new RegisterModule();
        mainScreen = new MainScreen();
        friendManager = new FriendManager();
        petManager = new PetManager();
        handlerUtil = new EventsHandlerUtil();
        events = new Events();
        configManager = new ConfigManager();
        executeNative.hook();
        executeNative.start();
        eventManager.register(events);
        eventManager.register(petManager);
        eventManager.register(handlerUtil);
        eventManager.register(this);
        eventManager.register(registerModule);
        MinecraftForge.EVENT_BUS.post(new EventInit());
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
        executeNative.panic();
        registerModule.panic();
        executeNative = null;
        nativeManager = null;
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

    public NativeManager getNativeManager() {
        return nativeManager;
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }

    public FriendManager getFriendManager() {
        return friendManager;
    }

    public ExecuteNative getExecute() {
        return executeNative;
    }

    public PetManager getPetManager() {
        return petManager;
    }

    public GifManagerNative getGifManager() {
        return gifManager;
    }

    public EventsHandlerUtil getHandlerUtil() {
        return handlerUtil;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}
