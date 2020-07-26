package me.shadowchild.candor.module;

import me.shadowchild.candor.ConfigHandler;

import java.io.File;

public abstract class AbstractModule {

    public AbstractModule() {

        ModuleSelector.MODULES.add(this);
    }

    public abstract File getGame();

    public abstract File getModsFolder();

    public abstract void setGame(File file);

    public abstract void setModsFolder(File file);

    public abstract String getModuleName();

    public String getExeName() {

        return getGame().getName().substring(0, getGame().getName().lastIndexOf("."));
    }

    public abstract String getReadableGameName();

    public abstract ConfigHandler.IConfig getConfig();

    public abstract AbstractModInstaller getModInstaller();

    public abstract boolean requiresModFolderSelection();

    public abstract String[] acceptedExe();

    public abstract String getModFileFilterList();
}
