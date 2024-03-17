package com.denger.client.utils;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.Texture;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import net.minecraft.util.Timer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.TimerTask;

import static com.denger.client.Main.getInstance;
import static com.denger.client.Main.mc;
import static com.denger.client.utils.rect.RenderUtil.resetColor;
import static com.mojang.blaze3d.platform.GlStateManager.*;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;

public class Utils {
    private static float Last;
    private static final Random random = new Random();
    private static final HashMap<Integer, FloatBuffer> kernelCache = new HashMap<>();
    public static float Yaw = 0;
    public static float Pitch = 0;

    public static void setupRender() {
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.disableAlphaTest();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.shadeModel(7425);
        //RenderSystem.depthMask(false);
    }

    public static InputStream getResource(String str) {
        return Utils.class.getClassLoader().getResourceAsStream("assets/minecraft/" + str);
    }

    public static void endRender() {
        //RenderSystem.depthMask(true);
        RenderSystem.shadeModel(7424);
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        RenderSystem.enableAlphaTest();
        RenderSystem.enableTexture();
        RenderSystem.enableDepthTest();
    }

    public static void drawSetup() {
        _disableTexture();
        _enableBlend();
        _blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    public static void drawFinish() {
        _enableTexture();
        _disableBlend();
        resetColor();
    }

    public static int getTextureId(ResourceLocation identifier) {
        Texture abstractTexture = mc.getTextureManager().getTexture(identifier);
        if (abstractTexture == null) {
            abstractTexture = new SimpleTexture(identifier);
            mc.getTextureManager().register(identifier, abstractTexture);
        }
        return abstractTexture.getId();
    }

    public static void PickItemTo(int firstSlot, int secondSlot) {
        assert mc.gameMode != null;
        assert mc.player != null;
        mc.gameMode.handleInventoryMouseClick(0, firstSlot, 0, ClickType.PICKUP, mc.player);
        mc.gameMode.handleInventoryMouseClick(0, secondSlot, 0, ClickType.PICKUP, mc.player);
        mc.gameMode.handleInventoryMouseClick(0, firstSlot, 0, ClickType.PICKUP, mc.player);
    }

    public static int getItemSlot(Item item) {
        assert mc.player != null;
        return mc.player.inventory.findSlotMatchingItem(new ItemStack(item));
    }

    public static FloatBuffer getKernel(int radius) {
        FloatBuffer buffer = kernelCache.get(radius);
        if (buffer == null) {
            buffer = BufferUtils.createFloatBuffer(radius);
            float[] kernel = new float[radius];
            float sigma = radius / 2.0F;
            float total = 0.0F;
            for (int i = 0; i < radius; i++) {
                float multiplier = i / sigma;
                kernel[i] = 1.0F / (Math.abs(sigma) * 2.50662827463F) * (float) Math.exp(-0.5 * multiplier * multiplier);
                total += i > 0 ? kernel[i] * 2 : kernel[0];
            }
            for (int i = 0; i < radius; i++) {
                kernel[i] /= total;
            }
            buffer.put(kernel);
            buffer.flip();
            kernelCache.put(radius, buffer);
        }
        return buffer;
    }

    public static Vector2f getMouseScaled(double mX, double mY) {
        int scale = mc.getWindow().calculateScale(mc.options.guiScale, mc.isEnforceUnicode());
        return new Vector2f((float) (mX * scale / 2), (float) (mY * scale / 2));
    }

    public static void bool(boolean state, Runnable True, Runnable False) {
        if (state) {
            True.run();
        } else {
            False.run();
        }
    }

    public static void sleepVoid(Runnable execute, int cooldown) {
        java.util.Timer timer = new java.util.Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                execute.run();
                timer.cancel();
            }
        }, cooldown);
    }

    public static float[] rotations(Entity entity, float yOffset) {
        if (entity == null) {
            return new float[]{0.0f, 0.0f};
        }
        try {
            double x = entity.getX() - mc.player.getX();
            double y = entity.getY() - (mc.player.getY() + (double) mc.player.getBbHeight() - (double) (mc.player.getEyeHeight() * yOffset));
            double z = entity.getZ() - mc.player.getZ();
            double u = MathHelper.sqrt(x * x + z * z);
            float u2 = (float) (MathHelper.atan2(z, x) * 57.29577951308232 - 90.0);
            float u3 = (float) (-MathHelper.atan2(y, u) * 57.29577951308232);
            return new float[]{u2, u3};
        } catch (Exception exception) {
            return new float[]{0.0f, 0.0f};
        }
    }

    public static float[] rotationsPro(Entity e, float yOffset) {
        if (e == null) {
            return new float[]{0.0f, 0.0f};
        }
        float[] rot = rotations(e, yOffset);
        float yrot = MathUtils.wrapDegrees(mc.player.yHeadRot + 90.0f - (rot[0] + 90.0f));
        return new float[]{mc.player.yHeadRot - yrot, rot[1]};
    }

    public static float getPartialTick() {
        return getInstance.timer.partialTick;
    }

    public static Vector3d getVecEntity(Entity e, float pt, float height) {
        return new Vector3d(e.xOld + (e.getX() - e.xOld) * pt
                - (e.getX() - e.xOld), e.yOld + (e.getY() - e.yOld) * pt
                - (e.getY() - e.yOld) + height, e.zOld + (e.getZ() - e.zOld) * pt
                - (e.getZ() - e.zOld));
    }

    public static MatrixStack get3DMatrix() {
        MatrixStack ms = new MatrixStack();
        ms.mulPose(Vector3f.XP.rotationDegrees(mc.getEntityRenderDispatcher().camera.getXRot()));
        ms.mulPose(Vector3f.YP.rotationDegrees(mc.getEntityRenderDispatcher().camera.getYRot() + 180));
        return ms;
    }

    public static boolean checkNot() {
        return mc.player == null;
    }

    public static String readInputStream(InputStream inputStream) {
        StringBuilder stringBuilder = new StringBuilder();

        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null)
                stringBuilder.append(line).append('\n');

        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }


    public static void SetTimer(float num) {
        if (num != Utils.Last) {
            Utils.Last = num;
            new ReflectFileld(getInstance.timer, Timer.class, 3).setValue(1000.0F / num);
        }
    }

    public static void ResetTimer() {
        if (Utils.Last == 20) return;
        SetTimer(20);
    }

    public static void setSession(Session session) {
        new ReflectFileld(mc, Minecraft.class, 23).setValue(session);
    }


    public static int findItem(Item item) {
        for (int index = 0; index < mc.player.inventoryMenu.slots.size(); index++) {
            if (mc.player.inventoryMenu.getSlot(index).getItem().getItem() == item) {
                return index;
            }
        }
        return -1;
    }

    public static int randomInt(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    public static String getKeyName(int num) {
        switch (num) {
            case 32:
                return "SPACE";
            case 39:
                return "APOSTROPHE";
            case 44:
                return "COMMA";
            case 45:
                return "MINUS";
            case 46:
                return "PERIOD";
            case 47:
                return "SLASH";
            case 48:
                return "0";
            case 49:
                return "1";
            case 50:
                return "2";
            case 51:
                return "3";
            case 52:
                return "4";
            case 53:
                return "5";
            case 54:
                return "6";
            case 55:
                return "7";
            case 56:
                return "8";
            case 57:
                return "9";
            case 59:
                return "SEMICOLON";
            case 61:
                return "EQUAL";
            case 65:
                return "A";
            case 66:
                return "B";
            case 67:
                return "C";
            case 68:
                return "D";
            case 69:
                return "E";
            case 70:
                return "F";
            case 71:
                return "G";
            case 72:
                return "H";
            case 73:
                return "I";
            case 74:
                return "J";
            case 75:
                return "K";
            case 76:
                return "L";
            case 77:
                return "M";
            case 78:
                return "N";
            case 79:
                return "O";
            case 80:
                return "P";
            case 81:
                return "Q";
            case 82:
                return "R";
            case 83:
                return "S";
            case 84:
                return "T";
            case 85:
                return "U";
            case 86:
                return "V";
            case 87:
                return "W";
            case 88:
                return "X";
            case 89:
                return "Y";
            case 90:
                return "Z";
            case 91:
                return "LEFT BRACKET";
            case 92:
                return "BACKSLASH";
            case 93:
                return "RIGHT BRACKET";
            case 96:
                return "GRAVE ACCENT";
            case 161:
                return "WORLD 1";
            case 162:
                return "WORLD 2";
            default:
                return getKeyNameFromGLFWDefault(num);
        }
    }

    private static String getKeyNameFromGLFWDefault(int num) {
        switch (num) {
            case 256:
                return "ESCAPE";
            case 257:
                return "ENTER";
            case 258:
                return "TAB";
            case 259:
                return "BACKSPACE";
            case 260:
                return "INSERT";
            case 261:
                return "DELETE";
            case 262:
                return "RIGHT";
            case 263:
                return "LEFT";
            case 264:
                return "DOWN";
            case 265:
                return "UP";
            case 266:
                return "PAGE UP";
            case 267:
                return "PAGE DOWN";
            case 268:
                return "HOME";
            case 269:
                return "END";
            case 280:
                return "CAPS LOCK";
            case 281:
                return "SCROLL LOCK";
            case 282:
                return "NUM LOCK";
            case 283:
                return "PRINT SCREEN";
            case 284:
                return "PAUSE";
            case 290:
                return "F1";
            case 291:
                return "F2";
            case 292:
                return "F3";
            case 293:
                return "F4";
            case 294:
                return "F5";
            case 295:
                return "F6";
            case 296:
                return "F7";
            case 297:
                return "F8";
            case 298:
                return "F9";
            case 299:
                return "F10";
            case 300:
                return "F11";
            case 301:
                return "F12";
            case 302:
                return "F13";
            case 303:
                return "F14";
            case 304:
                return "F15";
            case 305:
                return "F16";
            case 306:
                return "F17";
            case 307:
                return "F18";
            case 308:
                return "F19";
            case 309:
                return "F20";
            case 310:
                return "F21";
            case 311:
                return "F22";
            case 312:
                return "F23";
            case 313:
                return "F24";
            case 314:
                return "F25";
            case 320:
                return "KP 0";
            case 321:
                return "KP 1";
            case 322:
                return "KP 2";
            case 323:
                return "KP 3";
            case 324:
                return "KP 4";
            case 325:
                return "KP 5";
            case 326:
                return "KP 6";
            case 327:
                return "KP 7";
            case 328:
                return "KP 8";
            case 329:
                return "KP 9";
            case 330:
                return "KP DECIMAL";
            case 331:
                return "KP DIVIDE";
            case 332:
                return "KP MULTIPLY";
            case 333:
                return "KP SUBTRACT";
            case 334:
                return "KP ADD";
            case 335:
                return "KP ENTER";
            case 336:
                return "KP EQUAL";
            case 340:
                return "LEFT SHIFT";
            case 341:
                return "LEFT CONTROL";
            case 342:
                return "LEFT ALT";
            case 343:
                return "LEFT SUPER";
            case 344:
                return "RIGHT SHIFT";
            case 345:
                return "RIGHT CONTROL";
            case 346:
                return "RIGHT ALT";
            case 347:
                return "RIGHT SUPER";
            case 348:
                return "MENU";
            default:
                return "NONE";
        }

    }

    public static synchronized ByteBuffer createDirectByteBuffer(int capacity) {
        return ByteBuffer.allocateDirect(capacity).order(ByteOrder.nativeOrder());
    }


    public static FloatBuffer createDirectFloatBuffer(int capacity) {
        return createDirectByteBuffer(capacity << 2).asFloatBuffer();
    }

    public static IntBuffer createDirectIntBuffer(int capacity) {
        return createDirectByteBuffer(capacity << 2).asIntBuffer();
    }

    public static String getNextValue(ArrayList<String> texts, String text) {
        boolean found = false;
        for (int i = 0; i < texts.size(); i++) {
            if (found) {
                return texts.get(i);
            }
            if (texts.get(i).equals(text)) {
                found = true;
            }
        }
        if (found) {
            return texts.get(0);
        }
        return null;
    }

    public static String getPreviousValue(String[] texts, String text) {
        boolean found = false;
        for (int i = 1; i < texts.length; i++) {
            if (texts[i].equals(text)) {
                found = true;
                return texts[i - 1];
            }
        }
        if (found && texts[0].equals(text)) {
            return texts[texts.length - 1];
        }
        return null;
    }

    public static int getPd() {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        return Integer.parseInt(name.split("@")[0]);
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

    public interface User32 extends StdCallLibrary, WinUser, WinNT {
        User32 INSTANCE = Native.loadLibrary("user32", User32.class, W32APIOptions.DEFAULT_OPTIONS);

        int MB_OK = 0x00000000;
        int MB_ICONINFORMATION = 0x00000040;

        int IDOK = 1;
        int IDCANCEL = 2;

        int MB_OKCANCEL = 0x00000001;
        int MB_ICONERROR = 0x00000010;

        int IDYES = 6;
        int IDNO = 7;

        int MessageBox(WinDef.HWND hWnd, String text, String caption, int type);
    }

    public static void showMessage(String text, String title, Runnable ok, Runnable no) {

        User32 user32 = User32.INSTANCE;

        // Display b.a simple message box
        int result = user32.MessageBox(null, text, title, User32.MB_OK | User32.MB_ICONINFORMATION);

        // Check the result
        if (result == User32.IDOK) {
            ok.run();
        } else {
            no.run();
        }

    }

    public static String readFile(File file) {
        StringBuilder stringBuilder = new StringBuilder();

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null)
                stringBuilder.append(line).append('\n');

        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static double[] convertTo2D(double x, double y, double z) {
        FloatBuffer screenCords = BufferUtils.createFloatBuffer(3);
        IntBuffer viewport = BufferUtils.createIntBuffer(16);
        FloatBuffer modelView = BufferUtils.createFloatBuffer(16);
        FloatBuffer projection = BufferUtils.createFloatBuffer(16);
        GL11.glGetFloatv(2982, modelView);
        GL11.glGetFloatv(2983, projection);
        GL11.glGetIntegerv(2978, viewport);
        boolean result = Utils.gluProject((float) x, (float) y, (float) z, modelView, projection, viewport, screenCords);
        if (result) {
            return new double[]{screenCords.get(0), (float) mc.getWindow().getHeight() - screenCords.get(1), screenCords.get(2)};
        }
        return null;
    }

    public static Vector3d vectorTo2D(final int scaleFactor, final double x, final double y, final double z) {
        final float xPos = (float) x;
        final float yPos = (float) y;
        final float zPos = (float) z;
        final IntBuffer viewport = createDirectIntBuffer(16);
        final FloatBuffer modelview = createDirectFloatBuffer(16);
        final FloatBuffer projection = createDirectFloatBuffer(16);
        final FloatBuffer vector = createDirectFloatBuffer(4);
        GL11.glGetFloatv(2982, modelview);
        GL11.glGetFloatv(2983, projection);
        GL11.glGetIntegerv(2978, viewport);
        if (gluProject(xPos, yPos, zPos, modelview, projection, viewport, vector)) {
            return new Vector3d(vector.get(0) / scaleFactor, (mc.getWindow().getHeight() - vector.get(1)) / scaleFactor, vector.get(2));
        }
        return null;
    }

    public static boolean gluProject(float objx, float objy, float objz, FloatBuffer modelMatrix, FloatBuffer projMatrix, IntBuffer viewport, FloatBuffer win_pos) {
        return gluProjectUtil(objx, objy, objz, modelMatrix, projMatrix, viewport, win_pos);
    }

    public static boolean gluProjectUtil(float objx, float objy, float objz, FloatBuffer modelMatrix, FloatBuffer projMatrix, IntBuffer viewport, FloatBuffer win_pos) {
        float[] in = Utils.in;
        float[] out = Utils.out;
        in[0] = objx;
        in[1] = objy;
        in[2] = objz;
        in[3] = 1.0F;
        __gluMultMatrixVecf(modelMatrix, in, out);
        __gluMultMatrixVecf(projMatrix, out, in);
        if ((double) in[3] == 0.0) {
            return false;
        } else {
            in[3] = 1.0F / in[3] * 0.5F;
            in[0] = in[0] * in[3] + 0.5F;
            in[1] = in[1] * in[3] + 0.5F;
            in[2] = in[2] * in[3] + 0.5F;
            win_pos.put(0, in[0] * (float) viewport.get(viewport.position() + 2) + (float) viewport.get(viewport.position()));
            win_pos.put(1, in[1] * (float) viewport.get(viewport.position() + 3) + (float) viewport.get(viewport.position() + 1));
            win_pos.put(2, in[2]);
            return true;
        }
    }

    private static void __gluMultMatrixVecf(FloatBuffer m, float[] in, float[] out) {
        for (int i = 0; i < 4; ++i) {
            out[i] = in[0] * m.get(m.position() + i) + in[1] * m.get(m.position() + 4 + i) + in[2] * m.get(m.position() + 8 + i) + in[3] * m.get(m.position() + 12 + i);
        }

    }

    private static final float[] in = new float[4];
    private static final float[] out = new float[4];


}
