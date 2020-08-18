package me.shadowchild.candor.module;

import me.shadowchild.candor.mod.Mod;

public abstract class AbstractModInstaller {

    protected AbstractModule module;

    public AbstractModInstaller(AbstractModule module) {

        this.module = module;
    }

    public abstract boolean canInstall(Mod mod);

    public abstract boolean install(Mod mod);
}
