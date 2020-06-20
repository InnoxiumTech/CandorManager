package me.shadowchild.modmanager.module;

import java.io.File;

public interface IModule {

    public File getGame();

    public File getModsFolder();

    public String getModuleName();

    public String getReadableGameName();
}
