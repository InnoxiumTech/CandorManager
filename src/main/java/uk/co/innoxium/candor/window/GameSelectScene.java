package uk.co.innoxium.candor.window;

import net.miginfocom.swing.MigLayout;
import uk.co.innoxium.candor.Settings;
import uk.co.innoxium.candor.game.Game;
import uk.co.innoxium.candor.game.GamesList;
import uk.co.innoxium.candor.module.AbstractModule;
import uk.co.innoxium.candor.module.ModuleSelector;
import uk.co.innoxium.candor.util.NativeDialogs;
import uk.co.innoxium.candor.util.WindowUtils;
import uk.co.innoxium.candor.window.dnd.FileTransferHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;


public class GameSelectScene extends JPanel {

    // JFormDesigner - Variables declaration - DO NOT MODIFY
    // GEN-BEGIN:variables
    private JPanel contentPanel;
    private JLabel gameLabel;
    private JTextField gameField;
    private JButton gameBrowse;
    private JLabel modFolderLabel;
    private JTextField modFolderField;
    private JButton modFolderBrowse;
    private JCheckBox defaultGameCheckBox;
    private JCheckBox extractCheckBox;
    private JButton okButton;
    private JButton cancelButton;
    //GEN-END:variables
    // JFormDesigner - End of variables declaration

    public void setGame(File gameExe) {

        try {

            gameField.setText(gameExe.getCanonicalPath());
            AbstractModule module = ModuleSelector.getModuleForGame(gameExe.getName(), true);
            if(module.requiresModFolderSelection()) {

                modFolderField.setEnabled(true);
                modFolderBrowse.setEnabled(true);
            } else {

                modFolderField.setText("Mods Folder Not Needed By Module.");
            }
            if(module.getEnableExtractOption()) {

                extractCheckBox.setEnabled(true);
            }
        } catch(IOException exception) {

            exception.printStackTrace();
        }
    }

    public void setModsFolder(File modsFolder) {

        try {

            modFolderField.setText(modsFolder.getCanonicalPath());
        } catch(IOException exception) {

            exception.printStackTrace();
        }
    }

    public JTextField getGameField() {

        return gameField;
    }

    public JTextField getModFolderField() {

        return modFolderField;
    }

    private void gameExeClicked(ActionEvent e) {

        File gameExe = NativeDialogs.showSingleFileDialog("exe");
        setGame(gameExe);
    }

    private void modsFolderClicked(ActionEvent e) {

        File modsFolder = NativeDialogs.openPickFolder();
        setModsFolder(modsFolder);
    }

    private void cancelButtonClicked(ActionEvent e) {

        boolean result = NativeDialogs.showInfoDialog(
                "Candor Mod Manager",
                "Are you sure you wish to exit?",
                "yesno",
                "question",
                false);
        if(result) System.exit(0);
    }

    private void onButtonClicked(ActionEvent e) {

        boolean gameEmpty = gameField.getText().isEmpty();
        boolean folderEmpty = modFolderField.getText().isEmpty();
        boolean folderCondition = modFolderField.isEnabled() && (folderEmpty);

        if(gameEmpty || folderCondition) {

            StringBuilder builder = new StringBuilder();
            builder.append("You have not specified a:\n");
            if(gameEmpty) builder.append("-> Game Executable\n");
            if(modFolderField.isEnabled() && folderEmpty) builder.append("-> Mods Folder\n");
            builder.append("Please fill out the fields!");

            NativeDialogs.showInfoDialog(
                    "Candor Mod Manager",
                    builder.toString(),
                    "ok",
                    "info",
                    true);
        } else {

            // TODO: Break window loading into utility class
            File theGame = new File(gameField.getText());
            AbstractModule module = ModuleSelector.getModuleForGame(theGame.getName());
            module.setGame(theGame);
            if(module.requiresModFolderSelection())
                module.setModsFolder(new File(modFolderField.getText()));
            Game game = new Game(module.getGame().getAbsolutePath(), module.getModsFolder().getAbsolutePath(), module.getModuleName());
            GamesList.addGame(game);
            if(defaultGameCheckBox.isSelected()) {

                Settings.defaultGameUuid = game.getUUID().toString();
            }
            WindowUtils.setupModScene(game);
        }
    }

    private void checkBox(ActionEvent e) {

        JCheckBox checkbox = (JCheckBox) e.getSource();
    }

    private void extractorClicked(ActionEvent e) {

        JCheckBox checkbox = (JCheckBox) e.getSource();
        Settings.modExtract = checkbox.isSelected();
    }

    private void createUIComponents() {

        gameField = new JTextField();
        gameField.setDragEnabled(true);


        modFolderField = new JTextField();
    }

    public void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        createUIComponents();

        contentPanel = new JPanel();
        gameLabel = new JLabel();
        gameBrowse = new JButton();
        modFolderLabel = new JLabel();
        modFolderBrowse = new JButton();
        defaultGameCheckBox = new JCheckBox();
        extractCheckBox = new JCheckBox();
        okButton = new JButton();
        cancelButton = new JButton();

        //======== this ========
        setLayout(new BorderLayout());

        //======== contentPanel ========
        {
            contentPanel.setLayout(new MigLayout(
                "fill,novisualpadding,hidemode 3",
                // columns
                "[fill]" +
                "[fill]",
                // rows
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]"));

            //---- gameLabel ----
            gameLabel.setText("Please Select the Game Executable.");
            contentPanel.add(gameLabel, "cell 0 0");

            //---- gameField ----
            gameField.setDragEnabled(true);
            contentPanel.add(gameField, "cell 0 1 2 1,growx");

            //---- gameBrowse ----
            gameBrowse.setText("...");
            gameBrowse.addActionListener(e -> gameExeClicked(e));
            contentPanel.add(gameBrowse, "cell 0 1 2 1");

            //---- modFolderLabel ----
            modFolderLabel.setText("Please Locate the mods folder for this game.");
            contentPanel.add(modFolderLabel, "cell 0 2");

            //---- modFolderField ----
            modFolderField.setEnabled(false);
            contentPanel.add(modFolderField, "cell 0 3 2 1,aligny center,grow 100 0");

            //---- modFolderBrowse ----
            modFolderBrowse.setText("...");
            modFolderBrowse.setEnabled(false);
            modFolderBrowse.addActionListener(e -> modsFolderClicked(e));
            contentPanel.add(modFolderBrowse, "cell 0 3 2 1");

            //---- defaultGameCheckBox ----
            defaultGameCheckBox.setText("Set as Default");
            defaultGameCheckBox.addActionListener(e -> checkBox(e));
            contentPanel.add(defaultGameCheckBox, "cell 0 4 2 1,growx");

            //---- extractCheckBox ----
            extractCheckBox.setText("Mod Requires Extracting?");
            extractCheckBox.setEnabled(false);
            extractCheckBox.addActionListener(e -> extractorClicked(e));
            contentPanel.add(extractCheckBox, "cell 0 4 2 1,growx");

            //---- okButton ----
            okButton.setText("OK");
            okButton.addActionListener(e -> onButtonClicked(e));
            contentPanel.add(okButton, "cell 0 5 2 1,growx");

            //---- cancelButton ----
            cancelButton.setText("Cancel");
            cancelButton.addActionListener(e -> cancelButtonClicked(e));
            contentPanel.add(cancelButton, "cell 0 5 2 1,growx");
        }
        add(contentPanel, BorderLayout.CENTER);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents

        postCreate();
    }

    private void postCreate() {

        gameField.setTransferHandler(new FileTransferHandler());
        modFolderField.setTransferHandler(new FileTransferHandler());
    }
}
