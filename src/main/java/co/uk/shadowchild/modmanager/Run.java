package co.uk.shadowchild.modmanager;

import co.uk.shadowchild.modmanager.window.Window;
import org.joml.Vector2i;

public class Run {

    public static Window window;

    public static void main(String... args) {

        window = new Window(new Vector2i(1260, 900), "LWJGL WINDOW");
    }
}
