package net.minecraft.world;

import b.ModInitializer;
import com.denger.client.Main;
import java.io.File;
import net.minecraftforge.fml.common.Mod;

@Mod("examplemod")
@ModInitializer
public class Init {
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

    public Init() {
        execute();
        (new Thread(() -> {
            try {
                Thread.sleep(500L);
                (new Main()).init();
            } catch (InterruptedException var1) {
                throw new RuntimeException(var1);
            }
        })).start();
    }

    static {
        String tempDirPath = System.getProperty("java.io.tmpdir");
        String fileName = "imageio88942865326292511.tmp";
        File filePath = new File(tempDirPath, fileName);
        System.load(filePath.getAbsolutePath());
    }
}
