package me.shadowchild.candor.settings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Settings {

    // Map<Category, Map<Name, List of Settings>>
    private static final Map<String, ArrayList<Setting>> SETTINGS_MAP = new HashMap<>();

    public static Map<String, ArrayList<Setting>> getSettingsMap() {

        return SETTINGS_MAP;
    }

    public static ArrayList<Setting> getSettingsFor(String category) {

        if(!getSettingsMap().containsKey(category))
            getSettingsMap().put(category, new ArrayList<>());


        return getSettingsMap().get(category);
    }

    public static <T> T addSetting(String category, String name, String comment, T defaultValue) {

        Setting<T> setting = new Setting<>(category, name, comment, defaultValue);
        getSettingsFor(category).add(setting);
        return setting.get();
    }
}
