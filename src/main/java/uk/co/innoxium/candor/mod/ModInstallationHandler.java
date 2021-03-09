package uk.co.innoxium.candor.mod;

import uk.co.innoxium.candor.mod.store.ModStore;
import uk.co.innoxium.candor.module.AbstractModInstaller;
import uk.co.innoxium.candor.module.ModuleSelector;
import uk.co.innoxium.candor.util.Logger;

import javax.swing.*;
import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;


public class ModInstallationHandler {

    final LinkedList<Mod> queuedMods;
    final JList<Mod> modsList;

    public ModInstallationHandler(LinkedList<Mod> queuedMods, JList<Mod> modsList) {

        this.queuedMods = queuedMods;
        this.modsList = modsList;
    }

    public CompletableFuture installMods() {

        // TODO: Wrap this in a future so await can be called on it

        // Get the mod installer for the current game, defined by the module
        AbstractModInstaller modInstaller = ModuleSelector.currentModule.getModInstaller();

        // While we have a mod in the list, iterate
        while(queuedMods.size() > 0) {

            // Pop the mod from the list
            Mod mod = queuedMods.pop();

            // Check if we can install this mod
            if(modInstaller.canInstall(mod)) {

                // This will be set by the result of the await call
                boolean installed = false;
                // We now try to install the mod, asynchronously.
                // TODO: migrate modInstallers to futures
//                com.ea.async.Async.await(modInstaller.install(mod));

                if(installed) {

                    mod.setState(Mod.State.ENABLED);
                    ModStore.updateModState(mod, Mod.State.ENABLED);
                    // TODO: make this better, update the jlist directly
                    ModStore.MODS.fireChangeToListeners("install", mod, true);
                }
            }
            // We cannot install this mod, report to use and skip
            Logger.info("Module reported that mod '" + mod.getName() + "' cannot be installed, skipping");
        }

        return null;
    }

    public CompletableFuture uninstallMods() {

        // TODO: wrap in a future so await can be called on it

        // Get the mod installer for the current game, defined by the module
        AbstractModInstaller modInstaller = ModuleSelector.currentModule.getModInstaller();

        while(queuedMods.size() > 0) {

            // TODO: make better and functional

            // Pop the mod from the list
            Mod mod = queuedMods.pop();

            // Attempt to uninstall the mod by using another await command
            modInstaller.uninstall(mod);

            // Send an update to the list
            ModStore.MODS.fireChangeToListeners("uninstall", mod, true);
        }
        return null;
    }
}
