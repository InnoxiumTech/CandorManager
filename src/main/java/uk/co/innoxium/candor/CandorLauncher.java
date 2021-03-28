package uk.co.innoxium.candor;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import org.lwjgl.system.Platform;
import uk.co.innoxium.candor.game.GamesList;
import uk.co.innoxium.candor.module.ModuleSelector;
import uk.co.innoxium.candor.tool.ToolsList;
import uk.co.innoxium.candor.util.Logger;
import uk.co.innoxium.candor.util.Resources;
import uk.co.innoxium.candor.util.WindowUtils;
import uk.co.innoxium.cybernize.zip.ZipUtils;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;


/**
 * The entry point for candor!
 */
public class CandorLauncher {

    private static boolean safeExit = false;

    public static void main(String[] args) {

        // Initialise our Logger
        Logger.initialise();

        Logger.info("Candor Initialising");
        // Ensure the 7zip natives are loaded
        ZipUtils.setUpSevenZip();

        if(Platform.get() == Platform.MACOSX) {

            // This is due to LWJGL being a bit finicky on OSX
            // We want to restart the application here with the -XstartOnFirstThread JVM argument
            // This is slightly more difficult for us as we have the java agent to deal with
            // TODO: this will be in the candor launcher as part of the launch rather than here
        }

        // Get a plash screen instance, currently, we don't do any manipulation, this may be increased in the future.
        SplashScreen splash = SplashScreen.getSplashScreen();
        if(splash != null) {

            Graphics2D g = splash.createGraphics();

            g.setPaintMode();
            g.setFont(new Font("Calibri", Font.PLAIN, 25));
            g.drawString("Loading Candor Resources", splash.getBounds().width / 2, splash.getBounds().height / 2);

            splash.update();
        }

        try {

            // We next want to install our fonts,
            Resources.installFonts();
        } catch(IOException | FontFormatException e) {

            // Print stack if it fails.
            e.printStackTrace();
        }

        try {

            // Check if the generic module exists, if not download it.
//            ModuleSelector.checkGenericModule();
            // Load modules from disk.
            ModuleSelector.initModules();
            // Load the lists of user set up games.
            GamesList.loadFromFile();
        } catch(Exception e) {

            // Probably from IDE, ignore for now
            e.printStackTrace();
        }
        // Handle our configurations
        ConfigHandler.handleCore();
        // Install our Look and Feel.
        if(Settings.darkTheme) {

            FlatDarculaLaf.install();
        } else {

            FlatIntelliJLaf.install();
        }
        // Set our theme customizations
        setThemeCustomizations();
        // Add our shutdown hook for saving configs
        Runtime.getRuntime().addShutdownHook(new RuntimeHook());

        // Close the splash screen now
        if(splash != null) splash.close();

        // We can now set up the frame.
        WindowUtils.initialiseFrame();
    }

    public static void safeExit() {

        safeExit(0);
    }

    public static void safeExit(int status) {

        safeExit = true;

        try {

            // Write the games and tools list to file
            GamesList.writeToFile();
            ToolsList.writeToJson();
        } catch(IOException e) {

            e.printStackTrace();
        }
        Logger.info("Candor shutting down.");


        System.exit(status);
    }

    private static void setThemeCustomizations() {

        // The following only works on Windows 10 currently, due it only being implemented on that platform
        // TODO: move our menubar in to the JFrame menubar
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);

        // Set the default font to ORBITRON
        UIManager.getLookAndFeelDefaults().put("defaultFont", Resources.orbitron.deriveFont(Font.PLAIN, 14));
    }

    private static class RuntimeHook extends Thread {

        @Override
        public void run() {

            if(!safeExit) {

                Logger.warn("Candor was not shut down using CandorLauncher.safeExit, thankfully we will handle this to ensure we are shut down correctly");

                try {

                    // Write the games and tools list to file
                    GamesList.writeToFile();
                    ToolsList.writeToJson();
                } catch(IOException e) {

                    e.printStackTrace();
                }
                Logger.info("Candor shutting down.");
            }
        }
    }
}