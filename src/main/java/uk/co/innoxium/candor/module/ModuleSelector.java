package uk.co.innoxium.candor.module;

import ca.cgjennings.jvm.JarLoader;
import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import uk.co.innoxium.candor.game.Game;
import uk.co.innoxium.candor.util.Dialogs;
import uk.co.innoxium.cybernize.util.ClassLoadUtil;
import uk.co.innoxium.cybernize.util.Download;
import uk.co.innoxium.cybernize.util.MathUtils;
import uk.co.innoxium.cybernize.util.Utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class ModuleSelector {

    public static final ArrayList<AbstractModule> MODULES = Lists.newArrayList();
    public static AbstractModule GENERIC_MODULE;

    public static AbstractModule currentModule = null;

    /**
     * Loads all the module from the 'Modules' folder
     */
    public static void initModules() throws Exception {

        loadFromDir(new File("./module"));
        System.out.println("Modules Found: " + MODULES.size());
        for(AbstractModule module : MODULES) System.out.println("Loaded Module: " + module.getModuleName());
        instanceGenericModule();
    }

    private static void loadFromDir(File file) throws Exception {

        if(!file.exists()) file.mkdirs();

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
    private static void instanceGenericModule() {

        try {

            Class<? extends AbstractModule> clazz = ClassLoadUtil.loadClass("uk.co.innoxium.candor.generic.GenericModule");
            GENERIC_MODULE = clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {

            e.printStackTrace();
            Dialogs.showCandorGenericFailure();
        }
    }

    public static AbstractModule getModuleForGame(String gameExe, boolean showWarning) {

        File file = new File(gameExe);
        String gameString = file.getName().substring(0, file.getName().indexOf("."));

        for(AbstractModule module : MODULES) {

            for(String s : module.acceptedExe()) {

//                System.out.println(s);
//                System.out.println(gameString);
                if(s.equalsIgnoreCase(gameString)) {

                    System.out.println("Module is " + module.getModuleName());
                    currentModule = module;
                    return module;
                }
            }
        }
        if(showWarning) {

            Dialogs.showInfoDialog(
                    "Candor Mod Manager",
                    "Warning, No module was found for this game.\n" +
                            "Defaulting to Generic Module, this may have unforseen consequences.\n" +
                            "For more information, please visit https://innoxium.co.uk/candor/modules",
                    "ok",
                    "warning",
                    true
            );
        }
        System.out.println("Module is " + GENERIC_MODULE.getModuleName());
        currentModule = GENERIC_MODULE;
        return GENERIC_MODULE;
    }

    public static AbstractModule getModuleForGame(Game game, boolean showWarning) {

        return getModuleForGame(game.getGameExe(), showWarning);
    }

    public static AbstractModule getModuleForGame(Game game) {

        return getModuleForGame(game.getGameExe(), false);
    }

    public static AbstractModule getModuleForGame(String gameExe) {

        return getModuleForGame(gameExe, false);
    }

    public static void checkGenericModule() throws IOException {

        File moduleJar = new File("./module/GenericModule.jar");
        String url = findGenericModuleUrl();
        String fileName = Utils.getFileName(new URL(url));
        if(!moduleJar.exists()) {

            // If the module jar doesnt exist, download the newest version available
            // TODO: Add maven repo resolving, for now just use a jar

            Utils.downloadFile(url, new DownloadObserver(), new File("./module"));
        }
        while(!moduleJar.exists()) {

            try {

                Thread.sleep(20);
            } catch (InterruptedException e) {

                e.printStackTrace();
            }
        }
        FileUtils.deleteQuietly(new File("./module", fileName));
    }

    private static String findGenericModuleUrl() {


        return "https://dl.bintray.com/candor/candor-alpha/uk/co/innoxium/candor/candor-genericmodule/0.2.0/candor-genericmodule-0.2.0.jar";
    }

    private static class DownloadObserver implements Observer {

        @Override
        public void update(Observable o, Object arg) {

            Download dl = (Download)o;

            switch (dl.getStatus()) {

                case Download.DOWNLOADING -> System.out.println("Progress = " + dl.getProgress() + ", " +
                        MathUtils.humanReadableByteCount(dl.getDownloaded(), false) + " / " +
                        MathUtils.humanReadableByteCount(dl.getSize(), false));
                case Download.COMPLETE -> {

                    try {

                        FileUtils.copyFile(new File("./module", dl.getUrl().substring(dl.getUrl().lastIndexOf("/"))), new File("./module", "GenericModule.jar"));
                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
