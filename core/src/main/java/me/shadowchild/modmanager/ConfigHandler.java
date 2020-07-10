package me.shadowchild.modmanager;

import com.electronwill.nightconfig.core.file.FileNotFoundAction;

import java.io.File;
import java.nio.file.Files;

public class ConfigHandler {

    private static File configFolder = new File("./config");

    public static void handleCore() {

        new CoreConfig().loadConfig(configFolder);
    }

    public interface IConfig {

        FileNotFoundAction CREATE_EMPTY = (file, cfgFormat) -> {

            Files.createDirectories(file.getParent());
            Files.createFile(file);
            cfgFormat.initEmptyFile(file);
            return false;
        };

        String getName();

        // handleModules the loading via the config as they may wish to some things differently
        void loadConfig(File configFolder);
    }
}
