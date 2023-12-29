package com.denger.client.another.resource;

import com.denger.client.utils.TimerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;

public class Gif {
    static int num = 0;
    private final HashMap<Integer, ResourceLocation> frames;
    private int framesCount = 0;
    private int currentFrame = 0;
    private final TimerUtil util;
    private ImageReader imageReader;
    private int frameCooldown;

    public Gif(String url, int frameCooldown) {
        util = new TimerUtil();
        frames = new HashMap<>();
        this.frameCooldown = frameCooldown;

        try {
            InputStream in = new URL(url).openStream();
            ImageInputStream imageInputStream = ImageIO.createImageInputStream(in);
            if (imageInputStream != null) {
                imageReader = ImageIO.getImageReaders(imageInputStream).next();
                imageReader.setInput(imageInputStream);
                framesCount = imageReader.getNumImages(true);
                for (int i = 0; i < framesCount; i++) {
                    frames.put(i, createTextureFromFrame(i));
                }
            } else {
                throw new IOException("Ошибка при создании ImageInputStream из входного потока");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Gif(String url) {
        this(url, 100);
    }

    private ResourceLocation createTextureFromFrame(int index) throws IOException {
        BufferedImage frame = getFrame(index); // Получение изображения для кадра
        NativeImage nativeImage = NativeImage.read(Objects.requireNonNull(convertImageToPngInputStream(frame)));
        return Minecraft.getInstance().getTextureManager().register(num + "texture", new DynamicTexture(nativeImage));
    }

    private BufferedImage getFrame(int index) throws IOException {
        BufferedImage bufferedImage = imageReader.read(index);
        if (bufferedImage == null) {
            throw new IOException("Ошибка при чтении кадра " + index);
        }
        return bufferedImage;
    }

    private static InputStream convertImageToPngInputStream(BufferedImage image) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", outputStream);
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    public int getCurrentCount() {
        if (util.hasReached(frameCooldown)) {
            currentFrame++;
            if (currentFrame >= framesCount) {
                currentFrame = 0;
            }
            util.reset();
        }
        return currentFrame;
    }

    public ResourceLocation getResource() {
        return frames.get(getCurrentCount());
    }

    public ResourceLocation getResource(int procent) {
        int count = (int) ((framesCount - 1) * (procent / 100.0));
        if (frames.containsKey(count)) {
            return frames.get(count);
        } else {
            try {
                frames.put(count, createTextureFromFrame(count));
                return frames.get(count);
            } catch (IOException e) {
                e.printStackTrace();
                return frames.get(0);
            }
        }
    }

}