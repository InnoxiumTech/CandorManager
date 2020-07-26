package me.shadowchild.candor.module;

import com.google.common.collect.Lists;
import ca.cgjennings.jvm.JarLoader;
import me.shadowchild.candor.util.Dialogs;
import me.shadowchild.cybernize.util.ClassLoadUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class ModuleSelector {

    public static final ArrayList<AbstractModule> MODULES = Lists.newArrayList();
    public static final AbstractModule GENERIC_MODULE = instanceGenericModule();

    public static AbstractModule currentModule = null;

    /**
     * Loads all the module from the 'Modules' folder
     */
    public static void initModules() throws Exception {

        loadFromDir(new File("./module"));

//        try {
//
        // We happen to be testing with mass effect, no idea why, first game i clicked on
//            Class<? extends AbstractModule> clazz = ClassLoadUtil.loadClass("me.shadowchild.modmanager.MassEffectModule");
//            clazz.getDeclaredConstructor().newInstance();
//        } catch (Exception e) {
//
//            e.printStackTrace();
//        }
        // TODO: LOAD MODULE FROM DISK
    }

    private static void loadFromDir(File file) throws Exception {

        for(File jar : file.listFiles()) {

            JarFile jarFile = new JarFile(jar);
            Manifest mf = jarFile.getManifest();
            if(mf != null) {

                Attributes attrib = mf.getMainAttributes();
                String clazz = attrib.getValue("Candor-Module-Class");
                System.out.println(clazz);

                JarLoader.addToClassPath(jar);
                Class<? extends AbstractModule> theClazz = ClassLoadUtil.loadClass(clazz);
                MODULES.add(theClazz.getDeclaredConstructor().newInstance());
            }
            // Jar has no manifest, this means we cannot load a module from it
        }
    }

    /**
     * This method loads the GenericModule in to the array, as this will be a fallback if there is no module for a game
     */
    private static AbstractModule instanceGenericModule() {

        try {

            Class<? extends AbstractModule> clazz = ClassLoadUtil.loadClass("me.shadowchild.candor.generic.GenericModule");
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

    public static AbstractModule getModuleForGame(File gameExe) {

        for (AbstractModule module : MODULES) {

            for(String s : module.acceptedExe()) {

                String gameString = gameExe.getName().substring(0, gameExe.getName().indexOf("."));
                if(s.equalsIgnoreCase(gameString)) {

                    currentModule = module;
                    return module;
                }
            }
        }
        currentModule = GENERIC_MODULE;
        return GENERIC_MODULE;
    }
}
