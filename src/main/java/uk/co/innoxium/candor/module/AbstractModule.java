package uk.co.innoxium.candor.module;

import uk.co.innoxium.candor.implement.FileMerger;
import uk.co.innoxium.cybernize.archive.ArchiveItem;

import java.io.File;

public abstract class AbstractModule {

    protected File game;
    protected File modsFolder;

    public AbstractModule() {

    }

    public File getGame() {

        return this.game;
    }

    public File getModsFolder() {

        return this.modsFolder;
    }

    public void setGame(File file) {

        this.game = file;
    }

    public void setModsFolder(File file) {

        this.modsFolder = file;
    }

    public abstract String getModuleName();

    public String getExeName() {

        File game = getGame();

        String exeName = game.getName().substring(0, game.getName().lastIndexOf("."));

        return exeName;
    }

    public abstract String getReadableGameName();

    public abstract AbstractModInstaller getModInstaller();

    public abstract boolean requiresModFolderSelection();

    public abstract String[] acceptedExe();

    public abstract String getModFileFilterList();

    public boolean getEnableExtractOption() {

        return false;
    }

    public abstract RunConfig getDefaultRunConfig();

    public boolean isCritical(ArchiveItem archiveItem) {

        return false;
    }

    public FileMerger getFileMerger() {

        return null;
    }
}
