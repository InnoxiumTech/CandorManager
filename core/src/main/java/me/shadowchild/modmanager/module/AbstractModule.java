package me.shadowchild.modmanager.module;

import me.shadowchild.modmanager.ConfigHandler;

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

    public abstract String getReadableGameName();

    public abstract ConfigHandler.IConfig getConfig();

    public abstract AbstractModInstaller getModInstaller();

    public abstract boolean requiresModFolderSelection();

    public abstract String[] acceptedExe();
}
