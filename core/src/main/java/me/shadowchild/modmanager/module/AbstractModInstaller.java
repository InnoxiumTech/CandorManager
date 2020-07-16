package me.shadowchild.modmanager.module;

import me.shadowchild.modmanager.mod.Mod;

public abstract class AbstractModInstaller {

    private AbstractModule module;

    public AbstractModInstaller(AbstractModule module) {

        this.module = module;
    }

    public abstract boolean canInstall(Mod mod);

    public abstract boolean install(Mod mod);
}
