package me.shadowchild.candor.thread;

import me.shadowchild.candor.mod.Mod;
import me.shadowchild.candor.mod.ModsHandler;
import me.shadowchild.candor.module.AbstractModInstaller;
import me.shadowchild.candor.module.ModuleSelector;

public class ThreadModInstaller extends Thread {

    private Mod mod;

    public ThreadModInstaller(Mod mod) {

        this.mod = mod;
    }

    @Override
    public void run() {

        boolean installed = false;

        AbstractModInstaller module = ModuleSelector.currentModule.getModInstaller();
        if(module.canInstall(mod)) {

            installed = module.install(mod);
        }
        if(installed) {

            mod.setState(Mod.State.ENABLED);
            ModsHandler.MODS.fireChangeToListeners("install", mod, true);
        }
    }
}
