package me.shadowchild.candor;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class CoreConfig implements ConfigHandler.IConfig {

    public static boolean showIntro;
    public static String game;
    public static String modsFolder;

    static CommentedFileConfig cfg;

    @Override
    public String getName() {

        return "Core Config";
    }

    @Override
    public void loadConfig(File configFolder) {

        cfg = CommentedFileConfig
                .builder(new File(configFolder, "core.toml"))
                .autosave()
                .autoreload()
                .charset(StandardCharsets.UTF_8)
                .onFileNotFound(CREATE_EMPTY)
                .build();

        cfg.load();

        showIntro = cfg.get("showIntro");
        cfg.set("showIntro", showIntro);

        game = cfg.get("game");
        cfg.set("game", game);

        modsFolder = cfg.get("modsFolder");
        cfg.set("modsFolder", modsFolder);

//        cfg.save();
    }

    public static void changeValue(String valueName, Object value, Object newValue) {

        value = newValue;
        cfg.set(valueName, value);
    }

    public void close() {

        cfg.close();
//        cfg.set("showIntro", showIntro);
//        cfg.set("game", game);
//        cfg.set("modsFolder", modsFolder);
    }
}
