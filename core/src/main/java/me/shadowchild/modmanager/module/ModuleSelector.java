package me.shadowchild.modmanager.module;

import com.google.common.collect.Lists;
import me.shadowchild.cybernize.util.ClassLoadUtil;
import me.shadowchild.modmanager.util.Dialogs;

import java.io.File;
import java.util.ArrayList;

public class ModuleSelector {

    public static final ArrayList<IModule> MODULES = Lists.newArrayList();
    public static final IModule GENERIC_MODULE = instanceGenericModule();

    /**
     * Loads all the module from the 'Modules' folder
     */
    public static void initModules() {

        instanceGenericModule();

        // TODO: LOAD MODULE FROM DISK
    }

    /**
     * This method loads the GenericModule in to the array, as this will be a fallback if there is no module for a game
     */
    private static IModule instanceGenericModule() {

        try {

            Class<? extends IModule> clazz = ClassLoadUtil.loadClass("me.shadowchild.modmanager.GenericModule");
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {

            Dialogs.showInfoDialog(
                    "Candor Mod Manager",
                    "Candor did not load correctly.\nPlease restart or contact us at:\nhttps://discord.gg/CMG9ZtS",
                    "ok",
                    "error",
                    true);
            e.printStackTrace();
            System.exit(2);
        }
        return null;
    }

    public static IModule getModuleForGame(File gameExe) {

        for (IModule module : MODULES) {

            for(String s : module.acceptedExe()) {

                String gameString = gameExe.getName().substring(0, gameExe.getName().indexOf("."));
                if(s.equalsIgnoreCase(gameString)) {

                    return module;
                }
            }
        }
        return GENERIC_MODULE;
    }
}
