package me.shadowchild.modmanager.module;

import me.shadowchild.modmanager.mod.Mod;

import java.io.File;
import java.nio.file.Paths;

public abstract class AbstractModInstaller {

    private IModule module;

    public AbstractModInstaller(IModule module) {

        this.module = module;
    }

    public abstract boolean canInstall(Mod mod);

    public abstract boolean install(Mod mod);
}
