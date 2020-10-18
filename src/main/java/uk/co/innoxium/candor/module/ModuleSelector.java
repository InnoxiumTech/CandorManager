package uk.co.innoxium.candor.module;

import ca.cgjennings.jvm.JarLoader;
import org.apache.commons.io.FileUtils;
import uk.co.innoxium.candor.game.Game;
import uk.co.innoxium.candor.util.NativeDialogs;
import uk.co.innoxium.candor.util.Logger;
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

/**
 * This class handles everything to do with modules.
 * This means loading, checking, setting, etc.
 */
public class ModuleSelector {

    // Our list of Modules
    public static final ArrayList<AbstractModule> MODULES = new ArrayList<>();
    // A Generic module, to handle any not officially supported games
    public static AbstractModule GENERIC_MODULE;

    // The current module
    public static AbstractModule currentModule = null;

    /**
     * Loads all the module from the 'Modules' folder
     */
    public static void initModules() throws Exception {

        loadFromDir(new File("./module"));
        Logger.info("Modules Found: " + MODULES.size());
        for(AbstractModule module : MODULES) Logger.info("Loaded Module: " + module.getModuleName());
        instanceGenericModule();
    }

    /**
     * Loads all modules from a directory
     * @param file - The folder to load the modules from
     * @throws Exception - If there was an error loading the module
     */
    private static void loadFromDir(File file) throws Exception {

        // If the folder doesn't exist, make it
        if(!file.exists()) file.mkdirs();

        // For each file in the directory, try to load it as a module
        for(File jar : file.listFiles()) {

            // Create a JarFile instance for the file
            JarFile jarFile = new JarFile(jar);
            // Get the manifest from the jar
            Manifest mf = jarFile.getManifest();
            // If the manifest isn't null, try to get the module data from it
            if(mf != null) {

                // Get the manifest attributes
                Attributes attrib = mf.getMainAttributes();
                // Get the module attribute
                String clazz = attrib.getValue("Candor-Module-Class");
                Logger.info(clazz);

                // Use JarLoader to add the jar in to our classpath
                JarLoader.addToClassPath(jar);
                // We can now load the class in to the runtime
                Class<? extends AbstractModule> theClazz = ClassLoadUtil.loadClass(clazz);
                // And now create an instance of the module, and add it to the module list.
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

            // Try to load the generic class in to the classpath and instantiate it
            Class<? extends AbstractModule> clazz = ClassLoadUtil.loadClass("uk.co.innoxium.candor.generic.GenericModule");
            GENERIC_MODULE = clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {

            // If it fails, show stack trace and then shutdown.
            e.printStackTrace();
            NativeDialogs.showCandorGenericFailure();
        }
    }

    /**
     * Gets a module instance form the specified game
     * @param gameExe - the game executable to check against the modules
     * @param showWarning - Should we show a warning if it falls back on the generic module
     * @return - An instance of a module to handle mod tasks
     */
    public static AbstractModule getModuleForGame(String gameExe, boolean showWarning) {

        // Remove the extension from the string
        String gameString = gameExe.substring(0, gameExe.indexOf(".")).toLowerCase();

        // For each module, check if the string is on the list of accepted executable names in the module
        for(AbstractModule module : MODULES) {

            for(String s : module.acceptedExe()) {

                // check without cases to ensure no errors
                if(s.toLowerCase().equals(gameString)) {

                    // Return the module instance
                    Logger.info("Module is " + module.getModuleName());
                    currentModule = module;
                    return module;
                }
            }
        }
        // If we should show the warning, do it now
        if(showWarning) {

            NativeDialogs.showInfoDialog(
                    "Candor Mod Manager",
                    "Warning, No module was found for this game.\n" +
                            "Defaulting to Generic Module, this may have unforseen consequences.\n" +
                            "For more information, please visit https://innoxium.co.uk/candor/modules",
                    "ok",
                    "warning",
                    true
            );
        }
        // We have fallen back on the generic module
        Logger.info("Module is " + GENERIC_MODULE.getModuleName());
        currentModule = GENERIC_MODULE;
        return GENERIC_MODULE;
    }

    /**
     * Gets a module instance form the specified game
     * @param game - the game executable to check against the modules
     * @param showWarning - Should we show a warning if it falls back on the generic module
     * @return - An instance of a module to handle mod tasks
     */
    public static AbstractModule getModuleForGame(Game game, boolean showWarning) {

        File file = new File(game.getGameExe());
        return getModuleForGame(file.getName(), showWarning);
    }

    /**
     * Gets a module instance form the specified game without the option for showing a warning
     * @param game - the game executable to check against the modules
     * @return - An instance of a module to handle mod tasks
     */
    public static AbstractModule getModuleForGame(Game game) {

        return getModuleForGame(game, false);
    }

    /**
     * Gets a module instance form the specified game without the option for showing a warning
     * @param gameExe - the game executable to check against the modules
     * @return - An instance of a module to handle mod tasks
     */
    public static AbstractModule getModuleForGame(String gameExe) {

        return getModuleForGame(gameExe, false);
    }

    /**
     * Checks to see if the generic module is installed, if not, downloads it.
     * @throws IOException - If there is an error downloading the generic module.
     */
    public static void checkGenericModule() throws IOException {

        // creates a File instance for the module
        File moduleJar = new File("./module/GenericModule.jar");
        // Gets the url to download from
        String url = findGenericModuleUrl();
        // gets the file name of the url
        String fileName = Utils.getFileName(new URL(url));
        if(!moduleJar.exists()) {

            // If the module jar doesnt exist, download the newest version available
            // TODO: Add maven repo resolving, for now just use a jar

            Utils.downloadFile(url, new DownloadObserver(), new File("./module"));
        }
        // Due to file utils being asynchronous, we should wait until the file can be seen
        while(!moduleJar.exists()) {

            try {

                Thread.sleep(20);
            } catch (InterruptedException e) {

                e.printStackTrace();
            }
        }
        // delete the downloaded file
        FileUtils.deleteQuietly(new File("./module", fileName));
    }

    /* Currently returns a url, hope to add a way to find the latest version eventually */
    private static String findGenericModuleUrl() {


        return "https://dl.bintray.com/candor/candor-alpha/uk/co/innoxium/candor/candor-genericmodule/0.2.0/candor-genericmodule-0.2.0.jar";
    }

    /**
     * A class to observe the state of the download.
     */
    private static class DownloadObserver implements Observer {

        @Override
        public void update(Observable o, Object arg) {

            Download dl = (Download)o;

            switch (dl.getStatus()) {

                // If downloading, print the progress of it
                case Download.DOWNLOADING -> Logger.info("Progress = " + dl.getProgress() + ", " +
                        MathUtils.humanReadableByteCount(dl.getDownloaded(), false) + " / " +
                        MathUtils.humanReadableByteCount(dl.getSize(), false));
                // If complete, copy the file
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
