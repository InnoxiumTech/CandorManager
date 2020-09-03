package uk.co.innoxium.candor.thread;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import uk.co.innoxium.candor.mod.Mod;
import uk.co.innoxium.candor.mod.store.ModStore;
import uk.co.innoxium.candor.module.AbstractModInstaller;
import uk.co.innoxium.candor.module.ModuleSelector;
import uk.co.innoxium.cybernize.json.JsonUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ThreadModInstaller extends Thread {

    private final Mod mod;
    private final boolean installing;

    public ThreadModInstaller(Mod mod) {

        this(mod, true);
    }

    public ThreadModInstaller(Mod mod, boolean installing) {

        this.mod = mod;
        this.installing = installing;
    }

    @Override
    public void run() {

        AbstractModInstaller modInstaller = ModuleSelector.currentModule.getModInstaller();

        if(installing) {

            boolean installed = false;

            if(modInstaller.canInstall(mod)) {

                installed = modInstaller.install(mod);
            }
            if(installed) {

                mod.setState(Mod.State.ENABLED);
                ModStore.updateModState(mod, Mod.State.ENABLED);
                ModStore.MODS.fireChangeToListeners("install", mod, true);
            }
        } else {

            boolean successful = modInstaller.uninstall(mod);
            ModStore.MODS.fireChangeToListeners("uninstall", mod, successful);
        }
    }
}
