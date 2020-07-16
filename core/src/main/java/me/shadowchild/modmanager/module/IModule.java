package me.shadowchild.modmanager.module;

import me.shadowchild.modmanager.ConfigHandler;

import java.io.File;

public interface IModule {

    public File getGame();

    public File getModsFolder();

    public String getModuleName();

    public String getReadableGameName();

    public ConfigHandler.IConfig getConfig();

    public AbstractModInstaller getModInstaller();

    public boolean requiresModFolderSelection();

    public String[] acceptedExe();
}
