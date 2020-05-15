package co.uk.shadowchild.modmanager.util.config;

import com.electronwill.nightconfig.core.conversion.ObjectConverter;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.google.common.collect.Maps;

import java.util.Map;

public class ConfigManager {

    private Map<String, CommentedFileConfig> configs = Maps.newHashMap();

    private ConfigManager() {

    }

    public static ConfigManager createConfigManager() {

        return new ConfigManager();
    }

    public void createConfig(String location, String key) {

        CommentedFileConfig cfg = CommentedFileConfig.builder(location).autosave().build();
        configs.put(key, cfg);
        cfg.load();
    }

    public void createConfig(String location, String key, Object object) {

        CommentedFileConfig cfg = CommentedFileConfig.builder(location).autosave().build();
        cfg.load();
        ObjectConverter converter = new ObjectConverter();
        Configuration.DefaultData defaultData = converter.toObject(cfg, Configuration.DefaultData::new);
        converter.toConfig(defaultData, cfg);
        configs.put(key, cfg);
    }

    // You may store this as an Object if you wish
    public CommentedFileConfig getConfigFromKey(String key) {

        return configs.get(key);
    }

    public <T> T get(String key, String setting, T defaultSetting) {

        CommentedFileConfig cfg = getConfigFromKey(key);
        T ret = cfg.get(setting);
        if(ret != null) {

            return ret;
        } else {

            return cfg.set(setting, defaultSetting);
        }
    }

    public <T> T getWithComment(String key, String setting, String comment, T defaultSetting) {

        CommentedFileConfig cfg = getConfigFromKey(key);
        cfg.setComment(setting, comment);
        return get(key, setting, defaultSetting);
    }

    public void replaceConfig(String key, CommentedFileConfig toReplace) {

        if(configs.containsKey(key)) {

            configs.remove(key);
            configs.put(key, toReplace);
        }
        configs.put(key, toReplace);
    }

    public void saveToFile(String key) {

        ObjectConverter converter = new ObjectConverter();
        Configuration.DefaultData data = new Configuration.DefaultData();
        converter.toConfig(data, getConfigFromKey(key));
        getConfigFromKey(key).save();
    }
}
