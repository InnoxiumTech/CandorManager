package uk.co.innoxium.candor;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import uk.co.innoxium.candor.util.Resources;
import uk.co.innoxium.cybernize.setting.Config;
import uk.co.innoxium.cybernize.setting.Setting;
import uk.co.innoxium.cybernize.setting.SettingsHolder;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@SettingsHolder(id = "core", ext = "toml")
public class Settings {

    @Setting(category = "game")
    @Setting.Comment("Should the mod file be extracted? May be overwritten by a module")
    public static boolean modExtract = false;

    @Setting(category = "game")
    @Setting.Comment("The UUID of the default game to load")
    public static String defaultGameUuid = "";

    @Setting(category = "game")
    @Setting.Comment("The UUID of the last game loaded")
    public static String lastGameUuid = "";

    @Config
    public static CommentedFileConfig getConfig() {

        return CommentedFileConfig
                .builder(new File(Resources.CONFIG_PATH, "core.toml"))
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
