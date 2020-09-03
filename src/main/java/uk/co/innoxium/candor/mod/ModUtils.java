package uk.co.innoxium.candor.mod;

import uk.co.innoxium.candor.mod.store.ModStore;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

public class ModUtils {

    public static boolean checkAlreadyInstalled(File mod) {

        AtomicBoolean ret = new AtomicBoolean(false);

        ModStore.MODS.forEach(o -> {

            if(Mod.fromFile(mod).equals(o)) {

                ret.set(true);
            }
        });

        return ret.get();
    }
}
