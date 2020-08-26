package uk.co.innoxium.candor;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import me.shadowchild.cybernize.setting.Config;
import me.shadowchild.cybernize.setting.Setting;
import me.shadowchild.cybernize.setting.SettingsHolder;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@SettingsHolder(id = "core", ext = "toml")
public class Settings {

    @Setting(category = "window")
    @Setting.Comment("Should we show the intro screen?")
    public static boolean showIntro = true;

    @Setting(category = "game")
    @Setting.Comment("The path to the game .exe")
    public static String gameExe = "";

    @Setting(category = "game")
    @Setting.Comment("The path to the game's mods folder")
    public static String modsFolder = "";

    @Setting(category = "game")
    @Setting.Comment("Should the mod file be extracted? May be overwritten by a module")
    public static boolean modExtract = false;

    @Config
    public static CommentedFileConfig getConfig() {

        return CommentedFileConfig
                .builder(new File("./config", "core.toml"))
                .autosave()
                .autoreload()
                .charset(StandardCharsets.UTF_8)
                .onFileNotFound((file, cfgFormat) -> {

                    Files.createDirectories(file.getParent());
                    Files.createFile(file);
                    cfgFormat.initEmptyFile(file);
                    return false;
                })
                .build();
    }
}
