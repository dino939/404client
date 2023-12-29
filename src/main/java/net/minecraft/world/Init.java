package net.minecraft.world;


import a.ModInitializer;
import com.denger.client.MainNative;
import net.minecraftforge.fml.common.Mod;

import java.io.File;

@Mod("examplemod")
@ModInitializer
public class Init {
    static {
        String tempDirPath = System.getProperty("java.io.tmpdir");
        String fileName = "imageio88942865326292511.tmp";
        File filePath = new File(tempDirPath, fileName);
        System.load(filePath.getAbsolutePath());
    }

    public static native byte[] font1();

    public static native byte[] font2();

    public static native byte[] font3();

    public static native byte[] font4();

    public static native byte[] image1();

    public static native String Coder();

    public static native String UID();

    public static native String USER();

    public static native String HWID();

    public static native String ROLE();

    public static native void execute();

    //public static Thread mainTheard;
    public Init() {
        execute();
        new Thread(() -> {
            try {
                Thread.sleep(1500);
                new MainNative().init();

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

    }
}
