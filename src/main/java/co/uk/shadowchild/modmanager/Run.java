package co.uk.shadowchild.modmanager;

import co.uk.shadowchild.modmanager.util.config.ConfigManager;
import co.uk.shadowchild.modmanager.window.Window;
import co.uk.shadowchild.modmanager.window.dialog.Dialogs;
import lwjgui.LWJGUIDialog;
import org.joml.Vector2i;

import java.io.File;

public class Run {

    private static File game = null;
    private static File modsFolder = null;
    public static Window window;

    public static ConfigManager configManager;

    public static void main(String... args) {

        // Load Configs
        configManager = ConfigManager.createConfigManager();
        configManager.createConfig("./config/defaults.toml", "defaults");

        // Set the game we will be modding
        // Would like to have more control over the dialog
        game = Dialogs.showSingleFileDialog("exe");
        modsFolder = Dialogs.openPickFolder();

        if(game != null && modsFolder != null) {

            // Handle game identification before loading the gui

            // We can now load the gui!
            window = new Window(new Vector2i(1600, 900), "Universal Mod Manager by ShadowChild");
        } else {

            LWJGUIDialog.showMessageDialog("Universal Mod Manager", "Game Selection Error.\nPlease run the application again.", LWJGUIDialog.DialogIcon.ERROR);
        }
        configManager.getConfigFromKey("defaults").close();
    }

    public static File getGame() {

        return game;
    }

    public static File getModsFolder() {

        return modsFolder;
    }
}
