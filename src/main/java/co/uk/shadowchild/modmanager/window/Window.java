package co.uk.shadowchild.modmanager.window;

import co.uk.shadowchild.modmanager.Resources;
import co.uk.shadowchild.modmanager.window.handler.KeyHandler;
import co.uk.shadowchild.modmanager.window.handler.MouseHandler;
import co.uk.shadowchild.modmanager.window.render.GLContext;
import co.uk.shadowchild.modmanager.window.render.IRenderContext;
import co.uk.shadowchild.modmanager.window.render.Texture;
import lwjgui.scene.Scene;
import lwjgui.scene.WindowManager;
import lwjgui.scene.control.Label;
import lwjgui.scene.layout.StackPane;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    // Is GLFW Initlized?
    public boolean glfwInitialized;
    // The Window Handle for GLFW
    private long handle;
    // Our GLFW VideoMode
    private GLFWVidMode vidMode;

    // Needed for future renderer support
    private IRenderContext context;

    lwjgui.scene.Window window;

    public Window(Vector2i size, String title) {

        this(size, title, NULL, NULL);
    }

    public Window(Vector2i size, String title, long monitor, long share) {

        // Set up an error callback, will send any native errors to java logger
        GLFWErrorCallback.createPrint(System.err).set();

        decideContext();

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
        handle = glfwCreateWindow(size.x, size.y, title, monitor, share);
        if(handle == NULL) {

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
        glfwSwapInterval(1);
        context.createCapabilities();

        window = WindowManager.generateWindow(handle);
        StackPane pane = new StackPane();
        pane.getChildren().add(new Label("Hello World"));
        window.setScene(new Scene(pane, size.x, size.y));
        window.show();

        // Load our resources now
        loadResources();

        // Start loop
        while(!glfwWindowShouldClose(handle)) {

            glfwMakeContextCurrent(handle);

            WindowManager.update();

            window.render();

            glfwSwapBuffers(handle);
        }

        // Terminate once finished
        WindowManager.dispose();
        glfwFreeCallbacks(handle);
        glfwDestroyWindow(handle);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void decideContext() {

        // I plan on adding Vulkan support, for now it will just be OpenGL
        context = new GLContext();
    }

    private void loadResources() {

        Resources.test = new Texture("./test.png");
    }
}
