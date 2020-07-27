package me.shadowchild.candor.generic;

import me.shadowchild.candor.ConfigHandler;
import me.shadowchild.candor.module.AbstractModInstaller;
import me.shadowchild.candor.module.AbstractModule;

import java.io.File;

/**
 * this module will be able to handleModules a majority of games that don't have their own module
 *
 * Also serves as an example of how a module should be made
 */
public class GenericModule extends AbstractModule {

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

        game = file;
    }

    @Override
    public void setModsFolder(File file) {

        modsFolder = file;
    }

    @Override
    public String getModuleName() {

        return "Generic Module";
    }

    @Override
    public String getModFileFilterList() {

        return "zip,7zip,7z,rar";
    }

    @Override
    public String getReadableGameName() {

        // Return the executable without the file extension
        return game.getName().substring(0, game.getName().lastIndexOf("."));
    }

    @Override
    public ConfigHandler.IConfig getConfig() {

        return new GenericConfig();
    }

    @Override
    public AbstractModInstaller getModInstaller() {

        return new GenericModInstaller(this);
    }

    @Override
    public boolean requiresModFolderSelection() {

        return true;
    }

    @Override
    public String[] acceptedExe() {

        // We dont need this for the generic module
        return new String[0];
    }
}
