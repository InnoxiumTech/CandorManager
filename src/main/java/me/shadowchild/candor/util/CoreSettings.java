package me.shadowchild.candor.util;

import me.shadowchild.candor.settings.Settings;

public class CoreSettings {


    // Display Settings
    public static int width = Settings.addSetting("core", "width", "width of display", 1600);
    public static int height = Settings.addSetting("core", "height", "height of display", 900);

    // Functional settings
    public static boolean showIntro = Settings.addSetting("core", "showIntro", "Should the intro screen be shown?", true);
    public static String game = Settings.addSetting("core", "game", "The filepath to the game", "");
    public static String mods = Settings.addSetting("core", "mods", "The filepath to the mods folder", "");
    public static boolean modExtract = Settings.addSetting("core", "modExtract", "Should the mod be extracted for this module?", false);

    public static void init() {}
}
