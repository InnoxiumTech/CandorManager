package uk.co.innoxium.candor.thread;

import uk.co.innoxium.candor.mod.Mod;
import uk.co.innoxium.candor.mod.store.ModStore;
import uk.co.innoxium.candor.module.AbstractModInstaller;
import uk.co.innoxium.candor.module.ModuleSelector;


/**
 * This thread controls how the mod is installed, it will send the mod to the module for installation or removal
 */
public class ThreadModInstaller extends Thread {

    // The mod to install
    private final Mod mod;
    // Are we installing or uninstalling?
    private final boolean installing;

    /**
     * Creates an Installing thread
     *
     * @param mod - The mod to install
     */
    public ThreadModInstaller(Mod mod) {

        this(mod, true);
    }

    /**
     * Creates a thread with the option to uninstall
     *
     * @param mod        - The mod to install/uninstall
     * @param installing - Are we installing or uninstalling?
     */
    public ThreadModInstaller(Mod mod, boolean installing) {

        // Set the fields
        this.mod = mod;
        this.installing = installing;
    }

    /**
     * Lets install or uninstall that mod!
     */
    @Override
    public void run() {

        // Get the mod installer from the current module
        AbstractModInstaller modInstaller = ModuleSelector.currentModule.getModInstaller();

        // Are we installing?
        if(installing) {

            // We are not yet installed
            boolean installed = false;

            // Check whether we can install this mod
            if(modInstaller.canInstall(mod)) {

                // Send the mod to the module installer to be installed
                installed = modInstaller.install(mod);
            }
            // If it was successfully installed, update the state of the mod and fire a change to all listeners
            if(installed) {

                mod.setState(Mod.State.ENABLED);
                ModStore.updateModState(mod, Mod.State.ENABLED);
                ModStore.MODS.fireChangeToListeners("install", mod, true);
            }
        } else {

            // If we are uninstalling, send the mod to the mod installer for removal, and fire change to listeners.
            boolean successful = modInstaller.uninstall(mod);
            ModStore.MODS.fireChangeToListeners("uninstall", mod, successful);
        }
    }
}
