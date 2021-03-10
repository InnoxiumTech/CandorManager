package uk.co.innoxium.candor.util;

import com.github.f4b6a3.uuid.util.UuidConverter;
import uk.co.innoxium.candor.Settings;
import uk.co.innoxium.candor.game.Game;
import uk.co.innoxium.candor.game.GamesList;
import uk.co.innoxium.candor.tool.ToolsList;
import uk.co.innoxium.candor.window.EntryScene;
import uk.co.innoxium.candor.window.GameSelectScene;
import uk.co.innoxium.candor.window.modscene.ModScene;
import uk.co.innoxium.cybernize.util.ClassLoadUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.UUID;


/**
 * Contains a bunch of helper methods for setting up the scenes.
 * Not for outside use, without proper knowledge.
 */
public class WindowUtils {

    /* The main JFrame we use to show the scenes. */
    public static JFrame mainFrame = new JFrame();

    /**
     * This method initialises the JFrame.
     */
    public static void initialiseFrame() {

        // We don't need this for the entry screen, instead we should show the entry screen if no default is found
        mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mainFrame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {

                boolean result = NativeDialogs.showInfoDialog(
                        "Candor Mod Manager",
                        "Are you sure you wish to exit?",
                        "yesno",
                        "question",
                        false);
                if(result) System.exit(0);
            }
        });
        mainFrame.setResizable(false);
        mainFrame.setTitle("Candor Mod Manager");
        mainFrame.setIconImage(new ImageIcon(ClassLoadUtil.getCL().getResource("logo.png")).getImage());
        // This check sets which scene would be loaded
        boolean showIntro = showIntroCheck();
        JPanel scene = showIntro ? new EntryScene() : new ModScene(Settings.defaultGameUuid);
        Resources.currentScene = scene;
        if(!showIntro)
            ToolsList.determineDefinedTools();
        mainFrame.setContentPane(scene);
        // TODO: Allow the window to stay on the same screen it was used on
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    // Sets up the frame for the mod scene, based on the game passed
    public static void setupModScene(Game game) {

        mainFrame.setVisible(false);
        mainFrame.setResizable(true);
        ModScene modScene = new ModScene(game.getUUID().toString());
        Resources.currentScene = modScene;
        ToolsList.determineDefinedTools();
        mainFrame.setContentPane(modScene);
        mainFrame.setMinimumSize(new Dimension(1200, 768));
        // TODO: Allow the window to stay on the same screen it was used on
        WindowUtils.mainFrame.pack();
        WindowUtils.mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    // Sets up the frame for the game selection scene.
    public static void setupGameSelectScene() {

//        mainFrame.setLocationByPlatform(true);
        mainFrame.setVisible(false);
        mainFrame.setResizable(false);
        mainFrame.setMinimumSize(new Dimension(435, 300));
        GameSelectScene scene = new GameSelectScene();
        scene.initComponents();
        Resources.currentScene = scene;
        mainFrame.setContentPane(scene);
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    // true for show intro, false for show mod scene
    private static boolean showIntroCheck() {

        // If the default uuid is set, we can continue
        if(Settings.defaultGameUuid.isBlank()) return true;

        UUID uuid = UuidConverter.fromString(Settings.defaultGameUuid);
        Game game = GamesList.getGameFromUUID(uuid);
        // If game is null, no game was found, so show the entry screen
        boolean ret = game == null;
        if(!ret) Logger.info("Found game to load " + game.getGameExe());
        return game == null; // returns true if game is null
    }

    // Sets up the frame for the entry scene
    public static void setupEntryScene() {

        mainFrame.setMinimumSize(new Dimension(0, 0));
        JPanel scene = new EntryScene();
        Resources.currentScene = scene;
        mainFrame.setContentPane(scene);
        // TODO: Allow the window to stay on the same screen it was used on
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }
}
