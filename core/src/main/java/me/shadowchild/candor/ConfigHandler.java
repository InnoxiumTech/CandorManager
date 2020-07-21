package me.shadowchild.candor;

import com.electronwill.nightconfig.core.file.FileConfig;
import com.electronwill.nightconfig.core.file.FileNotFoundAction;

import java.io.File;
import java.nio.file.Files;

public class ConfigHandler {

    private static File configFolder = new File("./config");
    private static final IConfig coreConfig = new CoreConfig();

    public static void handleCore() {

        coreConfig.loadConfig(configFolder);
    }

    public interface IConfig {

        FileConfig config = buildConfig();

        static FileConfig buildConfig() {
            return null;
        }

        FileNotFoundAction CREATE_EMPTY = (file, cfgFormat) -> {

            Files.createDirectories(file.getParent());
            Files.createFile(file);
            cfgFormat.initEmptyFile(file);
            return false;
        };

        String getName();

        // handle the loading via the config as they may wish to some things differently
        void loadConfig(File configFolder);
    }

    public static IConfig getCoreConfig() {

        return coreConfig;
    }
}
