package uk.co.innoxium.candor.module;

import uk.co.innoxium.candor.implement.FileMerger;
import uk.co.innoxium.candor.mod.Mod;

import java.util.concurrent.CompletableFuture;


public abstract class AbstractModInstaller {

    protected AbstractModule module;

    public AbstractModInstaller(AbstractModule module) {

        this.module = module;
    }

    public abstract boolean canInstall(Mod mod);

    public abstract CompletableFuture<Boolean> install(Mod mod);

    public abstract boolean uninstall(Mod mod);

    // Asks the module if it has the ability to merge mod files, similar to w3 script merger
    public boolean canMergeFiles() {

        return false;
    }

    /**
     * @param mod - The mod to get the merger for
     * @return - An instance of a FileMerger to handle merging of files
     */
    public FileMerger getMergerForMod(Mod mod, String fileExtension) {

        return null;
    }
}
