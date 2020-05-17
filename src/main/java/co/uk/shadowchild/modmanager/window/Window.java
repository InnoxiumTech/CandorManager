package co.uk.shadowchild.modmanager.window;

import co.uk.shadowchild.modmanager.Resources;
import co.uk.shadowchild.modmanager.util.config.Configuration;
import co.uk.shadowchild.modmanager.window.handler.KeyHandler;
import co.uk.shadowchild.modmanager.window.handler.MouseHandler;
import co.uk.shadowchild.modmanager.window.render.Texture;
import lwjgui.geometry.Orientation;
import lwjgui.geometry.Pos;
import lwjgui.scene.Scene;
import lwjgui.scene.WindowManager;
import lwjgui.scene.control.Label;
import lwjgui.scene.control.ScrollPane;
import lwjgui.scene.control.SplitPane;
import lwjgui.scene.layout.StackPane;
import lwjgui.scene.layout.VBox;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;

import java.util.Objects;

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
    // Our GLCapabilities
    private GLCapabilities caps;

    lwjgui.scene.Window window;

    public Window(Vector2i size, String title) {

        this(size, title, NULL, NULL);
    }

    public Window(Vector2i size, String title, long monitor, long share) {

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

        // Do we want vsync?
        if(Configuration.DefaultData.enableVsync) glfwSwapInterval(1);

        // Create Gl Capabilities
        caps = GL.createCapabilities();

        // Generate a lwjgui.scene.Window from our glfw handle
        window = WindowManager.generateWindow(handle);

        // Initialize all components of our pane
        setupPane(window);

        // Now the window is set up, we can show it
        window.show();

        // Load our resources now
        loadResources();

        // Start loop
        while(!glfwWindowShouldClose(handle)) {

            glfwMakeContextCurrent(handle);

            WindowManager.update();

            window.render();

            // This couples rendering and logic, need to decouple this at some point
            window.updateDisplay(Configuration.DefaultData.targetFrameRate);
        }

        // Terminate once finished
        WindowManager.dispose();
        glfwFreeCallbacks(handle);
        glfwDestroyWindow(handle);
        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }

    private void setupPane(lwjgui.scene.Window window) {

        // Create background pane
        StackPane pane = new StackPane();

        // Create vertical layout
        VBox box = new VBox();
        box.setAlignment(Pos.CENTER);
        box.setFillToParentHeight(true);
        box.setFillToParentWidth(true);
        pane.getChildren().add(box);

        // Title label
        Label title = new Label("Universal Mod Manager");
        title.setFontSize(32);
        title.setAlignment(Pos.TOP_CENTER);
        box.getChildren().add(title);

        // Create split pane
        SplitPane split = new SplitPane();
        split.setFillToParentHeight(true);
        split.setFillToParentWidth(true);
        split.setOrientation(Orientation.VERTICAL);
        box.getChildren().add(split);

        // Add some content

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFillToParentHeight(true);
        scrollPane.setFillToParentWidth(true);
        scrollPane.setAlignment(Pos.CENTER_LEFT);
        split.getItems().add(scrollPane);

        ScrollPane scrollPane2 = new ScrollPane();
        scrollPane2.setFillToParentHeight(true);
        scrollPane2.setFillToParentWidth(true);
        scrollPane2.setAlignment(Pos.CENTER_RIGHT);
        split.getItems().add(scrollPane2);

        // After it's all setup, make sizes not rely on window size.
//        LWJGUI.runLater(()->{
//            SplitPane.setResizableWithParent(split.getItems().get(0), false);
//            SplitPane.setResizableWithParent(split.getItems().get(1), false);
//        });

        // Set the scene
        window.setScene(new Scene(pane, 1600, 900));
    }

    private void loadResources() {

        Resources.test = new Texture("./test.png");
    }
}
