package uk.co.innoxium.candor.module;

import uk.co.innoxium.candor.mod.Mod;

public abstract class AbstractModInstaller {

    protected AbstractModule module;

    public AbstractModInstaller(AbstractModule module) {

        this.module = module;
    }

    public abstract boolean canInstall(Mod mod);

    public abstract boolean install(Mod mod);

    public abstract boolean uninstall(Mod mod);
}
