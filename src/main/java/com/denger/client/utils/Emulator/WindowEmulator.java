package com.denger.client.utils.Emulator;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class WindowEmulator {

    // Window size
    private int width = 800;
    private int height = 600;
    public static int texture = 0;

    public static void main(String[] args) {
        new WindowEmulator().run();
    }

    private String title = "OBS ByPassWindow";

    // The window handle
    private long window;

    public void run() {
        init();
        loop();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        // Setup an error callback
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // Window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // Window will be resizable

        // Create the window
        window = glfwCreateWindow(width, height, title, NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        // Set resize callback
        glfwSetFramebufferSizeCallback(window, (win, newWidth, newHeight) -> {
            this.width = newWidth;
            this.height = newHeight;
            glViewport(0, 0, newWidth, newHeight);
        });

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);

        // Create OpenGL capabilities
        GL.createCapabilities();

        // Set swap interval and show the window
        glfwSwapInterval(1);
        glfwShowWindow(window);
    }

    private void loop() {
        RSColor(-1);
        while (!glfwWindowShouldClose(window)) {
            // Ensure context is current in the loop
            GL.createCapabilities();

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            EmulatorUtil.downloadImage("https://cdn.discordapp.com/attachments/1029135008583135272/1174022760255721553/image.png?ex=656614e6&is=65539fe6&hm=0e23c40a053c90ecbc209d877ea7276c8388a20691ce4f3eab8659b7351236db&",-10,-10,100,100);

            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }
    public static void RSColor(final int n) {
        GL11.glColor4f((n >> 16 & 0xFF) / 255.0f, (n >> 8 & 0xFF) / 255.0f, (n & 0xFF) / 255.0f, (n >> 24 & 0xFF) / 255.0f);
    }

    public long getWindow() {
        return window;
    }
}
