package uk.co.innoxium.candor.module;

import java.io.File;

public abstract class AbstractModule {

    public AbstractModule() {

    }

    public abstract File getGame();

    public abstract File getModsFolder();

    public abstract void setGame(File file);

    public abstract void setModsFolder(File file);

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

    public boolean isCritical(String filePath) {

        return false;
    }
}
