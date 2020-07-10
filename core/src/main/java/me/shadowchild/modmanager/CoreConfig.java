package me.shadowchild.modmanager;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;

import java.io.File;
import java.nio.charset.Charset;

public class CoreConfig implements ConfigHandler.IConfig {

    public static boolean showIntro;

    @Override
    public String getName() {

        return "Core Config";
    }

    @Override
    public void loadConfig(File configFolder) {

        CommentedFileConfig cfg = CommentedFileConfig
                .builder(new File(configFolder, "core.toml"))
                .autosave()
                .autoreload()
                .charset(Charset.forName("UTF-8"))
                .onFileNotFound(CREATE_EMPTY)
                .build();
        cfg.load();

        showIntro = cfg.getOrElse("showIntro", true);
        cfg.set("showIntro", showIntro);

        cfg.save();
    }
}
