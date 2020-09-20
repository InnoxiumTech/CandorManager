/*
 * Created by JFormDesigner on Wed Sep 09 19:33:10 BST 2020
 */

package uk.co.innoxium.candor.window;

import net.miginfocom.swing.MigLayout;
import uk.co.innoxium.candor.Settings;
import uk.co.innoxium.candor.game.Game;
import uk.co.innoxium.candor.game.GamesList;
import uk.co.innoxium.candor.util.Utils;
import uk.co.innoxium.candor.util.WindowUtils;
import uk.co.innoxium.cybernize.util.ClassLoadUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author Zach Piddock
 */
public class EntryScene extends JPanel {
    public EntryScene() {
        initComponents();
    }

    private void createUIComponents() {

        candoLogo = new JLabel(new ImageIcon(ClassLoadUtil.getCL().getResource("logo.png")));
        gamesList = new JComboBox<>(Utils.getVectorArrayFromList(GamesList.getGamesList()));
    }

    private void newGameClicked(ActionEvent e) {

        WindowUtils.setupGameSelectScene();
    }

    private void loadGameClicked(ActionEvent e) {

        Game game = (Game)gamesList.getSelectedItem();
        if(game != null) {

            if(defaultCheck.isSelected()) {

                Settings.defaultGameUuid = game.getUUID().toString();
            }
            Settings.lastGameUuid = game.getUUID().toString();
//            AbstractModule module = ModuleSelector.getModuleForGame(game.getGameExe(), true);
            WindowUtils.setupModScene(game);
        }
    }

    private void defaultClicked(ActionEvent e) {

        Game game = (Game)gamesList.getSelectedItem();
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
        defaultCheck.addActionListener(e -> defaultClicked(e));
        add(defaultCheck, "cell 0 1,alignx trailing,growx 0");

        //---- newGameButton ----
        newGameButton.setText("Load New Game");
        newGameButton.addActionListener(e -> newGameClicked(e));
        add(newGameButton, "cell 0 2,alignx center,growx 0");

        //---- loadGameButton ----
        loadGameButton.setText("Load Selected Game");
        loadGameButton.addActionListener(e -> loadGameClicked(e));
        add(loadGameButton, "cell 0 2,alignx center,growx 0");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JLabel candoLogo;
    private JComboBox gamesList;
    private JCheckBox defaultCheck;
    private JButton newGameButton;
    private JButton loadGameButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
