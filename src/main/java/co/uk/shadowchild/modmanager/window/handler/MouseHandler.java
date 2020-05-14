package co.uk.shadowchild.modmanager.window.handler;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.*;

public class MouseHandler {

    // Break the handler in to a seperate file for cleaner code
    public static GLFWMouseButtonCallback getMouseButtonCallback() {

        return GLFWMouseButtonCallback.create(((window, button, action, mods) -> {

            if(glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_LEFT) == GLFW_TRUE) {

                DoubleBuffer b1 = BufferUtils.createDoubleBuffer(1);
                DoubleBuffer b2 = BufferUtils.createDoubleBuffer(1);
                glfwGetCursorPos(window, b1, b2);
                System.out.println("x: " + b1.get(0) + ", y: " + b2.get(0));
            }
        }));
    }

    public static GLFWCursorPosCallback getCursorPosCallback() {

        return GLFWCursorPosCallback.create(((window, xpos, ypos) -> {

        }));
    }

    public static GLFWScrollCallback getScrollCallback() {

        return GLFWScrollCallback.create((window, xoffset, yoffset) -> {

        });
    }
}
