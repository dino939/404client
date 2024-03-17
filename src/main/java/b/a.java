package b;

import com.denger.client.utils.ReflectFileld;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class a {
    public static native Class<?> defineClass(ClassLoader var0, byte[] var1, int var2);

    public static native boolean redefineClass(String name, byte[] bytes);

    public static native byte[] getClassBytes(String name);
    public static native void setFinalField(Object instance,Class<?> clazz,int id,Object value);
    private static Map<String, byte[]> assets = new HashMap<>();

    static {
        System.load("D:\\AdvancedConvertor\\Native-Lib\\x64\\Debug\\Native-Lib.dll");
    }

    public static byte[] getResource(String str) {
        if (assets.containsKey(str)) return assets.get(str);
        assets.put(str, readAllBytes(a.class.getClassLoader().getResourceAsStream(str)));
        return assets.get(str);
    }

    public static byte[] readAllBytes(InputStream is) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[0xFFFF];
        try {
            for (int len = is.read(buffer); len != -1; len = is.read(buffer))
                baos.write(buffer, 0, len);
        } catch (Exception ignored) {
        }
        return baos.toByteArray();
    }

    public static Class<?> MainAnnotation() {
        return ModInitializer.class;
    }

    ;
}