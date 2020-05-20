package co.uk.shadowchild.modmanager;

import co.uk.shadowchild.modmanager.util.config.ConfigManager;
import co.uk.shadowchild.modmanager.util.config.Configuration;
import co.uk.shadowchild.modmanager.window.MMWindow;
import co.uk.shadowchild.modmanager.window.dialog.Dialogs;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import org.joml.Vector2i;

import java.io.File;

public class Run {

    private static File game = null;
    private static File modsFolder = null;
    public static MMWindow window;

    public static ConfigManager configManager;
    public static CommentedFileConfig config;

    public static void main(String... args) {

        // Add a shutdown handler for in case there is a crash, should hopefully stop configs getting erased
        Runtime.getRuntime().addShutdownHook(new Thread(Run::handleClosing));

        // Load Configs
        handleConfigs();

        if(game != null && modsFolder != null) {

            // Handle game identification before loading the gui

            start();
        } else {

            Dialogs.showInfoDialog(
                    "Universal Mod Manager",
                    "Game Selection Error.\nPlease run the application again.",
                    "ok",
                    "error",
                    true);
        }
    }

    public static File getGame() {

        return game;
    }

    public static File getModsFolder() {

        return modsFolder;
    }

    private static void handleConfigs() {

        // Very long winded way of serializing from a class to a file

        configManager = ConfigManager.createConfigManager();
        configManager.createConfig("./config/defaults.toml", "defaults", new Configuration.Core());
        config = configManager.getConfigFromKey("defaults");

        // Set the game we will be modding
        // Would like to have more control over the dialog
        if(Configuration.Core.game.equals("")) {

            game = Dialogs.showSingleFileDialog("exe");
            Configuration.Core.game = game.getAbsolutePath();
        }
        else game = new File(Configuration.Core.game);

        if(Configuration.Core.modsFolder.equals("")) {

            modsFolder = Dialogs.openPickFolder();
            Configuration.Core.modsFolder = modsFolder.getAbsolutePath();
        }
        else modsFolder = new File(Configuration.Core.modsFolder);

        handleClosing();
    }

    public static void handleClosing() {

        // we need to close the config when we're done

        try {

            configManager.saveToFile("defaults");
            configManager.getConfigFromKey("defaults").close();
        } catch(IllegalStateException ignored) {}
        // We can ignore the exception, it's just to sanity check
    }

    public static void start() {

        // We can now load the gui!
        window = new MMWindow(new Vector2i(Configuration.Core.windowWidth, Configuration.Core.windowHeight), "Universal Mod Manager by ShadowChild");
        // Initilize our window
        window.init();
        // Show our window
        window.showWindow();
        // Load our resources now
        window.loadResources();
        // Run our update thread
        window.update();
        // Run our render thread
        window.render();

        // Kill our window
        window.destroy();
    }
}
