package me.shadowchild.candor.module;

import ca.cgjennings.jvm.JarLoader;
import com.google.common.collect.Lists;
import me.shadowchild.candor.util.Dialogs;
import me.shadowchild.cybernize.util.ClassLoadUtil;
import me.shadowchild.cybernize.util.Download;
import me.shadowchild.cybernize.util.MathUtils;
import me.shadowchild.cybernize.util.Utils;
import org.apache.commons.io.FileUtils;

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

            Class<? extends AbstractModule> clazz = ClassLoadUtil.loadClass("me.shadowchild.candor.generic.GenericModule");
            GENERIC_MODULE = clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {

            e.printStackTrace();
            Dialogs.showCandorGenericFailure();
        }
    }

    public static AbstractModule getModuleForGame(File gameExe) {

        for (AbstractModule module : MODULES) {

            for(String s : module.acceptedExe()) {

                String gameString = gameExe.getName().substring(0, gameExe.getName().indexOf("."));
                System.out.println(s);
                System.out.println(gameString);
                if(s.equalsIgnoreCase(gameString)) {

                    System.out.println("Module is " + module.getModuleName());
                    currentModule = module;
                    return module;
                }
            }
        }
        currentModule = GENERIC_MODULE;
        return GENERIC_MODULE;
    }

    public static void checkGenericModule() throws IOException {

        File moduleJar = new File("./module/GenericModule.jar");
        String url = "https://dl.bintray.com/candor/candor-alpha/me/shadowchild/candor/candor-genericmodule/0.1.2/candor-genericmodule-0.1.2.jar";
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
