package me.shadowchild.modmanager;

import me.shadowchild.modmanager.module.IModule;

import java.io.File;

/**
 * this module will be able to handleModules a majority of games that don't have their own module
 *
 * Also serves as an example of how a module should be made
 */
public class GenericModule implements IModule {

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
    public String getModuleName() {

        return "Generic Module";
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
}
