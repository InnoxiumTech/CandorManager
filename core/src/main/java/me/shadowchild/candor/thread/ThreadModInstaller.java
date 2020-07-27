package me.shadowchild.candor.thread;

import me.shadowchild.candor.mod.Mod;
import me.shadowchild.candor.module.AbstractModInstaller;
import me.shadowchild.candor.module.ModuleSelector;

public class ThreadModInstaller extends Thread {

    private Mod mod;

    public ThreadModInstaller(Mod mod) {

        this.mod = mod;
    }

    @Override
    public void run() {

        AbstractModInstaller module = ModuleSelector.currentModule.getModInstaller();
        if(module.canInstall(mod)) {

            module.install(mod);
        }
    }
}
