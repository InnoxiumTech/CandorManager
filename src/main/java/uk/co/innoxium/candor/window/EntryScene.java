/*
 * Created by JFormDesigner on Wed Sep 09 19:33:10 BST 2020
 */

package uk.co.innoxium.candor.window;

import net.miginfocom.swing.MigLayout;
import uk.co.innoxium.candor.Settings;
import uk.co.innoxium.candor.game.Game;
import uk.co.innoxium.candor.game.GamesList;
import uk.co.innoxium.candor.util.NativeDialogs;
import uk.co.innoxium.candor.util.Utils;
import uk.co.innoxium.candor.util.WindowUtils;
import uk.co.innoxium.cybernize.util.ClassLoadUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;


/**
 * The entry scene, to set up a game, or load game.
 */
public class EntryScene extends JPanel {

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JLabel candoLogo;
    private JComboBox gamesList;
    private JCheckBox defaultCheck;
    private JButton newGameButton;
    private JButton loadGameButton;

    // JFormDesigner - End of variables declaration  //GEN-END:variables

    public EntryScene() {

        initComponents();
    }

    private void createUIComponents() {

        // Create our components separate from JFormDesigner code.
        candoLogo = new JLabel(new ImageIcon(ClassLoadUtil.getCL().getResource("logo.png")));
        gamesList = new JComboBox<>(Utils.getVectorArrayFromList(GamesList.getGamesList()));
    }

    private void newGameClicked(ActionEvent e) {

        // Show the game selection scene.
        WindowUtils.setupGameSelectScene();
    }

    private void loadGameClicked(ActionEvent e) {

        // get the selected game from the list.
        Game game = (Game) gamesList.getSelectedItem();

        // if it's not null, we can load the mod scene from the game.
        if(game != null) {

            // if the default game checkbox is checked, set this game to now be the default.
            if(defaultCheck.isSelected()) {

                Settings.defaultGameUuid = game.getUUID().toString();

            }
            // The last game we loaded is now this one.
            Settings.lastGameUuid = game.getUUID().toString();
            // Set up the mod scene with the selected game.
            WindowUtils.setupModScene(game);
        } else {

            NativeDialogs.showInfoDialog(
                    "Candor Mod Manager",
                    "No game selected.\nPlease Select one before clicking this button.",
                    "ok",
                    "info",
                    true);
        }
    }

    // TODO: Remove as we set the default game once the load game button is clicked.
    private void defaultClicked(ActionEvent e) {

        Game game = (Game) gamesList.getSelectedItem();
        if(game != null) {

            Settings.defaultGameUuid = game.getUUID().toString();
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        createUIComponents();

        defaultCheck = new JCheckBox();
        newGameButton = new JButton();
        loadGameButton = new JButton();

        //======== this ========
        setLayout(new MigLayout(
                "hidemode 3",
                // columns
                "[fill]",
                // rows
                "[]" +
                        "[]" +
                        "[]"));
        add(candoLogo, "cell 0 0,dock center");
        add(gamesList, "cell 0 1,growx");

        //---- defaultCheck ----
        defaultCheck.setText("Default?");
        defaultCheck.addActionListener(this::defaultClicked);
        add(defaultCheck, "cell 0 1,alignx trailing,growx 0");

        //---- newGameButton ----
        newGameButton.setText("Load New Game");
        newGameButton.addActionListener(this::newGameClicked);
        add(newGameButton, "cell 0 2,alignx center,growx 0");

        //---- loadGameButton ----
        loadGameButton.setText("Load Selected Game");
        loadGameButton.addActionListener(this::loadGameClicked);
        add(loadGameButton, "cell 0 2,alignx center,growx 0");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }
}
