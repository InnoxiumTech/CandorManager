package co.uk.shadowchild.modmanager;

import co.uk.shadowchild.modmanager.window.Window;
import lwjgui.LWJGUIDialog;
import org.joml.Vector2i;

import java.io.File;

public class Run {

    private static File game = null;
    public static Window window;

    public static void main(String... args) {

        // Set the game we will be modding
        game = LWJGUIDialog.showOpenFileDialog("Universal Mod Manager", new File("./"), "Executables", "exe");

        if(game != null) {

            // Handle game indentification before loading the gui

            // We can now load the gui!
            window = new Window(new Vector2i(1600, 900), "Universal Mod Manager by ShadowChild");
        } else {

            LWJGUIDialog.showMessageDialog("Universal Mod Manager", "Game Selection Error.\nPlease run the application again.", LWJGUIDialog.DialogIcon.ERROR);
        }
    }

    public static File getGame() {

        return game;
    }
}
