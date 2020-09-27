package uk.co.innoxium.candor.mod;

import uk.co.innoxium.candor.mod.store.ModStore;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class has utility methods for Mods
 */
public class ModUtils {

    /**
     * Check fit he mod is already installed.
     * @param mod - The mod to check.
     * @return - true if already installed, false if not.
     */
    public static boolean checkAlreadyInstalled(File mod) {

        // We have to use atomic, because a forEach is asynchronous.
        AtomicBoolean ret = new AtomicBoolean(false);

        ModStore.MODS.forEach(o -> {

            if(Mod.fromFile(mod).equals(o)) {

                ret.set(true);
            }
        });

        return ret.get();
    }
}
