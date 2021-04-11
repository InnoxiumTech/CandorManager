package uk.co.innoxium.candor.mod.handler;

import uk.co.innoxium.candor.mod.Mod;
import uk.co.innoxium.candor.mod.store.ModStore;
import uk.co.innoxium.candor.module.AbstractModInstaller;
import uk.co.innoxium.candor.module.ModuleSelector;
import uk.co.innoxium.candor.util.Logger;
import uk.co.innoxium.candor.window.dialog.SwingDialogs;

import javax.swing.*;
import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;


public class ModInstallationHandler {

    final LinkedList<Mod> queuedMods;

    public ModInstallationHandler(LinkedList<Mod> queuedMods) {

        this.queuedMods = queuedMods;
    }

    public CompletableFuture<Boolean> installMods() {

        // TODO: Wrap this in a future so await can be called on it

        // Get the mod installer for the current game, defined by the module
        AbstractModInstaller modInstaller = ModuleSelector.currentModule.getModInstaller();

        // While we have a mod in the list, iterate
        while(queuedMods.size() > 0) {

            // Pop the mod from the list
            Mod mod = queuedMods.pop();

            // Check if we can install this mod
            if(modInstaller.canInstall(mod)) {

                // We now try to install the mod, asynchronously.
                // TODO: migrate modInstallers to futures
                Logger.info("Attempting to install mod " + mod.getReadableName());
                Boolean installed = modInstaller.install(mod).join();

                Logger.info("Installation returned " + installed);

                if(installed) {

                    mod.setState(Mod.State.ENABLED);
                    ModStore.updateModState(mod, Mod.State.ENABLED);
                    // TODO: make this better, update the jlist directly
                    ModStore.MODS.fireChangeToListeners("install", mod, true);
                } else {

                    Logger.info("Mod " + mod.getReadableName() + " was not installed.");
                    SwingDialogs.showInfoMessage("Install Warning", "The Mod " + mod.getReadableName() + " was not installed.", JOptionPane.WARNING_MESSAGE);
                }
            } else {

                // We cannot install this mod, report to use and skip
                Logger.info("Module reported that mod '" + mod.getName() + "' cannot be installed, skipping");
            }
        }

        return CompletableFuture.completedFuture(true);
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
