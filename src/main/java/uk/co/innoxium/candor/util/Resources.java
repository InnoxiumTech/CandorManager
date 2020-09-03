package uk.co.innoxium.candor.util;

import net.harawata.appdirs.AppDirsFactory;

import javax.swing.*;
import java.io.File;

public class Resources {

    public static JComponent currentScene;

    public static final File CANDOR_DATA_PATH = new File(
            AppDirsFactory.getInstance().getUserDataDir(
                    "Innoxium/Candor",
                    null,
                    null,
                    true));

    public static final File STORE_PATH = new File(CANDOR_DATA_PATH, "ModStore");
    public static final File CONFIG_PATH = new File(CANDOR_DATA_PATH, "Config");
}
