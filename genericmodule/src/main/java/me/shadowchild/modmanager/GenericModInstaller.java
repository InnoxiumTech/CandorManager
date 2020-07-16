package me.shadowchild.modmanager;

import me.shadowchild.modmanager.mod.Mod;
import me.shadowchild.modmanager.module.AbstractModInstaller;
import me.shadowchild.modmanager.module.AbstractModule;

public class GenericModInstaller extends AbstractModInstaller {

    public GenericModInstaller(AbstractModule module) {

        super(module);
    }

    @Override
    public boolean canInstall(Mod mod) {
        return false;
    }

    @Override
    public boolean install(Mod mod) {
        return false;
    }
}
