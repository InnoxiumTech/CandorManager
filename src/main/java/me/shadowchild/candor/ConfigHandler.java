package me.shadowchild.candor;

import me.shadowchild.cybernize.setting.SettingsHandler;

import java.io.File;

public class ConfigHandler {

    private static File configFolder = new File("./config");

    public static void handleCore() {

        SettingsHandler.addHolder(Settings.class);
        try {

            SettingsHandler.load();
        } catch (IllegalAccessException e) {

            e.printStackTrace();
        }
//        CoreSettings.init();
//        coreConfig.loadConfig(configFolder);
    }
}
