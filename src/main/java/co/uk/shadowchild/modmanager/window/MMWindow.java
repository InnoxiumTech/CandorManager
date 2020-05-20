package co.uk.shadowchild.modmanager.window;

import co.uk.shadowchild.modmanager.Resources;
import co.uk.shadowchild.modmanager.util.config.Configuration;
import co.uk.shadowchild.modmanager.window.handler.KeyHandler;
import co.uk.shadowchild.modmanager.window.handler.MouseHandler;
import co.uk.shadowchild.modmanager.window.render.Texture;
import lwjgui.scene.Scene;
import lwjgui.scene.Window;
import lwjgui.scene.WindowManager;
import lwjgui.scene.control.Label;
import lwjgui.scene.layout.StackPane;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;

import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class MMWindow {

    // Is GLFW Initlized?
    public boolean glfwInitialized;
    // The Window Handle for GLFW
    private long handle;
    // Our GLFW VideoMode
    private GLFWVidMode vidMode;
    // Our GLCapabilities
    private GLCapabilities caps;
    //  The size of our window
    private Vector2i size;
    // Our window title
    private String title;
    // The monitor to show on
    private long monitor;
    // The monitor id for exclusive fullscreen
    private long monitorShare;
    // Our LWJGUI Window instance
    private Window renderWindow;

    public MMWindow(Vector2i size, String title) {

        this(size, title, NULL, NULL);
    }

    public MMWindow(Vector2i size, String title, long monitor, long share) {

        this.size = size;
        this.title = title;
        this.monitor = monitor;
        this.monitorShare = share;
    }

    public void loadResources() {

        Resources.test = new Texture("./test.png");
    }

    public void init() {

        // Set up an error callback, will send any native errors to java logger
        GLFWErrorCallback.createPrint(System.err).set();

        // See if we can initialize GLFW
        if (!glfwInit()) {

            throw new IllegalStateException("Failed to initialize GLFW!");
        }
        glfwInitialized = true;

        // Now GLFW is initialized, we can create a window.
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        handle = glfwCreateWindow(size.x, size.y, title, monitor, monitorShare);
        if (handle == NULL) {

            throw new IllegalStateException("Could not create GLFW Window!");
        }

        // Setup a mouse and key callbacks
        glfwSetKeyCallback(handle, KeyHandler.getKeyHandler());
        glfwSetCursorPosCallback(handle, MouseHandler.getCursorPosCallback());
        glfwSetMouseButtonCallback(handle, MouseHandler.getMouseButtonCallback());
        glfwSetScrollCallback(handle, MouseHandler.getScrollCallback());

        // Window now created, setup video mode
        vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(handle, (vidMode.width() - size.x) / 2, (vidMode.height() - size.y) / 2);

        // Start the OpenGL Context
        glfwMakeContextCurrent(handle);

        // Do we want vsync?
        if (Configuration.Core.enableVsync) glfwSwapInterval(1);

        // Create Gl Capabilities
        caps = GL.createCapabilities();

        // Create LWJGUI Window instance
        renderWindow = WindowManager.generateWindow(handle);

        setupScene();
    }

    private void setupScene() {

        StackPane pane = new StackPane();
        pane.getChildren().add(new Label("Hello World!"));
        renderWindow.setScene(new Scene(pane, size.x, size.y));
    }

    public void destroy() {

        WindowManager.dispose();

        // Terminate once finished
        glfwFreeCallbacks(handle);
        glfwDestroyWindow(handle);
        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }

    public void showWindow() {

        renderWindow.show();
//        glfwShowWindow(handle);
    }

    public void update() {

    }

    public void render() {

        // Start loop
        while (!glfwWindowShouldClose(handle)) {

            glfwMakeContextCurrent(handle);

            glfwPollEvents();


            WindowManager.update();

            renderWindow.render();
//            RenderUtils.clear();
//            RenderUtils.clearColour(Colour.CYAN);

            // TODO: replace with Variable Step Game Loop
            renderWindow.updateDisplay(Configuration.Core.targetFrameRate);
//            glfwSwapBuffers(handle);
        }
    }
}
