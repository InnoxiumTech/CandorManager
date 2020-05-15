package co.uk.shadowchild.modmanager.util.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.file.FileConfig;
import com.google.common.collect.Maps;

import java.util.Map;

public class ConfigManager {

    private Map<String, FileConfig> configs = Maps.newHashMap();

    private ConfigManager() {

    }

    public static ConfigManager createConfigManager() {

        return new ConfigManager();
    }

    public void createConfig(String location, String key) {

        FileConfig cfg = CommentedFileConfig.builder(location).build();
        configs.put(key, cfg);
        cfg.load();
    }

    public FileConfig getConfigFromKey(String key) {

        return configs.get(key);
    }
}
