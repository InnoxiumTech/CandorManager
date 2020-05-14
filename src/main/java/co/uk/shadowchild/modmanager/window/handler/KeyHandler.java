package co.uk.shadowchild.modmanager.window.handler;

import org.lwjgl.glfw.GLFWKeyCallback;

import static org.lwjgl.glfw.GLFW.*;

public class KeyHandler {

    public static GLFWKeyCallback getKeyHandler() {

        return GLFWKeyCallback.create((window, key, scancode, action, mods) -> {

            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE ) {

                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
            }
        });
    }
}
