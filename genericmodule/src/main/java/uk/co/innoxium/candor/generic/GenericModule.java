package uk.co.innoxium.candor.generic;

import uk.co.innoxium.candor.Settings;
import uk.co.innoxium.candor.module.AbstractModInstaller;
import uk.co.innoxium.candor.module.AbstractModule;
import uk.co.innoxium.candor.module.RunConfig;

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

        return "generic";
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

    @Override
    public boolean getEnableExtractOption() {

        return true;
    }

    @Override
    public RunConfig getDefaultRunConfig() {

        return new DefaultRunConfig();
    }

    public class DefaultRunConfig extends RunConfig {

        @Override
        public String getStartCommand() {

            return Settings.gameExe;
        }

        @Override
        public String getProgramArgs() {

            return "";
        }

        @Override
        public String getWorkingDir() {

            return null;
        }
    }
}
