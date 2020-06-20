package me.shadowchild.modmanager;

import com.electronwill.nightconfig.core.file.FileNotFoundAction;
import com.google.common.collect.Lists;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;

public class ConfigHandler {

    private static File configFolder = new File("./config");
    public static final ArrayList<IConfig> CONFIG_LIST = Lists.newArrayList();

    public static void handleModules() {

        // Load the configs
        CONFIG_LIST.forEach((cfg) -> {

            cfg.loadConfig(configFolder);
        });
    }

    public static void handleCore() {

        new CoreConfig().loadConfig(configFolder);
    }

    public interface IConfig {

        FileNotFoundAction CREATE_EMPTY = (f,c) -> {

            Files.createDirectories(f.getParent());
            Files.createFile(f);
            c.initEmptyFile(f);
            return false;
        };

        String getName();

        // handleModules the loading via the config as they may wish to some things differently
        void loadConfig(File configFolder);
    }
}
