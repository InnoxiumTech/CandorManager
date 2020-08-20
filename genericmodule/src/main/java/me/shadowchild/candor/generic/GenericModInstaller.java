package me.shadowchild.candor.generic;

import me.shadowchild.candor.mod.Mod;
import me.shadowchild.candor.module.AbstractModInstaller;
import me.shadowchild.candor.module.AbstractModule;
import me.shadowchild.candor.Settings;
import me.shadowchild.cybernize.zip.ZipUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class GenericModInstaller extends AbstractModInstaller {

    public GenericModInstaller(AbstractModule module) {

        super(module);
    }

    @Override
    public boolean canInstall(Mod mod) {
        return true;
    }

    // Returns whether the mod was installed or not
    @Override
    public boolean install(Mod mod) {

        File modDir = this.module.getModsFolder();
        if(!modDir.exists()) modDir.mkdirs();

        if (Settings.modExtract) {

            try {

                ZipUtils.unZipIt(mod.getFile().getCanonicalPath(), modDir.getCanonicalPath());
                return true;
            } catch (IOException exception) {

                exception.printStackTrace();
                return false;
            }
        } else {

            try {

                if (!FileUtils.directoryContains(modDir, mod.getFile())) {

                    FileUtils.copyFileToDirectory(mod.getFile(), modDir);
                    return true;
                }
            } catch (IOException exception) {

                exception.printStackTrace();
                return false;
            }
        }

        return false;
    }
}
