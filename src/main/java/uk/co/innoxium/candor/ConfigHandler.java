package uk.co.innoxium.candor;


import uk.co.innoxium.candor.util.Resources;
import uk.co.innoxium.cybernize.setting.SettingsHandler;


/**
 * Handles all our configs, We currently only have one.
 */
public class ConfigHandler {

    public static void handleCore() {

        // Sets the config directory to our config directory.
        SettingsHandler.CONFIG_DIR = Resources.CONFIG_PATH;
        // Add our settings holder to the handler.
        SettingsHandler.addHolder(Settings.class);
        try {

            // Load the settings!
            SettingsHandler.load();
        } catch(IllegalAccessException e) {

            e.printStackTrace();
        }
    }
}
