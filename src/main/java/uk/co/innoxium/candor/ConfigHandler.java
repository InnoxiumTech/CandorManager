package uk.co.innoxium.candor;


import uk.co.innoxium.candor.util.Resources;
import uk.co.innoxium.cybernize.setting.SettingsHandler;

public class ConfigHandler {

    public static void handleCore() {

        SettingsHandler.CONFIG_DIR = Resources.CONFIG_PATH;
        SettingsHandler.addHolder(Settings.class);
        try {

            SettingsHandler.load();
        } catch (IllegalAccessException e) {

            e.printStackTrace();
        }
    }
}
