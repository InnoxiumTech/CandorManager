package uk.co.innoxium.candor.util;

import uk.co.innoxium.candor.Settings;
import uk.co.innoxium.candor.mod.store.ModStore;
import uk.co.innoxium.candor.module.AbstractModule;
import uk.co.innoxium.candor.module.ModuleSelector;
import uk.co.innoxium.candor.window.EntryScene;
import uk.co.innoxium.cybernize.util.ClassLoadUtil;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

public class WindowUtils {

    public static JFrame mainFrame = new JFrame();

    public static void initialiseEntryScene() {

        File game = new File(Settings.gameExe);
        AbstractModule module = ModuleSelector.getModuleForGame(game);
        module.setGame(game);
        module.setModsFolder(new File(Settings.modsFolder));
        ModStore.initialise();
        mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mainFrame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {

                boolean result = Dialogs.showInfoDialog(
                        "Candor Mod Manager",
                        "Are you sure you wish to exit?",
                        "yesno",
                        "question",
                        false);
                if(result) System.exit(0);
            }
        });
        mainFrame.setResizable(true);
        mainFrame.setTitle("Candor Mod Manager");
        mainFrame.setIconImage(new ImageIcon(ClassLoadUtil.getCL().getResource("logo.png")).getImage());
        EntryScene scene = new EntryScene();
//			ModScene modScene = new ModScene();
        Resources.currentScene = scene;
        mainFrame.setContentPane(scene);
//			frame.setMinimumSize(new Dimension(1200, 768));
        // TODO: Allow the window to stay on the same screen it was used on
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }
}
