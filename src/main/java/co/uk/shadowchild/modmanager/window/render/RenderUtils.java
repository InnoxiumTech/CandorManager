package co.uk.shadowchild.modmanager.window.render;

import static org.lwjgl.opengl.GL11.*;

public class RenderUtils {

    // Runs the GL11.glClear
    public static void clear() {

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    // Runs the Gl11.glClearColor
    public static void clearColour(Colour colour) {

        glClearColor(
                colour.r,
                colour.g,
                colour.b,
                colour.a
        );
    }
}
