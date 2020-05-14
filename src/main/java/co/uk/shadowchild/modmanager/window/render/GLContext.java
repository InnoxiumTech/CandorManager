package co.uk.shadowchild.modmanager.window.render;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;

public class GLContext implements IRenderContext {

    private static GLCapabilities caps;

    @Override
    public void createCapabilities() {

        caps = GL.createCapabilities();
    }

    public static GLCapabilities getCaps() {

        return caps;
    }
}
