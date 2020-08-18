package me.shadowchild.candor.mod;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

public class ModUtils {

    public static boolean checkAlreadyInstalled(File mod) {

        AtomicBoolean ret = new AtomicBoolean(false);

        ModsHandler.MODS.forEach(o -> {

            if(Mod.of(mod).equals(o)) {

                ret.set(true);
            }
        });

        return ret.get();
    }
}
