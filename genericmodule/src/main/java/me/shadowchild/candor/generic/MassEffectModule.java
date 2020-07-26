package me.shadowchild.candor.generic;

import me.shadowchild.candor.ConfigHandler;
import me.shadowchild.candor.module.AbstractModInstaller;
import me.shadowchild.candor.module.AbstractModule;

import java.io.File;

public class MassEffectModule extends AbstractModule {

    public File game, modsFolder;

    @Override
    public File getGame() {

        return game;
    }

    @Override
    public File getModsFolder() {

        return modsFolder;
    }

    @Override
    public void setGame(File file) {

        this.game = file;
    }

    @Override
    public void setModsFolder(File file) {

        this.modsFolder = new File(game.getParentFile(), "../..");
    }

    @Override
    public String getModuleName() {

        return "Mess Effect Module";
    }

    @Override
    public String getReadableGameName() {

        return "Mass Effect";
    }

    @Override
    public ConfigHandler.IConfig getConfig() {

        return null;
    }

    @Override
    public AbstractModInstaller getModInstaller() {

        return null;
    }

    @Override
    public boolean requiresModFolderSelection() {

        return false;
    }

    @Override
    public String[] acceptedExe() {

        return new String[] { "MassEffect", "MassEffect2", "MassEffect3" };
    }

    @Override
    public String getModFileFilterList() {
        return null;
    }
}
