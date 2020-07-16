package me.shadowchild.candor.mod;

import java.io.File;

public abstract class Mod {

    private File file;
    private String modName;

    public Mod(File file, String modName) {

        this.file = file;
        this.modName = modName;
    }

    public File getFile() {

        return file;
    }

    public String getModName() {

        return modName;
    }
}
